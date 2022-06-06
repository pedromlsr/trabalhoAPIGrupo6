package org.serratec.ecommerce.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.dtos.PedidoResDTO;
import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.entities.Pedido;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.exceptions.PedidoException;
import org.serratec.ecommerce.repositories.ClienteRepository;
import org.serratec.ecommerce.repositories.PedidoRepository;
import org.serratec.ecommerce.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	ItemPedidoService itemPedidoService;

	@Autowired
	EmailService emailService;

	public List<PedidoResDTO> findAllPedido() {
		if (pedidoRepository.findAll().isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum Pedido encontrado.");
		} else {
			List<PedidoResDTO> pedidoResDTOList = new ArrayList<>();

			for (Pedido pedido : pedidoRepository.findAll()) {
				Double valorLiqTotal = 0.0;
				for (ItemPedido itemPedido : itemPedidoService.findAllItemPedido()) {
					if (pedido.getIdPedido() == itemPedido.getPedido().getIdPedido()) {
						if (itemPedido.getValorLiquido() != null) {
							valorLiqTotal += itemPedido.getValorLiquido();
						}
					}
				}

				PedidoResDTO pedidoResDTO = convertEntityToDTO(pedido);
				pedidoResDTO.setValorLiqTotal(valorLiqTotal);

				pedidoResDTOList.add(pedidoResDTO);
			}
			return pedidoResDTOList;
		}
	}

	public Pedido findPedidoById(Integer id) {
		return pedidoRepository.existsById(id) ? pedidoRepository.findById(id).get() : null;
	}
	
	public PedidoResDTO findPedidoByIdDTO(Integer id) {		
		if(!pedidoRepository.existsById(id)) {
			throw new NoSuchElementFoundException("O Pedido de id = " + id + " não foi encontrado.");
		}		
		Pedido pedido = pedidoRepository.findById(id).get();
		Double valorLiqTotal = 0.0;
		for (ItemPedido itemPedido : pedido.getItemPedidoList()) {
			if (pedido.getIdPedido() == itemPedido.getPedido().getIdPedido()) {
				if (itemPedido.getValorLiquido() != null) {
					valorLiqTotal += itemPedido.getValorLiquido();
				}
			}
		}
		PedidoResDTO pedidoResDTO = convertEntityToDTO(pedido);
		pedidoResDTO.setValorLiqTotal(valorLiqTotal);
		return pedidoResDTO;
	}


	public PedidoResDTO savePedido(PedidoReqDTO pedidoReqDTO) {
		Pedido pedido = new Pedido();

		pedido.setDataPedido(LocalDate.now());
		pedido.setStatus(statusRepository.findById(1).get());
		
		if (pedidoReqDTO.getIdCliente() == null) {
			throw new PedidoException("Não foi informado um id para o Cliente.");
		}
		
		if (!clienteRepository.existsById(pedidoReqDTO.getIdCliente())) {
			throw new NoSuchElementFoundException("O Cliente de id = " + pedidoReqDTO.getIdCliente() + " não foi encontrado.");
		}
		
		pedido.setCliente(clienteRepository.findById(pedidoReqDTO.getIdCliente()).get());

		Pedido pedidoSave = pedidoRepository.save(pedido);
		pedidoReqDTO.setIdPedido(pedidoSave.getIdPedido());

		PedidoReqDTO novoPedidoReqDTO = itemPedidoService.salvarItemPedido(pedidoReqDTO);

		emailService.enviarEmailHtml(novoPedidoReqDTO);

		PedidoResDTO pedidoResDTO = convertEntityToDTO(pedido);

		pedidoResDTO.setValorLiqTotal(novoPedidoReqDTO.getValorLiqTotal());

		return pedidoResDTO;
	}

	public PedidoResDTO updatePedido(PedidoReqDTO pedidoReqDTO) {
		if (pedidoReqDTO.getIdPedido() == null) {
			throw new PedidoException("Não foi informado um id para o Pedido.");
		}
		
		if (!pedidoRepository.existsById(pedidoReqDTO.getIdPedido())) {
			throw new NoSuchElementFoundException("Não foi possível atualizar. O Pedido de id = "
					+ pedidoReqDTO.getIdPedido() + " não foi encontrado.");
		}
		
		if (findPedidoById(pedidoReqDTO.getIdPedido()).getStatus().getIdStatus() != 1) {
			throw new PedidoException("Esta requisição só pode ser realizada enquanto o pedido estiver em status de 'Aguardando pagamento'.");
		}
		
		Pedido pedidoBD = findPedidoById(pedidoReqDTO.getIdPedido());

		for (ItemPedido itemPedido : pedidoBD.getItemPedidoList()) {
			itemPedido.getProduto().setQtdEstoque(itemPedido.getProduto().getQtdEstoque() + itemPedido.getQuantidade());
			itemPedidoService.deleteItemPedidoById(itemPedido.getIdItemPedido());
		}
		
		PedidoReqDTO novoPedidoReqDTO = itemPedidoService.salvarItemPedido(pedidoReqDTO);

		PedidoResDTO pedidoResDTO = convertEntityToDTO(pedidoRepository.findById(pedidoReqDTO.getIdPedido()).get());

		pedidoResDTO.setValorLiqTotal(novoPedidoReqDTO.getValorLiqTotal());

		return pedidoResDTO;

	}

	public PedidoResDTO updatePedidoStatus(Integer idPedido, Integer idStatus) {
		if (!pedidoRepository.existsById(idPedido)) {
			throw new NoSuchElementFoundException(
					"Não foi possível atualizar. O Pedido de id = " + idPedido + " não foi encontrado.");
		}
		if (idStatus != 2 && idStatus != 3) {
			throw new PedidoException(
					"Esta requisição só pode ser realizada para os status de id 2 e 3 (Enviado e Entregue).");
		}
		if (idStatus == 3 && (findPedidoById(idPedido).getDataEnvio() == null)) {
			throw new PedidoException(
					"Não é possível definir como entregue um pedido que ainda não foi enviado.");
		}
		if (findPedidoById(idPedido).getDataEntrega() != null) {
			throw new PedidoException(
					"Não é possível alterar o status de um pedido já finalizado.");
		}
		
		Pedido pedido = pedidoRepository.findById(idPedido).get();

		pedido.setStatus(statusRepository.findById(idStatus).get());

		if (idStatus == 2) {
			pedido.setDataEnvio(LocalDate.now());
		}

		if (idStatus == 3) {
			pedido.setDataEntrega(LocalDate.now());
		}

		return convertEntityToDTO(pedidoRepository.save(pedido));
	}

	public void deletePedidoById(Integer id) {
		if (!pedidoRepository.existsById(id)) {
			throw new NoSuchElementFoundException(
					"Não foi possível excluir. O Pedido de id = " + id + " não foi encontrado.");
		}
		
		for (ItemPedido itemPedido : itemPedidoService.findAllItemPedido()) {

			if (itemPedido.getPedido().getIdPedido() == id) {
				itemPedidoService.deleteItemPedidoById(itemPedido.getIdItemPedido());
			}
		}
		pedidoRepository.deleteById(id);

	}

	private PedidoResDTO convertEntityToDTO(Pedido pedido) {
		PedidoResDTO pedidoResDTO = new PedidoResDTO();

		pedidoResDTO.setIdPedido(pedido.getIdPedido());
		pedidoResDTO.setDataPedido(pedido.getDataPedido());
		pedidoResDTO.setDataEnvio(pedido.getDataEnvio());
		pedidoResDTO.setDataEntrega(pedido.getDataEntrega());
		pedidoResDTO.setDescricaoStatus(pedido.getStatus().getDescricaoStatus());
		pedidoResDTO.setIdCliente(pedido.getCliente().getIdCliente());
		pedidoResDTO.setNomeCliente(pedido.getCliente().getNomeCompleto());

		return pedidoResDTO;
	}

}

package org.serratec.ecommerce.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.dtos.PedidoResDTO;
import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.entities.Pedido;
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
			return null;
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
			return null;
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


	public PedidoResDTO savePedido(PedidoReqDTO pedidoReqDTO) throws MessagingException {
		Pedido pedido = new Pedido();

		pedido.setDataPedido(LocalDate.now());
		pedido.setStatus(statusRepository.findById(1).get());
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

		Pedido pedidoBD = findPedidoById(pedidoReqDTO.getIdPedido());

		if (pedidoReqDTO.getItemPedidoList().size() >= pedidoBD.getItemPedidoList().size()) {

			for (int i = 0; i < pedidoBD.getItemPedidoList().size(); i++) {

				pedidoReqDTO.getItemPedidoList().get(i)
						.setIdItemPedido(pedidoBD.getItemPedidoList().get(i).getIdItemPedido());

			}

		} else {
			
			for (ItemPedido itemPedido : pedidoBD.getItemPedidoList()) {
				itemPedidoService.deleteItemPedidoById(itemPedido.getIdItemPedido());
			}

		}

		PedidoReqDTO novoPedidoReqDTO = itemPedidoService.salvarItemPedido(pedidoReqDTO);

		PedidoResDTO pedidoResDTO = convertEntityToDTO(pedidoRepository.findById(pedidoReqDTO.getIdPedido()).get());

		pedidoResDTO.setValorLiqTotal(novoPedidoReqDTO.getValorLiqTotal());

		return pedidoResDTO;

	}

	public PedidoResDTO updatePedidoStatus(Integer idPedido, Integer idStatus) {
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

	public Pedido atualizarEnvioPedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	public void deletePedidoById(Integer id) {

		for (ItemPedido itemPedido : itemPedidoService.findAllItemPedido()) {

			if (itemPedido.getPedido().getIdPedido() == id) {
				itemPedidoService.deleteItemPedidoById(itemPedido.getIdItemPedido());
			}
		}
		pedidoRepository.deleteById(id);

	}

//	private Pedido convertDTOToEntity(PedidoReqDTO pedidoReqDTO) {
//	
//	}

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

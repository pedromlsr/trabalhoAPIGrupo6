package org.serratec.ecommerce.services;

import java.util.List;

import org.serratec.ecommerce.entities.Pedido;
import org.serratec.ecommerce.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
	@Autowired
	PedidoRepository pedidoRepository;

	public List<Pedido> findAllPedido() {
		return pedidoRepository.findAll();
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


	public PedidoResDTO savePedido(PedidoReqDTO pedidoReqDTO) {
		Pedido pedido = new Pedido();

		pedido.setDataPedido(LocalDate.now());
		pedido.setStatus(statusRepository.findById(1).get());
		pedido.setCliente(clienteRepository.findById(pedidoReqDTO.getIdCliente()).get());

		Pedido pedidoSave = pedidoRepository.save(pedido);
		pedidoReqDTO.setIdPedido(pedidoSave.getIdPedido());

		PedidoReqDTO novoPedidoReqDTO = itemPedidoService.salvarItemPedido(pedidoReqDTO);

		emailService.enviarEmailTexto(novoPedidoReqDTO);

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

	public Pedido savePedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	public Pedido updatePedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	public void deletePedidoById(Integer id) {
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

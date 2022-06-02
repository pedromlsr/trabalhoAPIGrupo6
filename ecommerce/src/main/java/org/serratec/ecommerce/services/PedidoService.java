package org.serratec.ecommerce.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.dtos.PedidoResDTO;
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

	public List<PedidoResDTO> findAllPedido() {
		if (pedidoRepository.findAll().isEmpty()) {
			return null;
		} else {
			List<PedidoResDTO> pedidoResDTOList = new ArrayList<>();

			for (Pedido pedido : pedidoRepository.findAll()) {
				pedidoResDTOList.add(convertEntityToDTO(pedido));
			}
			return pedidoResDTOList;
		}
	}

	public PedidoResDTO findPedidoById(Integer id) {
		return pedidoRepository.existsById(id) ? convertEntityToDTO(pedidoRepository.findById(id).get()) : null;
	}

	public PedidoResDTO savePedido(PedidoReqDTO pedidoReqDTO) {
		Pedido pedido = new Pedido();

		pedido.setDataPedido(LocalDate.now());
		pedido.setStatus(statusRepository.findById(1).get());
		pedido.setCliente(clienteRepository.findById(pedidoReqDTO.getIdCliente()).get());

		return convertEntityToDTO(pedidoRepository.save(pedido));
	}

	public PedidoResDTO updatePedido(Integer idPedido, Integer idStatus) {
		Pedido pedido = pedidoRepository.findById(idPedido).get();

		pedido.setStatus(statusRepository.findById(idStatus).get());

		if (idStatus == 2) {
			pedido.setDataEnvio(LocalDate.now());
		}

		if (idStatus == 3) {
			pedido.setDataEntrega(LocalDate.now());
		}

		pedidoRepository.save(pedido);

		return convertEntityToDTO(pedido);
	}

	public Pedido atualizarEnvioPedido(Pedido pedido) {
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

package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.dtos.PedidoResDTO;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
	@Autowired
	PedidoService pedidoService;

	@GetMapping
	public ResponseEntity<List<PedidoResDTO>> findAllPedido() {
		if (pedidoService.findAllPedido() == null) {
			throw new NoSuchElementFoundException("Nenhum pedido encontrado.");
		}

		return new ResponseEntity<>(pedidoService.findAllPedido(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PedidoResDTO> findPedidoByIdDTO(@PathVariable Integer id) {
		if (pedidoService.findPedidoByIdDTO(id) == null) {
			throw new NoSuchElementFoundException("O Pedido de id = " + id + " não foi encontrado.");
		}

		return new ResponseEntity<>(pedidoService.findPedidoByIdDTO(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<PedidoReqDTO> savePedido(@RequestBody PedidoReqDTO pedidoReqDTO) {
		return new ResponseEntity<>(pedidoService.savePedido(pedidoReqDTO), HttpStatus.CREATED);
	}
	
	@PutMapping("/{idPedido}/{idStatus}")
	public ResponseEntity<PedidoResDTO> updatePedido(@PathVariable Integer idPedido, @PathVariable Integer idStatus) {
		if (pedidoService.findPedidoById(idPedido) == null) {
			throw new NoSuchElementFoundException(
					"Não foi possível atualizar. O Pedido de id = " + idPedido + " não foi encontrado.");
		}
		if (idStatus != 2 && idStatus != 3) {
			// Criar exception customizada
			throw new NoSuchElementFoundException("Esta requisição só pode ser efetuada para os status de id 2 e 3 (Enviado e Entregue).");
		}
		if (idStatus == 3 && (pedidoService.findPedidoById(idPedido).getDataEnvio() == null)) {
			// Criar exception customizada
			throw new NoSuchElementFoundException("Não é possível definir como entregue um pedido que ainda não foi enviado.");
		}

		return new ResponseEntity<>(pedidoService.updatePedido(idPedido, idStatus), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePedidoById(@PathVariable Integer id) {
		if (pedidoService.findPedidoById(id) == null) {
			throw new NoSuchElementFoundException(
					"Não foi possível excluir. O Pedido de id = " + id + " não foi encontrado.");
		}

		pedidoService.deletePedidoById(id);
		return new ResponseEntity<>("O Pedido de id = " + id + " foi excluído com sucesso.", HttpStatus.OK);
	}

}

package org.serratec.ecommerce.controllers;

import java.util.Date;
import java.util.List;

import org.serratec.ecommerce.entities.Pedido;
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
	public ResponseEntity<List<Pedido>> findAllPedido() {
		List<Pedido> pedidoList = pedidoService.findAllPedido();

		if (pedidoList.isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum pedido encontrado.");
		}

		return new ResponseEntity<>(pedidoList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> findPedidoById(@PathVariable Integer id) {
		Pedido pedido = pedidoService.findPedidoById(id);
		if (pedido == null) {
			throw new NoSuchElementFoundException("O Pedido de id = " + id + " não foi encontrado.");
		}

		return new ResponseEntity<>(pedido, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Pedido> savePedido(@RequestBody Pedido pedido) {
		Date date = new Date();
		
		if (pedido.getDataPedido() != null) {
			if (pedido.getDataPedido().before(date)) {
				return new ResponseEntity<>(pedidoService.savePedido(pedido), HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(pedidoService.savePedido(pedido), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Pedido> updatePedido(@RequestBody Pedido pedido) {
		if (pedidoService.findPedidoById(pedido.getIdPedido()) == null) {
			throw new NoSuchElementFoundException("Não foi possível atualizar. O Pedido de id = "
					+ pedido.getIdPedido() + " não foi encontrado.");
		}
		
		return new ResponseEntity<>(pedidoService.updatePedido(pedido), HttpStatus.OK);
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

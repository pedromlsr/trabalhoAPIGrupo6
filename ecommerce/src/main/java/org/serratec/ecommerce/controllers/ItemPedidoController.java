package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.ItemPedidoDTO;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.services.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/itemPedido")
public class ItemPedidoController {
	@Autowired
	ItemPedidoService itemPedidoService;

	@GetMapping
	public ResponseEntity<List<ItemPedidoDTO>> findAllItemPedidoDTO() {
		if (itemPedidoService.findAllItemPedidoDTO() == null) {
			throw new NoSuchElementFoundException("Nenhum itemPedido encontrado.");
		}
		return new ResponseEntity<>(itemPedidoService.findAllItemPedidoDTO(), HttpStatus.OK);
	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<ItemPedido> findItemPedidoById(@PathVariable Integer id) {
//		ItemPedido itemPedido = itemPedidoService.findItemPedidoById(id);
//		return new ResponseEntity<>(itemPedido, HttpStatus.OK);
//	}
//
//	@PostMapping
//	public ResponseEntity<ItemPedido> saveItemPedido(@RequestBody ItemPedido itemPedido) {
//		return new ResponseEntity<>(itemPedidoService.saveItemPedido(itemPedido), HttpStatus.CREATED);
//	}
//
//	@PutMapping
//	public ResponseEntity<ItemPedido> updateItemPedido(@RequestBody ItemPedido itemPedido) {
//		ItemPedido itemPedidoAtualizado = itemPedidoService.updateItemPedido(itemPedido);
//		return new ResponseEntity<>(itemPedidoAtualizado, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/{id}")
//	public ResponseEntity<String> deleteItemPedidoById(@PathVariable Integer id) {
//		itemPedidoService.deleteItemPedidoById(id);
//		return new ResponseEntity<>("", HttpStatus.OK);
//	}

}

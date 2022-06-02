package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.services.ItemPedidoService;
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
@RequestMapping("/itemPedido")
public class ItemPedidoController {
	@Autowired
	ItemPedidoService itemPedidoService;

	@GetMapping
	public ResponseEntity<List<ItemPedido>> findAllItemPedido() {
		List<ItemPedido> itemPedidoList = itemPedidoService.findAllItemPedido();
		return new ResponseEntity<>(itemPedidoList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ItemPedido> findItemPedidoById(@PathVariable Integer id) {
		ItemPedido itemPedido = itemPedidoService.findItemPedidoById(id);
		return new ResponseEntity<>(itemPedido, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ItemPedido> saveItemPedido(@RequestBody ItemPedido itemPedido) {
		return new ResponseEntity<>(itemPedidoService.saveItemPedido(itemPedido), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<ItemPedido> updateItemPedido(@RequestBody ItemPedido itemPedido) {
		ItemPedido itemPedidoAtualizado = itemPedidoService.updateItemPedido(itemPedido);
		return new ResponseEntity<>(itemPedidoAtualizado, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteItemPedidoById(@PathVariable Integer id) {
		itemPedidoService.deleteItemPedidoById(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}

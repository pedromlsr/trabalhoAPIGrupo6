package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.EnderecoDTO;
import org.serratec.ecommerce.dtos.ItemPedidoDTO;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.services.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itemPedido")
@Tag(name = "Item-Pedido")
public class ItemPedidoController {
	@Autowired
	ItemPedidoService itemPedidoService;

	@GetMapping
	@Operation(summary = "Busca todas as relações item-pedido cadastradas.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os item-pedido cadastrados.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ItemPedidoDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum item-pedido encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
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

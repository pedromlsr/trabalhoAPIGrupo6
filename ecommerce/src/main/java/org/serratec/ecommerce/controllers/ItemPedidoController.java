package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.ItemPedidoDTO;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.services.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
		return new ResponseEntity<>(itemPedidoService.findAllItemPedidoDTO(), HttpStatus.OK);
	}

	@Operation(summary = "Busca um itemPedido cadastrado no sistema através do seu ID.", parameters = {
			@Parameter(name = "id", description = "ID do itemPedido desejado.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna o itemPedido que possui o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemPedidoDTO.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Não há um itemPedido cadastrado no sistema que possua o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	@GetMapping("/{id}")
	public ResponseEntity<ItemPedidoDTO> findItemPedidoByIdDTO(@PathVariable Integer id) {
		return new ResponseEntity<>(itemPedidoService.findItemPedidoByIdDTO(id), HttpStatus.OK);
	}
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

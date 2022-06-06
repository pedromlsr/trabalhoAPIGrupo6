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
	@Operation(summary = "Busca todos os ItemPedido cadastrados no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os ItemPedido cadastrados no sistema.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ItemPedidoDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum ItemPedido encontrado no sistema.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<List<ItemPedidoDTO>> findAllItemPedidoDTO() {
		return new ResponseEntity<>(itemPedidoService.findAllItemPedidoDTO(), HttpStatus.OK);
	}

	@Operation(summary = "Busca um ItemPedido cadastrado no sistema através do seu id.", parameters = {
			@Parameter(name = "id", description = "Id do itemPedido desejado.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna o ItemPedido que possui o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemPedidoDTO.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Nenhum ItemPedido encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
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

package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.dtos.PedidoResDTO;
import org.serratec.ecommerce.exceptions.ErrorResponse;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pedido")
@Tag(name = "Pedido")
public class PedidoController {
	@Autowired
	PedidoService pedidoService;

	@GetMapping
	@Operation(summary = "Busca todos os pedidos cadastrados no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os pedidos cadastrados no sistema.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PedidoResDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum pedido encontrado no sistema.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<List<PedidoResDTO>> findAllPedido() {
		return new ResponseEntity<>(pedidoService.findAllPedido(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um pedido cadastrado no sistema através do seu id.", parameters = {
			@Parameter(name = "id", description = "Id do pedido desejado.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna o pedido que possui o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResDTO.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Nenhum pedido encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<PedidoResDTO> findPedidoByIdDTO(@PathVariable Integer id) {
		return new ResponseEntity<>(pedidoService.findPedidoByIdDTO(id), HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Cadastra um novo pedido no sistema e retorna seus dados.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra o pedido no sistema e retorna seus dados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Falha. Erro na requisição.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<PedidoResDTO> savePedido(@RequestBody PedidoReqDTO pedidoReqDTO) {
		return new ResponseEntity<>(pedidoService.savePedido(pedidoReqDTO), HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualiza um pedido cadastrado no sistema enquanto estiver em status de aguardando pagamento e retorna seus dados atualizados.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza o pedido que possui o id fornecido e retorna seus dados atualizados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Falha. Erro na requisição.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum pedido encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<PedidoResDTO> updatePedido(@RequestBody PedidoReqDTO pedidoReqDTO) {
		return new ResponseEntity<>(pedidoService.updatePedido(pedidoReqDTO), HttpStatus.OK);
	}

	@PutMapping("/{idPedido}/{idStatus}")
	@Operation(summary = "Atualiza o status de um pedido cadastrado no sistema e retorna seus dados atualizados.", parameters = {
			@Parameter(name = "idPedido", description = "Id do pedido desejado."),
			@Parameter(name = "idStatus", description = "Id do status para o qual será alterado.")}, responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza o status do pedido desejado e retorna seus dados atualizados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Falha. Erro na requisição.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum pedido encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<PedidoResDTO> updatePedidoStatus(@PathVariable Integer idPedido,
			@PathVariable Integer idStatus) {
		return new ResponseEntity<>(pedidoService.updatePedidoStatus(idPedido, idStatus), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um pedido cadastrado no sistema através do seu ID.", parameters = {
			@Parameter(name = "id", description = "ID do pedido desejado.") }, responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Exclui o pedido que possui o ID fornecido.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um pedido cadastrado no sistema que possua o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<String> deletePedidoById(@PathVariable Integer id) {
		pedidoService.deletePedidoById(id);
		return new ResponseEntity<>("O Pedido de id = " + id + " foi excluído com sucesso.", HttpStatus.OK);
	}

}

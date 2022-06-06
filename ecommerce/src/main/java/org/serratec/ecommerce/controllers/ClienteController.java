package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.dtos.ClienteDTO;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.services.ClienteService;
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
@RequestMapping("/cliente")
@Tag(name = "Cliente")
public class ClienteController {
	@Autowired
	ClienteService clienteService;

	@GetMapping
	@Operation(summary = "Busca todos os clientes cadastrados no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os clientes cadastrados no sistema.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClienteDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum cliente encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<List<ClienteDTO>> findAllCliente() {
		return new ResponseEntity<>(clienteService.findAllCliente(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um cliente cadastrado no sistema através do seu id.", parameters = {
			@Parameter(name = "id", description = "Id do cliente desejado.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna o cliente desejado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Nenhum cliente encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<ClienteDTO> findClienteByIdDTO(@PathVariable Integer id) {
		return new ResponseEntity<>(clienteService.findClienteByIdDTO(id), HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Cadastra um novo cliente no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra um novo cliente no sistema e retorna seus dados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
			@ApiResponse(responseCode = "400", description = "Falha. Erro na requisição.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<ClienteDTO> saveCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
		return new ResponseEntity<>(clienteService.saveCliente(clienteDTO), HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualiza um cliente cadastrado.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza o cliente desejado e retorna seus dados atualizados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
			@ApiResponse(responseCode = "400", description = "Falha. Erro na requisição.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum cliente encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<ClienteDTO> updateCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
		return new ResponseEntity<>(clienteService.updateCliente(clienteDTO), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um cliente cadastrado através do seu ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Exclui o cliente desejado.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum cliente encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<String> deleteClienteById(@PathVariable Integer id) {
		clienteService.deleteClienteById(id);
		return new ResponseEntity<>("O Cliente de id = " + id + " foi excluído com sucesso.", HttpStatus.OK);
	}

}

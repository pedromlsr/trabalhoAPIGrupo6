package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.dtos.ClienteDTO;
import org.serratec.ecommerce.exceptions.EnderecoException;
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
	@Operation(summary = "Busca todos os clientes cadastrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os clientes cadastrados.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClienteDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum cliente encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<List<ClienteDTO>> findAllCliente() {
		return new ResponseEntity<>(clienteService.findAllCliente(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um cliente cadastrado através do seu ID.", parameters = {
			@Parameter(name = "id", description = "Id da categoria desejada.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna a categoria desejada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Não há um cliente cadastrado com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<ClienteDTO> findClienteByIdDTO(@PathVariable Integer id) {
		return new ResponseEntity<>(clienteService.findClienteByIdDTO(id), HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma nova categoria.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra um novo cliente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<ClienteDTO> saveCliente(@Valid @RequestBody ClienteDTO clienteDTO) throws EnderecoException {
		return new ResponseEntity<>(clienteService.saveCliente(clienteDTO), HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualiza um cliente cadastrado.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza o cliente desejado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um cliente com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<ClienteDTO> updateCliente(@Valid @RequestBody ClienteDTO clienteDTO)
			throws EnderecoException {
		return new ResponseEntity<>(clienteService.updateCliente(clienteDTO), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um cliente cadastrado através do seu ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Exclui o cliente desejado.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um cliente com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<String> deleteClienteById(@PathVariable Integer id) {
		clienteService.deleteClienteById(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}

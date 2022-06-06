package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.EnderecoDTO;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.services.EnderecoService;
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
@RequestMapping("/endereco")
@Tag(name = "Endereço")
public class EnderecoController {
	@Autowired
	EnderecoService enderecoService;

	@GetMapping
	@Operation(summary = "Busca todos os enderecos cadastrados no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os endereços cadastrados no sistema.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EnderecoDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum endereço encontrado no sistema.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<List<EnderecoDTO>> findAllEndereco() {
		return new ResponseEntity<>(enderecoService.findAllEndereco(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um produto endereco no sistema através do seu id.", parameters = {
			@Parameter(name = "id", description = "Id do endereço desejado.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna o endereço que possui o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoDTO.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Nenhum endereço encontrado no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<EnderecoDTO> findEnderecoById(@PathVariable Integer id) {
		return new ResponseEntity<>(enderecoService.findEnderecoByIdDTO(id), HttpStatus.OK);
	}

}
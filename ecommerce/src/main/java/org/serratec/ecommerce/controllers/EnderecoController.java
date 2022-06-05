package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.EnderecoDTO;
import org.serratec.ecommerce.exceptions.EnderecoException;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.services.EnderecoService;
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
import org.springframework.web.bind.annotation.RequestParam;
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
	@Operation(summary = "Busca todos os endereços cadastrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os endereços cadastrados.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EnderecoDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum endereço encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<List<EnderecoDTO>> findAllEndereco() {
		return new ResponseEntity<>(enderecoService.findAllEndereco(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um endereço cadastrado através do seu ID.", parameters = {
			@Parameter(name = "id", description = "Id do endereço desejado.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna o endereço desejado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoDTO.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Não há um endereço cadastrado com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<EnderecoDTO> findEnderecoById(@PathVariable Integer id) {
		return new ResponseEntity<>(enderecoService.findEnderecoByIdDTO(id), HttpStatus.OK);
	}
	
	//Endpoints comentados para testar a necessidade deles aqui ou apenas através do ClienteController
	
	/*@PostMapping
	@Operation(summary = "Cadastra um novo endereço.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra um novo endereço.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoDTO.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<EnderecoDTO> saveEndereco(@RequestParam Integer idCliente, @RequestParam String cep,
			@RequestParam String numero, @RequestParam String complemento) throws EnderecoException {
		return new ResponseEntity<>(enderecoService.saveEnderecoDTO(idCliente, cep, numero, complemento),
				HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualiza um endereço cadastrado.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza o endereço desejado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoDTO.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um endereço com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<EnderecoDTO> updateEndereco(@RequestParam Integer idEndereco,
			@RequestBody EnderecoDTO enderecoDTO) throws EnderecoException {
		return new ResponseEntity<>(enderecoService.updateEnderecoDTO(idEndereco, enderecoDTO), HttpStatus.OK);
	}

	@DeleteMapping
	@Operation(summary = "Exclui um endereço cadastrado.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Exclui o endereço desejado.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um endereço com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<String> deleteEnderecoById(@PathVariable Integer id) throws Exception {
		enderecoService.deleteByIdEndereco(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}*/

}
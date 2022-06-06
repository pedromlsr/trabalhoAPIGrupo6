package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.dtos.ProdutoGetDTO;
import org.serratec.ecommerce.dtos.ProdutoPostDTO;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/produto")
@Tag(name = "Produto")
public class ProdutoController {

	@Autowired
	ProdutoService produtoService;

	@GetMapping
	@Operation(summary = "Busca todos os produtos cadastrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todos os produtos cadastrados.", content = 	@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProdutoGetDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhum produto encontrado.", content = 	@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<List<ProdutoGetDTO>> findAllProduto() {
		return new ResponseEntity<>(produtoService.findAllProduto(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um produto cadastrado através do seu ID.", parameters = {
		@Parameter(name = "id", description = "Id do produto desejado.") }, responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna o produto desejado.", content = 	@Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoGetDTO.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um produto cadastrado com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<ProdutoGetDTO> findProdutoByIdDTO(@PathVariable Integer id) {
		return new ResponseEntity<>(produtoService.findProdutoByIdDTO(id), HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Cadastra um novo produto.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra um novo produto.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoGetDTO.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<ProdutoGetDTO> saveProdutoDTO(@Valid @RequestBody ProdutoPostDTO produtoDto) {
		return new ResponseEntity<>(produtoService.saveProdutoDTO(produtoDto), HttpStatus.CREATED);
	}

	@PostMapping(value = "/com-foto", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "Cadastra um novo produto com uma foto.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra um novo produto com foto.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoGetDTO.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<ProdutoGetDTO> saveProdutoDtoComFoto(@Valid @RequestPart("produto") ProdutoPostDTO produtoDto, @RequestPart("file") MultipartFile file) throws Exception {
		return new ResponseEntity<>(produtoService.saveProdutoDtoComFoto(produtoDto, file), HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualiza um produto cadastrado.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza o produto desejado.", content = 	@Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoGetDTO.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um produto com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<ProdutoGetDTO> updateProduto(@Valid @RequestBody ProdutoPostDTO produtoDto) {
		return new ResponseEntity<>(produtoService.updateProduto(produtoDto), HttpStatus.OK);
	}	

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um produto cadastrado através do seu ID.", parameters = {
	@Parameter(name = "id", description = "Id do produto desejado.") }, responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Exclui o produto desejado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoGetDTO.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Não há um produto cadastrado com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<String> deleteProdutoById(@PathVariable Integer id) {
		produtoService.deleteProdutoById(id);
		return new ResponseEntity<>("O Produto de id = " + id + " foi excluído com sucesso.",HttpStatus.OK);
	}

}

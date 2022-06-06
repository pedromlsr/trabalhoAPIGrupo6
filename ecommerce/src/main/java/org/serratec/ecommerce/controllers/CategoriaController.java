package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.entities.Categoria;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.services.CategoriaService;
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
@RequestMapping("/categoria")
@Tag(name = "Categoria")
public class CategoriaController {
	@Autowired
	CategoriaService categoriaService;

	@GetMapping
	@Operation(summary = "Busca todas as Categorias cadastradas no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todas as categorias cadastradas no sistema.", content = @Content(mediaType = "application/json",  array = @ArraySchema(schema = @Schema(implementation = Categoria.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhuma categoria encontrada no sistema.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<List<Categoria>> findAllCategoria() {
		return new ResponseEntity<>(categoriaService.findAllCategoria(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca uma categoria cadastrada através do seu id.", parameters = {
			@Parameter(name = "id", description = "Id da categoria desejada.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna a categoria desejada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Não há uma categoria cadastrada com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<Categoria> findCategoriaById(@PathVariable Integer id) {
		return new ResponseEntity<>(categoriaService.findCategoriaById(id), HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma nova categoria no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra uma nova categoria no sistema e retorna seus dados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
			@ApiResponse(responseCode = "400", description = "Falha. Erro na requisição.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<Categoria> saveCategoria(@Valid @RequestBody Categoria categoria) {
		return new ResponseEntity<>(categoriaService.saveCategoria(categoria), HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualiza uma categoria cadastrada.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza a categoria que possui o id fornecido e retorna os seus dados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
			@ApiResponse(responseCode = "400", description = "Falha. Erro na requisição.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhuma categoria encontrada no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<Categoria> updateCategoria(@Valid @RequestBody Categoria categoria) {
		return new ResponseEntity<>(categoriaService.updateCategoria(categoria), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui uma categoria cadastrada através do seu id.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Exclui a categoria que possui o id fornecido.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhuma categoria encontrada no sistema com o id fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<String> deleteCategoriaById(@PathVariable Integer id) {
		categoriaService.deleteCategoriaById(id);
		
		return new ResponseEntity<>("A categoria de id = " + id + " foi excluída com sucesso.", HttpStatus.OK);
	}

}

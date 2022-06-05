package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.entities.Categoria;
import org.serratec.ecommerce.exceptions.ErrorResponse;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
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
@Tag(name = "Categoria", description = "endpoints")
public class CategoriaController {
	@Autowired
	CategoriaService categoriaService;

	@GetMapping
	@Operation(summary = "Busca todas as categorias cadastradas.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Retorna todas as categorias cadastradas.", content = @Content(mediaType = "application/json",  array = @ArraySchema(schema = @Schema(implementation = Categoria.class)))),
			@ApiResponse(responseCode = "404", description = "Falha. Nenhuma categoria encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<List<Categoria>> findAllCategoria() {
		List<Categoria> categoriaList = categoriaService.findAllCategoria();
		if (categoriaList.isEmpty())
			throw new NoSuchElementFoundException("Não foi encontrada nenhuma categoria");
		return new ResponseEntity<>(categoriaList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca uma categoria cadastrada através do seu ID.", parameters = {
			@Parameter(name = "id", description = "Id da categoria desejada.") }, responses = {
					@ApiResponse(responseCode = "200", description = "Sucesso. Retorna a categoria desejada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
					@ApiResponse(responseCode = "404", description = "Falha. Não há uma categoria cadastrada com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<Categoria> findCategoriaById(@PathVariable Integer id) {
		Categoria categoria = categoriaService.findCategoriaById(id);
		if (categoria == null)
			throw new NoSuchElementFoundException("Não foi encontrada categoria com o Id: " + id);
		return new ResponseEntity<>(categoria, HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma nova categoria.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Cadastra uma nova categoria.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<Categoria> saveCategoria(@Valid @RequestBody Categoria categoria) {
		Categoria novaCategoria = categoriaService.saveCategoria(categoria);
		if (novaCategoria == null)
			throw new NoSuchElementFoundException(
					"Já existe uma categoria salva com o nome " + "'" + categoria.getNomeCategoria() + "' ");
		return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Atualiza uma categoria cadastrada.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Atualiza a categoria desejada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
			@ApiResponse(responseCode = "404", description = "Falha. Não há uma categoria com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<Categoria> updateCategoria(@Valid @RequestBody Categoria categoria) {
		if (categoria.getIdCategoria() == null)
			throw new NoSuchElementFoundException("Não foi informado um ID");
		Categoria categoriaAtualizado = categoriaService.updateCategoria(categoria);
		if (categoriaAtualizado == null)
			throw new NoSuchElementFoundException(
					"Não foi encontrada categoria com o Id: " + categoria.getIdCategoria());
		return new ResponseEntity<>(categoriaAtualizado, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui uma categoria cadastrada através do seu ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso. Exclui a categoria desejada.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Falha. Não há uma categoria com o ID fornecido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "Falha. Erro inesperado.", content = @Content) })
	public ResponseEntity<String> deleteCategoriaById(@PathVariable Integer id) {
		if (categoriaService.findCategoriaById(id) == null)
			throw new NoSuchElementFoundException("Não foi encontrada categoria com o Id: " + id);
		categoriaService.deleteCategoriaById(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}

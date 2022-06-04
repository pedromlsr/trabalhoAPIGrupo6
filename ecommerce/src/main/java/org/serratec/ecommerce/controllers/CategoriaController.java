package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.entities.Categoria;
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

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
	@Autowired
	CategoriaService categoriaService;

	@GetMapping
	public ResponseEntity<List<Categoria>> findAllCategoria() {
		List<Categoria> categoriaList = categoriaService.findAllCategoria();
		if (categoriaList.isEmpty())
			throw new NoSuchElementFoundException("Não foi encontrada nenhuma categoria");
		return new ResponseEntity<>(categoriaList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> findCategoriaById(@PathVariable Integer id) {
		Categoria categoria = categoriaService.findCategoriaById(id);
		if (categoria == null)
			throw new NoSuchElementFoundException("Não foi encontrada categoria com o Id: " + id);
		return new ResponseEntity<>(categoria, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Categoria> saveCategoria(@Valid @RequestBody Categoria categoria) {
		Categoria novaCategoria = categoriaService.saveCategoria(categoria);
		if (novaCategoria == null)
			throw new NoSuchElementFoundException(
					"Já existe uma categoria salva com o nome " + "'" + categoria.getNomeCategoria() + "' ");
		return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
	}
 
	@PutMapping
	public ResponseEntity<Categoria> updateCategoria(@Valid @RequestBody Categoria categoria) {
		if(categoria.getIdCategoria()==null) 
			throw new NoSuchElementFoundException(
					"Não foi informado um ID");		
		Categoria categoriaAtualizado = categoriaService.updateCategoria(categoria);
		if(categoriaAtualizado==null)
			throw new NoSuchElementFoundException("Não foi encontrada categoria com o Id: " + categoria.getIdCategoria());
		return new ResponseEntity<>(categoriaAtualizado, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategoriaById(@PathVariable Integer id) {
		if(categoriaService.findCategoriaById(id)==null) 
			throw new NoSuchElementFoundException("Não foi encontrada categoria com o Id: " + id);
		categoriaService.deleteCategoriaById(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}

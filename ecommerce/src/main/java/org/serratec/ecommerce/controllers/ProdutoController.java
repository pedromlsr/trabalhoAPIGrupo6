package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.entities.Produto;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
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

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	@Autowired
	ProdutoService produtoService;  

	@GetMapping
	public ResponseEntity<List<Produto>> findAllProduto() {
		List<Produto> produtoList = produtoService.findAllProduto();
		return new ResponseEntity<>(produtoList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> findProdutoById(@PathVariable Integer id) {
		Produto produto = produtoService.findProdutoById(id);
		if(produto==null) {
			throw new NoSuchElementFoundException("Não foi encontrado um produto para o id: " + id);
		}
		return new ResponseEntity<>(produto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Produto> saveProduto(@Valid @RequestBody Produto produto) {
		Produto produtoNovo = produtoService.findProdutoByDescricao(produto.getDescricaoProduto());
		if(produtoNovo!=null) {
			// Precisa trocar o tipo de Exception
			throw new NoSuchElementFoundException("O produto de id: " + produtoNovo.getIdProduto() + " já possui essa descrição");
		}
		return new ResponseEntity<>(produtoService.saveProduto(produto), HttpStatus.CREATED);
		
	}
	
	@PostMapping("/com-foto")
	public ResponseEntity<Produto> saveProdutoComFoto(@Valid @RequestBody Produto produto) {
		return new ResponseEntity<>(produtoService.saveProduto(produto), HttpStatus.CREATED);
	}

	@PostMapping(value = "/com-foto", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Produto> saveProdutoComFoto(@RequestPart("produto") String produto,
			@RequestPart("file") MultipartFile file) throws Exception {

		Produto novoProduto = produtoService.convertStringToProduct(produto);
		Produto produtoBD = produtoService.findProdutoByDescricao(novoProduto.getDescricaoProduto());
		
		if(produtoBD!= null) {
			// Precisa trocar o tipo de Exception
						throw new NoSuchElementFoundException("O produto de id: " + produtoBD.getIdProduto() + " já possui essa descrição");
		}		
		return new ResponseEntity<>(produtoService.saveProdutoComFoto(novoProduto, file), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> updateProdutoById(@PathVariable Integer id,@RequestBody Produto produto) {
		if(id==null) {
			// Precisa trocar o tipo de Exception
			throw new NoSuchElementFoundException("Não foi informado um ID");
		}		
		Produto produtoAtualizado = produtoService.updateProdutoById(produtoService.findProdutoById(id), produto);
		return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProdutoById(@PathVariable Integer id) {
		Produto produto = produtoService.findProdutoById(id);
		if(produto==null) {
			throw new NoSuchElementFoundException("Não foi encontrado um produto para o id: " + id);
		}
		produtoService.deleteProdutoById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}

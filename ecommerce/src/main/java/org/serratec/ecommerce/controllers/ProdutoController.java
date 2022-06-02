package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.dtos.ProdutoGetDTO;
import org.serratec.ecommerce.dtos.ProdutoPostDTO;
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
	public ResponseEntity<List<ProdutoGetDTO>> findAllProduto() {
		List<ProdutoGetDTO> produtoList = produtoService.findAllProduto();
		if(produtoList.isEmpty()) {
			throw new NoSuchElementFoundException("Não foi encontrado nenhum produto");
		}
		return new ResponseEntity<>(produtoList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdutoGetDTO> findProdutoById(@PathVariable Integer id) {
		ProdutoGetDTO produtoDto = produtoService.findProdutoById(id);
		if (produtoDto == null) {
			throw new NoSuchElementFoundException("Não foi encontrado um produto para o id: " + id);
		}
		return new ResponseEntity<>(produtoDto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProdutoGetDTO> saveProdutoDTO(@Valid @RequestBody ProdutoPostDTO produtoDto) {
		ProdutoGetDTO produtoNovo = produtoService.findProdutoByDescricaoDto(produtoDto.getDescricaoProduto());
		if (produtoNovo != null) {
			// Precisa trocar o tipo de Exception
			throw new NoSuchElementFoundException(
					"O produto de id: " + produtoNovo.getIdProduto() + " já possui essa descrição");
		}
		return new ResponseEntity<>(produtoService.saveProdutoDTO(produtoDto), HttpStatus.CREATED);

	}
	
	@PostMapping(value = "/com-foto", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ProdutoGetDTO> saveProdutoDtoComFoto(@RequestPart("produto") String produto,
			@RequestPart("file") MultipartFile file) throws Exception {
		
		ProdutoPostDTO produtoDto = produtoService.convertStringToDto(produto);
		ProdutoGetDTO produtoBD = produtoService.findProdutoByDescricaoDto(produtoDto.getDescricaoProduto());
		
		if (produtoBD != null) {
			// Precisa trocar o tipo de Exception
			throw new NoSuchElementFoundException(
					"O produto de id: " + produtoBD.getIdProduto() + " já possui essa descrição");
		}
		return new ResponseEntity<>(produtoService.saveProdutoDtoComFoto(produtoDto, file), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<ProdutoGetDTO> updateProduto(@Valid @RequestBody ProdutoPostDTO produtoDto) {
		if (produtoDto.getIdProduto() == null) {
			// Precisa trocar o tipo de Exception
			throw new NoSuchElementFoundException("Não foi informado um ID");
		}
		ProdutoGetDTO produtoAtualizado = produtoService.updateProduto(produtoDto);
		return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoGetDTO> updateProdutoById(@PathVariable Integer id, @Valid @RequestBody ProdutoPostDTO produtoDto) {
		if (id == null) {
			// Precisa trocar o tipo de Exception
			throw new NoSuchElementFoundException("Não foi informado um ID");
		}
		ProdutoGetDTO produtoAtualizado = produtoService.updateProdutoById(produtoDto,id);
		return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProdutoById(@PathVariable Integer id) {
		ProdutoGetDTO produtoDto = produtoService.findProdutoById(id);
		if (produtoDto == null) {
			throw new NoSuchElementFoundException("Não foi encontrado um produto para o id: " + id);
		}
		produtoService.deleteProdutoById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}

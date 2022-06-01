package org.serratec.ecommerce.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.serratec.ecommerce.dtos.ProdutoDTO;
import org.serratec.ecommerce.entities.Produto;
import org.serratec.ecommerce.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProdutoService {
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired 
	ArquivoToService arquivoToService;

	public List<Produto> findAllProduto() {
		return produtoRepository.findAll();
	}

	public Produto findProdutoById(Integer id) {
		return produtoRepository.findById(id).isPresent() ? produtoRepository.findById(id).get() : null;
	}
	
	public Produto findProdutoByDescricao(String descricaoProduto) {
		return produtoRepository.findByDescricaoProdutoIgnoreCase(descricaoProduto);
	}

	public Produto saveProduto(Produto produto) {
		return produtoRepository.save(produto);
	}
	
	public Produto convertStringToProduct(String produtoString) {
		
		Produto produtoConvertido = new Produto();
		try {
			ObjectMapper objMapper = new ObjectMapper();
			produtoConvertido = objMapper.readValue(produtoString, Produto.class);
			
		} catch (IOException e) {
			System.out.println("Ocorreu um erro na conversão");
		}
		
		return produtoConvertido;
	}
	
	public Produto saveProdutoComFoto(Produto produto, MultipartFile file) throws Exception {
				
		Produto produtoBD = produtoRepository.save(produto);
		produtoBD.setNomeImagemProduto(produtoBD.getIdProduto() + "_" + file.getOriginalFilename());
		produtoBD.setDataCadastro(LocalDate.now());
		Produto produtoAtualizado = produtoRepository.save(produtoBD);
		
		arquivoToService.criarArquivo(produtoBD.getIdProduto() + "_" + file.getOriginalFilename(), file);
		
		
		return produtoAtualizado;
	}

	public Produto updateProduto(Produto produto) {
		produto.setDataCadastro(LocalDate.now());
		return produtoRepository.save(produto);
	}

	//Pode atualizar passando somente um dos campos
	
	public Produto updateProdutoById(Produto produtoBD, Produto produto) {
		Produto produtoAtualizado = new Produto();

		produtoAtualizado.setIdProduto(produtoBD.getIdProduto());
		
		if (produto.getNomeProduto() != null) {
			produtoAtualizado.setNomeProduto(produto.getNomeProduto());
		} else {
			produtoAtualizado.setNomeProduto(produtoBD.getNomeProduto());
		}
		if (produto.getDescricaoProduto() != null) {
			produtoAtualizado.setDescricaoProduto(produto.getDescricaoProduto());
		} else {
			produtoAtualizado.setDescricaoProduto(produtoBD.getDescricaoProduto());
		}
		if (produto.getValorUnitario() != null) {
			produtoAtualizado.setValorUnitario(produto.getValorUnitario());
		} else {
			produtoAtualizado.setValorUnitario(produtoBD.getValorUnitario());
		}
		if (produto.getCategoria()!=null) {
			produtoAtualizado.setCategoria(produto.getCategoria());
		} else {
			produtoAtualizado.setCategoria(produtoBD.getCategoria());
		}
		if (produto.getQtdEstoque() != null) {
			produtoAtualizado.setQtdEstoque(produto.getQtdEstoque());
		} else {
			produtoAtualizado.setQtdEstoque(produtoBD.getQtdEstoque());
		}
		produtoAtualizado.setDataCadastro(LocalDate.now());
		produtoAtualizado.setNomeImagemProduto(produto.getNomeImagemProduto());

		return produtoRepository.save(produtoAtualizado);
	}

	public void deleteProdutoById(Integer id) {
		produtoRepository.deleteById(id);
	}
	
	public Produto convertDtoToEntity(ProdutoDTO produtoDto) {
		Produto produto = new Produto();
		
		produto.setIdProduto(produtoDto.getIdProduto());
		produto.setNomeProduto(produtoDto.getNomeProduto());
		produto.setDescricaoProduto(produtoDto.getDescricaoProduto());
		produto.setValorUnitario(produtoDto.getValorUnitario());
		produto.setQtdEstoque(produtoDto.getQtdEstoque());
		produto.setDataCadastro(produtoDto.getDataCadastro());
		
		return produto;
	}
	
	public ProdutoDTO convertEntityToDto(Produto produto) {
		ProdutoDTO produtoDto = new ProdutoDTO();
		
		produtoDto.setIdProduto(produto.getIdProduto());
		produtoDto.setNomeProduto(produto.getNomeProduto());
		produtoDto.setDescricaoProduto(produto.getDescricaoProduto());
		produtoDto.setValorUnitario(produto.getValorUnitario());
		produtoDto.setQtdEstoque(produto.getQtdEstoque());
		produtoDto.setDataCadastro(produto.getDataCadastro());
		
		return produtoDto;
	}
	
}

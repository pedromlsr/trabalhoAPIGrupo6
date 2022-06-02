package org.serratec.ecommerce.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.dtos.ProdutoGetDTO;
import org.serratec.ecommerce.dtos.ProdutoPostDTO;
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

	public List<ProdutoGetDTO> findAllProduto() {
		List<ProdutoGetDTO> listProdutoDto = new ArrayList<ProdutoGetDTO>();
		List<Produto> listProduto = produtoRepository.findAll();
		for (Produto produto : listProduto) {
			listProdutoDto.add(convertEntityToDto(produto));
		}
		return listProdutoDto;
	}

	public ProdutoGetDTO findProdutoById(Integer id) {
		return produtoRepository.findById(id).isPresent() ? convertEntityToDto(produtoRepository.findById(id).get())
				: null;
	}

	public ProdutoGetDTO findProdutoByDescricaoDto(String descricaoProduto) {
		Produto produto = produtoRepository.findByDescricaoProdutoIgnoreCase(descricaoProduto);
		if (produto == null) {
			return null;
		}
		return convertEntityToDto(produto);
	}

	public ProdutoGetDTO saveProdutoDTO(ProdutoPostDTO produtoDto) {

		Produto produto = convertDtoToEntity(produtoDto);
		produto.setDataCadastro(LocalDate.now());
		return convertEntityToDto(produtoRepository.save(produto));
	}

	public ProdutoPostDTO convertStringToDto(String produtoString) {

		ProdutoPostDTO produtoDtoConvertido = new ProdutoPostDTO();
		try {
			ObjectMapper objMapper = new ObjectMapper();
			produtoDtoConvertido = objMapper.readValue(produtoString, ProdutoPostDTO.class);

		} catch (IOException e) {
			System.out.println("Ocorreu um erro na conversão");
		}

		return produtoDtoConvertido;
	}

	public ProdutoGetDTO saveProdutoDtoComFoto(ProdutoPostDTO produtoDto, MultipartFile file) throws Exception {

		Produto produtoBD = convertDtoToEntity(produtoDto);
		produtoRepository.save(produtoBD);
		produtoBD.setNomeImagemProduto(produtoBD.getIdProduto() + "_" + file.getOriginalFilename());
		produtoBD.setDataCadastro(LocalDate.now());
		Produto produtoAtualizado = produtoRepository.save(produtoBD);

		arquivoToService.criarArquivo(produtoBD.getIdProduto() + "_" + file.getOriginalFilename(), file);
		ProdutoGetDTO produtoGetDto = convertEntityToDto(produtoAtualizado);

		return produtoGetDto;
	}

	public ProdutoGetDTO updateProduto(ProdutoPostDTO produtoDto) {
		ProdutoGetDTO produtoBD = findProdutoByDescricaoDto(produtoDto.getDescricaoProduto());
		if (produtoBD != null && produtoBD.getIdProduto()!=produtoDto.getIdProduto()) {
			return null;
		}
		Produto produto = convertDtoToEntity(produtoDto);
		produtoRepository.save(produto);
		return convertEntityToDto(produto);

	}

	public ProdutoGetDTO updateProdutoById(ProdutoPostDTO produtoDto, Integer id) {
		
		ProdutoGetDTO produtoBD = findProdutoByDescricaoDto(produtoDto.getDescricaoProduto());
		if (produtoBD != null && produtoBD.getIdProduto()!=id) {
			return null;
		}
		
		Produto produtoAtualizado = new Produto();
		produtoBD = findProdutoById(id);

		produtoAtualizado.setIdProduto(id);
		produtoAtualizado.setNomeProduto(produtoDto.getNomeProduto());
		produtoAtualizado.setDescricaoProduto(produtoDto.getDescricaoProduto());
		produtoAtualizado.setValorUnitario(produtoDto.getValorUnitario());
//		produtoAtualizado.setCategoria(produtoDto.getCategoria());

		if (produtoBD != null) {
			produtoAtualizado.setDataCadastro(produtoBD.getDataCadastro());
		} else {
			produtoAtualizado.setDataCadastro(LocalDate.now());
		}
		if (produtoDto.getQtdEstoque() != null) {
			produtoAtualizado.setQtdEstoque(produtoDto.getQtdEstoque());
		} else {
			produtoAtualizado.setQtdEstoque(produtoBD.getQtdEstoque());
		}

		return convertEntityToDto(produtoRepository.save(produtoAtualizado));

	}

	public void deleteProdutoById(Integer id) {
		produtoRepository.deleteById(id);
	}

	public Produto convertDtoToEntity(ProdutoPostDTO produtoDto) {
		Produto produto = new Produto();

		produto.setIdProduto(produtoDto.getIdProduto());
		produto.setNomeProduto(produtoDto.getNomeProduto());
		produto.setDescricaoProduto(produtoDto.getDescricaoProduto());
		produto.setValorUnitario(produtoDto.getValorUnitario());
		produto.setQtdEstoque(produtoDto.getQtdEstoque());

		return produto;
	}

	public ProdutoGetDTO convertEntityToDto(Produto produto) {
		ProdutoGetDTO produtoDto = new ProdutoGetDTO();

		produtoDto.setIdProduto(produto.getIdProduto());
		produtoDto.setNomeProduto(produto.getNomeProduto());
		produtoDto.setDescricaoProduto(produto.getDescricaoProduto());
		produtoDto.setValorUnitario(produto.getValorUnitario());
		produtoDto.setQtdEstoque(produto.getQtdEstoque());
		produtoDto.setDataCadastro(produto.getDataCadastro());

		return produtoDto;
	}

}
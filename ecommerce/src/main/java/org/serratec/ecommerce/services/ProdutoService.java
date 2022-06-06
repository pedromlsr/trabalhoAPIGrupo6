package org.serratec.ecommerce.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.dtos.ProdutoGetDTO;
import org.serratec.ecommerce.dtos.ProdutoPostDTO;
import org.serratec.ecommerce.entities.Categoria;
import org.serratec.ecommerce.entities.Produto;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.exceptions.ProdutoException;
import org.serratec.ecommerce.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	ArquivoToService arquivoToService;

	@Autowired
	CategoriaService categoriaService;

	public List<ProdutoGetDTO> findAllProduto() {
		List<ProdutoGetDTO> listProdutoDto = new ArrayList<ProdutoGetDTO>();

		List<Produto> listProduto = produtoRepository.findAll();
		for (Produto produto : listProduto) {
			listProdutoDto.add(convertEntityToDto(produto));
		}
		if (listProduto.isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum produto encontrado");
		}

		return listProdutoDto;
	}

	public Produto findProdutoById(Integer id) {
		if (!produtoRepository.findById(id).isPresent()) {
			throw new NoSuchElementFoundException("O produto de id =" + id + " não foi encontrado");
		}
		return produtoRepository.findById(id).get();
	}

	public ProdutoGetDTO findProdutoByIdDTO(Integer id) {
		if (!produtoRepository.findById(id).isPresent()) {
			throw new NoSuchElementFoundException("O produto de id = " + id + " não foi encontrado");
		}

		return convertEntityToDto(produtoRepository.findById(id).get());
	}	

	public ProdutoGetDTO saveProdutoDTO(ProdutoPostDTO produtoDto) {

		findProdutoByDescricaoDto(produtoDto);
		verificaCategoria(produtoDto);

		Produto produto = convertDtoToEntity(produtoDto);
		produto.setDataCadastro(LocalDate.now());

		return convertEntityToDto(produtoRepository.save(produto));
	}

	public ProdutoGetDTO saveProdutoDtoComFoto(ProdutoPostDTO produtoDto, MultipartFile file) throws Exception {

		findProdutoByDescricaoDto(produtoDto);
		verificaCategoria(produtoDto);
				
		
		Produto produtoBD = convertDtoToEntity(produtoDto);
		produtoRepository.save(produtoBD);
		produtoBD.setNomeImagemProduto(produtoBD.getIdProduto() + "_" + file.getOriginalFilename());
		produtoBD.setDataCadastro(LocalDate.now());

		Produto produtoAtualizado = produtoRepository.save(produtoBD);

		arquivoToService.criarArquivo(produtoBD.getIdProduto() + "_" + file.getOriginalFilename(), file);

		return convertEntityToDto(produtoAtualizado);
	}

	public ProdutoGetDTO updateProduto(ProdutoPostDTO produtoDto) {
		
		if (produtoDto.getIdProduto() == null) {
			throw new ProdutoException("Não foi informado um id para o produto");
		}
		
		Produto produtoBD= convertDtoToEntity(findProdutoByDescricaoDto(produtoDto));
		verificaCategoria(produtoDto);				
		
		Produto produtoAtualizado = convertDtoToEntity(produtoDto);
		produtoBD = findProdutoById(produtoDto.getIdProduto());
		produtoAtualizado.setDataCadastro(produtoBD.getDataCadastro());

		return convertEntityToDto(produtoRepository.save(produtoAtualizado));
	}

	public void deleteProdutoById(Integer id) {
		if (findProdutoByIdDTO(id) == null) {
			throw new NoSuchElementFoundException("O produto de id = " + id + " não foi encontrado");
		}
		produtoRepository.deleteById(id);
	}

	public Produto convertDtoToEntity(ProdutoPostDTO produtoDto) {
		Produto produto = new Produto();

		produto.setIdProduto(produtoDto.getIdProduto());
		produto.setNomeProduto(produtoDto.getNomeProduto());
		produto.setDescricaoProduto(produtoDto.getDescricaoProduto());
		produto.setValorUnitario(produtoDto.getValorUnitario());
		produto.setQtdEstoque(produtoDto.getQtdEstoque());
		produto.setCategoria(categoriaService.findCategoriaById(produtoDto.getIdCategoria()));

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
		if (produto.getCategoria() != null)
			produtoDto.setNomeCategoria(produto.getCategoria().getNomeCategoria());

		return produtoDto;
	}
	
	public ProdutoPostDTO findProdutoByDescricaoDto(ProdutoPostDTO produtoDto) {

		Produto produtoBD = produtoRepository.findByDescricaoProdutoIgnoreCase(produtoDto.getDescricaoProduto());
		if (produtoBD != null && produtoBD.getIdProduto() != produtoDto.getIdProduto()) {
			throw new ProdutoException(
					"O produto de id = " + produtoBD.getIdProduto() + " já possui essa descrição");
		}

		return produtoDto;
	}

	public void verificaCategoria(ProdutoPostDTO produtoDto) {
		Categoria categoria = categoriaService.findCategoriaById(produtoDto.getIdCategoria());
		if (categoria == null) {
			throw new NoSuchElementFoundException(
					"A categoria de id = " + produtoDto.getIdCategoria() + "não foi encontrada" );
		}

	}

}
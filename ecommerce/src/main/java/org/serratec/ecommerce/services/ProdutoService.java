package org.serratec.ecommerce.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.dtos.ProdutoGetDTO;
import org.serratec.ecommerce.dtos.ProdutoPostDTO;
import org.serratec.ecommerce.entities.Categoria;
import org.serratec.ecommerce.entities.Produto;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
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
		return listProdutoDto;
	}

	public Produto findProdutoById(Integer id) {
		return produtoRepository.findById(id).isPresent() ? produtoRepository.findById(id).get() : null;
	}

	public ProdutoGetDTO findProdutoByIdDTO(Integer id) {
		return produtoRepository.findById(id).isPresent() ? convertEntityToDto(produtoRepository.findById(id).get())
				: null;
	}

	public ProdutoGetDTO findProdutoByDescricaoDto(ProdutoPostDTO produtoDto) {

		Produto produto = produtoRepository.findByDescricaoProdutoIgnoreCase(produtoDto.getDescricaoProduto());
		if (produto == null) {
			return null;
		}

		return convertEntityToDto(produto);
	}

	public ProdutoGetDTO saveProdutoDTO(ProdutoPostDTO produtoDto) {
		verificaCategoria(produtoDto);
		Produto produto = convertDtoToEntity(produtoDto);
		produto.setDataCadastro(LocalDate.now());
		return convertEntityToDto(produtoRepository.save(produto));
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
		verificaCategoria(produtoDto);
		Produto produtoBD = produtoRepository.findByDescricaoProdutoIgnoreCase(produtoDto.getDescricaoProduto());
		if (produtoBD != null && produtoBD.getIdProduto() != produtoDto.getIdProduto()) {
			return null;
		}
		produtoBD = findProdutoById(produtoDto.getIdProduto());
		Produto produtoAtualizado = convertDtoToEntity(produtoDto);
		produtoAtualizado.setDataCadastro(produtoBD.getDataCadastro());

		return convertEntityToDto(produtoRepository.save(produtoAtualizado));
	}

	public ProdutoGetDTO updateProdutoById(ProdutoPostDTO produtoDto, Integer id) {

		produtoDto.setIdProduto(id);

		return updateProduto(produtoDto);

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

	public void verificaCategoria(ProdutoPostDTO produtoDto) {
		Categoria categoria = categoriaService.findCategoriaById(produtoDto.getIdCategoria());
		if (categoria == null) {
			throw new NoSuchElementFoundException(
					"Não foi encontrada uma categoria para o Id: " + produtoDto.getIdCategoria());
		}

	}

}
package org.serratec.ecommerce.services;

import java.util.List;

import org.serratec.ecommerce.entities.Categoria;
import org.serratec.ecommerce.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.serratec.ecommerce.dtos.CategoriaDTO;


@Service
public class CategoriaService {
	@Autowired
	CategoriaRepository categoriaRepository;

	public List<Categoria> findAllCategoria() {
		return categoriaRepository.findAll();
	}

	public Categoria findCategoriaById(Integer id) {
		return categoriaRepository.findById(id).isPresent() ? categoriaRepository.findById(id).get() : null;
	}

	public CategoriaDTO findCategoriaDTOById(Integer id) {
		Categoria categoria = categoriaRepository.findById(id).isPresent() ? categoriaRepository.findById(id).get()
				: null;

		CategoriaDTO categoriaDTO = new CategoriaDTO();
		if (null != categoria) {
			categoriaDTO = converterEntidadeParaDto(categoria);
		}
		return categoriaDTO;
	}

	public Categoria saveCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public CategoriaDTO saveCategoriaDTO(CategoriaDTO categoriaDTO) {

		Categoria categoria = new Categoria();

		categoria.setIdCategoria(categoriaDTO.getIdCategoria());
		Categoria novaCategoria = categoriaRepository.save(categoria);

		return converterEntidadeParaDto(novaCategoria);
	}
	
	public Categoria updateCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public void deleteCategoria(Integer id) {
		Categoria inst = categoriaRepository.findById(id).get();
		categoriaRepository.delete(inst);
	}

	public void deleteCategoria(Categoria categoria) {
		categoriaRepository.delete(categoria);
	}

	private CategoriaDTO converterEntidadeParaDto(Categoria categoria) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();

		categoriaDTO.setIdCategoria(categoria.getIdCategoria());
		categoriaDTO.setNomeCategoria(categoria.getNomeCategoria());

		return categoriaDTO;
	}
}


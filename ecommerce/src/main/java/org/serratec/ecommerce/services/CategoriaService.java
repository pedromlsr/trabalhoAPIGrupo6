package org.serratec.ecommerce.services;

import java.util.List;

import org.serratec.ecommerce.entities.Categoria;
import org.serratec.ecommerce.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Categoria saveCategoria(Categoria categoria) {
		if (categoriaRepository.existsByNomeCategoria(categoria.getNomeCategoria())) {
			return null;
		}
		return categoriaRepository.save(categoria);
	}

	public Categoria updateCategoria(Categoria categoria) {
		return categoriaRepository.findById(categoria.getIdCategoria()).isPresent()
				? categoriaRepository.findById(categoria.getIdCategoria()).get()
				: null;
	}

	public void deleteCategoriaById(Integer id) {
		categoriaRepository.deleteById(id);
	}

}

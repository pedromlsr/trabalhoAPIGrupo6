package org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.entities.Categoria;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
	@Autowired
	CategoriaRepository categoriaRepository;

	public List<Categoria> findAllCategoria() {
		List<Categoria> categoriaList = new ArrayList<Categoria>();
		categoriaList = categoriaRepository.findAll();
		if (categoriaList.isEmpty()) {
			throw new NoSuchElementFoundException("Não foi encontrada nenhuma categoria");
		}

		return categoriaList;
	}

	public Categoria findCategoriaById(Integer id) {
		if (!categoriaRepository.findById(id).isPresent()) {
			throw new NoSuchElementFoundException("Não foi encontrada categoria com o Id: " + id);
		}

		return categoriaRepository.findById(id).get();
	}

	public Categoria findCategoriaByNome(Categoria categoria) {

		return categoriaRepository.findByNomeCategoria(categoria.getNomeCategoria());
	}

	public Categoria saveCategoria(Categoria categoria) {

		if (findCategoriaByNome(categoria) != null)
			throw new NoSuchElementFoundException("Já possui uma categoria cadastrada com essa descrição");

		return categoriaRepository.save(categoria);
	}

	public Categoria updateCategoria(Categoria categoria) {

		if (categoria.getIdCategoria() == null) {
			throw new NoSuchElementFoundException("Não foi informado um ID");
		}

		Categoria categoriaAtualizada = findCategoriaById(categoria.getIdCategoria());
		if (categoriaAtualizada == null) {
			throw new NoSuchElementFoundException(
					"Não foi encontrada categoria com o Id: " + categoria.getIdCategoria());
		}

		Categoria categoriaBD = findCategoriaByNome(categoria);

		if (categoriaBD != null) {

			if (categoriaAtualizada.getIdCategoria() != categoriaBD.getIdCategoria()) {
				throw new NoSuchElementFoundException(
						"A categoria de ID: " + categoriaBD.getIdCategoria() + " já possui essa descrição");
			}
		}
		return categoriaRepository.save(categoria);
	}

	public void deleteCategoriaById(Integer id) {
		Categoria categoriaAtualizada = findCategoriaById(id);
		if (categoriaAtualizada == null)
			throw new NoSuchElementFoundException("Não foi encontrada categoria com o Id: " + id);

		categoriaRepository.deleteById(id);
	}

}

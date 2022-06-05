package org.serratec.ecommerce.repositories;

import org.serratec.ecommerce.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

	Categoria findByNomeCategoria(String nome);
}

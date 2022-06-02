package org.serratec.ecommerce.repositories;

import org.serratec.ecommerce.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	Produto findByDescricaoProdutoIgnoreCase(String descricaoProduto);
	
}

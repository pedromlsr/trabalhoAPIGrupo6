package org.serratec.ecommerce.repositories;

import org.serratec.ecommerce.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	Cliente findByCpf(String cpf);
	Cliente findByEmail(String email);
}

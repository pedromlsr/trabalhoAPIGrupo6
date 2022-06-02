package org.serratec.ecommerce.repositories;

import org.serratec.ecommerce.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	Boolean existsByCpf(String cpf);
	Boolean existsByEmail(String email);
}

package org.serratec.ecommerce.repositories;

import java.util.List;

import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	
	List<Pedido> findByCliente(Cliente cliente);
}

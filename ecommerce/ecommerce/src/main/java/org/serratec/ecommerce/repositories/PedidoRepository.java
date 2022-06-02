package org.serratec.ecommerce.repositories;

import org.serratec.ecommerce.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}

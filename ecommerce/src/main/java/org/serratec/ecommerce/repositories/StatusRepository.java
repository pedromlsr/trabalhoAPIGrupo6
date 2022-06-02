package org.serratec.ecommerce.repositories;

import org.serratec.ecommerce.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {

}

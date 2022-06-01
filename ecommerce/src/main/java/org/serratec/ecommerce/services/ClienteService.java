package org.serratec.ecommerce.services;

import java.util.List;

import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	@Autowired
	ClienteRepository clienteRepository;

	public List<Cliente> findAllCliente() {
		return clienteRepository.findAll();
	}

	public Cliente findClienteById(Integer idCliente) {
		return clienteRepository.findById(idCliente).isPresent() ? clienteRepository.findById(idCliente).get() : null;
	}

	public Cliente saveCliente(Cliente cliente) {
		if (clienteRepository.existsByCpf(cliente.getCpf()) == true) {
			cliente.setCpf(null);
			return cliente;
		}
		if (clienteRepository.existsByEmail(cliente.getEmail()) == true) {
			cliente.setEmail(null);
			return cliente;
		}
		return clienteRepository.save(cliente);
	}

	public Cliente updateCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	public void deleteClienteById(Integer idCliente) {
		clienteRepository.deleteById(idCliente);
	}

}

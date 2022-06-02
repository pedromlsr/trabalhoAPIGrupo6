package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
	@Autowired
	ClienteService clienteService;
	
	@GetMapping
	public ResponseEntity<List<Cliente>> findAllCliente() {
		List<Cliente> clienteList = clienteService.findAllCliente();
		return new ResponseEntity<>(clienteList, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> findClienteById(@PathVariable Integer id) {
		Cliente cliente = clienteService.findClienteById(id);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente) {
		return new ResponseEntity<>(clienteService.saveCliente(cliente), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Cliente> updateCliente(@RequestBody Cliente cliente) {
		Cliente clienteAtualizado = clienteService.updateCliente(cliente);
		return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteClienteById(@PathVariable Integer id) {
		clienteService.deleteClienteById(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}


package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.dtos.ClienteDTO;
import org.serratec.ecommerce.exceptions.CpfException;
import org.serratec.ecommerce.exceptions.EmailException;
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
	public ResponseEntity<List<ClienteDTO>> findAllCliente() {
		return new ResponseEntity<>(clienteService.findAllCliente(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> findClienteById(@PathVariable Integer id) {
		return new ResponseEntity<>(clienteService.findClienteById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ClienteDTO> saveCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
		if (clienteService.saveCliente(clienteDTO).getCpf() == null) {
			throw new CpfException("CPF já existente.");
		}
		if (clienteService.saveCliente(clienteDTO).getEmail() == null) {
			throw new EmailException("Email já existente.");
		}
		return new ResponseEntity<>(clienteService.saveCliente(clienteDTO), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<ClienteDTO> updateCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
		return new ResponseEntity<>(clienteService.updateCliente(clienteDTO), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteClienteById(@PathVariable Integer id) {
		clienteService.deleteClienteById(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}

package org.serratec.ecommerce.controllers;

import java.util.List;

import javax.validation.Valid;

import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.entities.Endereco;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.services.ClienteService;
import org.serratec.ecommerce.services.EnderecoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
	@Autowired
	EnderecoService enderecoService;

	@Autowired
	ClienteService clienteService;

	@GetMapping
	public ResponseEntity<List<Endereco>> findAllEndereco() {
		List<Endereco> enderecoList = enderecoService.findAllEndereco();

		if (enderecoList.isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum endereço encontrado.");
		} else {
			return new ResponseEntity<>(enderecoList, HttpStatus.OK);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Endereco> findEnderecoById(@PathVariable Integer id) {
		Endereco endereco = enderecoService.findEnderecoById(id);

		if (endereco == null) {
			throw new NoSuchElementFoundException("Não foi possível encontrar o endereço com o id " + id);
		} else {
			return new ResponseEntity<>(endereco, HttpStatus.OK);
		}
	}

	@PostMapping
	public ResponseEntity<Endereco> saveEndereco(@Valid @RequestBody Endereco endereco) {
		return new ResponseEntity<>(enderecoService.saveEndereco(endereco), HttpStatus.CREATED);
	}

//	@PostMapping("/endereco-completo")
//	public ResponseEntity<Endereco> salvarEnderecoViaCep(@RequestParam Integer idCliente, @RequestParam String cep,
//			@RequestParam String numero, @RequestParam String complemento) {
//		Cliente cliente = clienteService.findClienteById(idCliente);
//
//		if (cliente == null) {
//			throw new NoSuchElementFoundException("Não foi possível encontrar o cliente com o id " + idCliente);
//		} else {
//			Endereco endereco = enderecoService.saveEnderecoDTO(cep, numero, complemento);
//			return new ResponseEntity<>(endereco, HttpStatus.CREATED);
//		}
//	}

	@PutMapping
	public ResponseEntity<Endereco> updateEndereco(@RequestBody Endereco endereco) {
		Endereco enderecoAtualizado = enderecoService.updateEndereco(endereco);
		return new ResponseEntity<>(enderecoAtualizado, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEnderecoById(@PathVariable Integer id) {
		Endereco endereco = enderecoService.findEnderecoById(id);
		if (endereco == null) {
			return new ResponseEntity<>("Não foi possível excluir. O Endereco de id = " + id + " não foi encontrado.",
					HttpStatus.NOT_FOUND);
		} else {
			enderecoService.deleteEnderecoById(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
	}

}

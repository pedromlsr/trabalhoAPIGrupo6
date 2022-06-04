package org.serratec.ecommerce.controllers;

import java.util.List;

import org.serratec.ecommerce.dtos.EnderecoDTO;
import org.serratec.ecommerce.exceptions.EnderecoException;
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

	@GetMapping
	public ResponseEntity<List<EnderecoDTO>> findAllEndereco() {
		return new ResponseEntity<>(enderecoService.findAllEndereco(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EnderecoDTO> findEnderecoById(@PathVariable Integer id) {
		return new ResponseEntity<>(enderecoService.findEnderecoById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<EnderecoDTO> saveEndereco(@RequestParam Integer idCliente, @RequestParam String cep,
			@RequestParam String numero, @RequestParam String complemento) throws EnderecoException {
		return new ResponseEntity<>(enderecoService.saveEnderecoDTO(idCliente, cep, numero, complemento),
				HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<EnderecoDTO> updateEndereco(@RequestParam Integer idEndereco,
			@RequestBody EnderecoDTO enderecoDTO) throws EnderecoException {
		return new ResponseEntity<>(enderecoService.updateEnderecoDTO(idEndereco, enderecoDTO), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> deleteEnderecoById(@PathVariable Integer id) throws Exception {
		enderecoService.deleteByIdEndereco(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}

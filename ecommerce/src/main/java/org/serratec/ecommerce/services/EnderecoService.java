package org.serratec.ecommerce.services;

import java.util.List;

import org.serratec.ecommerce.entities.Endereco;
import org.serratec.ecommerce.repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
	@Autowired
	EnderecoRepository enderecoRepository;

	public List<Endereco> findAllEndereco() {
		return enderecoRepository.findAll();
	}

	public Endereco findEnderecoById(Integer idEndereco) {
		return enderecoRepository.findById(idEndereco).isPresent() ? enderecoRepository.findById(idEndereco).get()
				: null;
	}

	public Endereco saveEndereco(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}

	public Endereco updateEndereco(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}

	public void deleteEnderecoById(Integer idEndereco) {
		enderecoRepository.deleteById(idEndereco);
	}
}

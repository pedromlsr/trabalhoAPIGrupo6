package org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.dtos.ClienteDTO;
import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	@Autowired
	ClienteRepository clienteRepository;

	public List<ClienteDTO> findAllCliente() {
		List<Cliente> listClienteEntidade = clienteRepository.findAll();
		List<ClienteDTO> listClienteDTO = new ArrayList<ClienteDTO>();

		for (Cliente cliente : listClienteEntidade) {
			listClienteDTO.add(EntidadeParaDTO(cliente));
		}
		return listClienteDTO;
	}

	public ClienteDTO findClienteById(Integer idCliente) {
		return clienteRepository.findById(idCliente).isPresent() ?
			EntidadeParaDTO(clienteRepository.findById(idCliente).get()) : null;
	}

	public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
		if (clienteRepository.existsByCpf(clienteDTO.getCpf()) == true) {
			clienteDTO.setCpf(null);
			return clienteDTO;
		}
		if (clienteRepository.existsByEmail(clienteDTO.getEmail()) == true) {
			clienteDTO.setEmail(null);
			return clienteDTO;
		}
		return EntidadeParaDTO(clienteRepository.save(DTOParaEntidade(clienteDTO)));
	}

	public ClienteDTO updateCliente(ClienteDTO clienteDTO) {
		Cliente cliente = DTOParaEntidade(clienteDTO);
		return EntidadeParaDTO(clienteRepository.save(cliente));
	}

	public void deleteClienteById(Integer idCliente) {
		clienteRepository.deleteById(idCliente);
	}

	private Cliente DTOParaEntidade(ClienteDTO clienteDTO) {
		Cliente cliente = new Cliente();

		cliente.setEmail(clienteDTO.getEmail());
		cliente.setNomeCompleto(clienteDTO.getNomeCompleto());
		cliente.setCpf(clienteDTO.getCpf());
		cliente.setTelefone(clienteDTO.getTelefone());
		cliente.setDataNascimento(clienteDTO.getDataNascimento());

		return cliente;
	}

	private ClienteDTO EntidadeParaDTO(Cliente cliente) {
		ClienteDTO clienteDTO = new ClienteDTO();

		clienteDTO.setEmail(cliente.getEmail());
		clienteDTO.setNomeCompleto(cliente.getNomeCompleto());
		clienteDTO.setCpf(cliente.getCpf());
		clienteDTO.setTelefone(cliente.getTelefone());
		clienteDTO.setDataNascimento(cliente.getDataNascimento());

		return clienteDTO;
	}

}

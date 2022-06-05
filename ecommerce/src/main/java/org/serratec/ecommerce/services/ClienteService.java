package org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.serratec.ecommerce.dtos.ClienteDTO;
import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.exceptions.ClienteException;
import org.serratec.ecommerce.exceptions.CpfException;
import org.serratec.ecommerce.exceptions.EmailException;
import org.serratec.ecommerce.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	EnderecoService enderecoService;

	public List<ClienteDTO> findAllCliente() {
		List<Cliente> listClienteEntidade = clienteRepository.findAll();
		List<ClienteDTO> listClienteDTO = new ArrayList<ClienteDTO>();

		for (Cliente cliente : listClienteEntidade) {
			listClienteDTO.add(EntidadeParaDTO(cliente));
		}
		return listClienteDTO;
	}

	public ClienteDTO findClienteById(Integer idCliente) {
		return clienteRepository.findById(idCliente).isPresent()
				? EntidadeParaDTO(clienteRepository.findById(idCliente).get())
				: null;
	}

	public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
		Boolean cpfExistente = clienteRepository.existsByCpf(clienteDTO.getCpf());
		Boolean emailExistente = clienteRepository.existsByEmail(clienteDTO.getEmail());

		if (cpfExistente == true) {
			throw new CpfException("CPF já registrado.");
		} else if (emailExistente == true) {
			throw new EmailException("Email já registrado.");
		} else if (!validate(clienteDTO.getEmail())) {
			throw new EmailException("Email inválido.");
		} else if (!clienteDTO.getNomeCompleto().matches("[a-zA-Z][a-zA-Z ]*")) {
			throw new ClienteException("Nome com apenas letras.");
		} else {
			return EntidadeParaDTO(clienteRepository.save(DTOParaEntidade(clienteDTO)));
		}
	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean validate(String stringEmail) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(stringEmail);
		return matcher.find();
	}

	public ClienteDTO updateCliente(ClienteDTO clienteDTO) throws CpfException, EmailException, ClienteException {

		if (!validate(clienteDTO.getEmail())) {
			throw new EmailException("Email inválido.");
		} else if (!clienteDTO.getNomeCompleto().matches("[a-zA-Z][a-zA-Z ]*")) {
			throw new ClienteException("Nome com apenas letras.");
		} else {
			return EntidadeParaDTO(clienteRepository.save(DTOParaEntidade(clienteDTO)));
		}

	}

	public void deleteClienteById(Integer idCliente) {
		clienteRepository.deleteById(idCliente);
	}

	private Cliente DTOParaEntidade(ClienteDTO clienteDTO) {
		Cliente cliente = new Cliente();

		cliente.setIdCliente(clienteDTO.getIdCliente());
		cliente.setEmail(clienteDTO.getEmail());
		cliente.setNomeCompleto(clienteDTO.getNomeCompleto());
		cliente.setCpf(clienteDTO.getCpf());
		cliente.setTelefone(clienteDTO.getTelefone());
		cliente.setDataNascimento(clienteDTO.getDataNascimento());
		//cliente.setEndereco(enderecoService.findEnderecoById(clienteDTO.getIdEndereco()));

		return cliente;
	}

	private ClienteDTO EntidadeParaDTO(Cliente cliente) {
		ClienteDTO clienteDTO = new ClienteDTO();

		clienteDTO.setIdCliente(cliente.getIdCliente());
		clienteDTO.setEmail(cliente.getEmail());
		clienteDTO.setNomeCompleto(cliente.getNomeCompleto());
		clienteDTO.setCpf(cliente.getCpf());
		clienteDTO.setTelefone(cliente.getTelefone());
		clienteDTO.setDataNascimento(cliente.getDataNascimento());
		if (cliente.getEndereco() != null) {
			clienteDTO.setIdEndereco(cliente.getEndereco().getIdEndereco());
		}

		return clienteDTO;
	}

}

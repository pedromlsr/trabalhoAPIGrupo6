package org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.serratec.ecommerce.dtos.ClienteDTO;
import org.serratec.ecommerce.dtos.EnderecoDTO;
import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.entities.Endereco;
import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.entities.Pedido;
import org.serratec.ecommerce.exceptions.ClienteException;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.repositories.ClienteRepository;
import org.serratec.ecommerce.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	EnderecoService enderecoService;

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	ItemPedidoService itemPedidoService;

	public List<ClienteDTO> findAllCliente() {
		List<Cliente> listCliente = clienteRepository.findAll();
		List<ClienteDTO> listClienteDTO = new ArrayList<>();

		if (listCliente.isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum Cliente encontrado.");
		}

		for (Cliente cliente : listCliente) {
			listClienteDTO.add(convertEntityToDto(cliente));
		}
		
		return listClienteDTO;
	}

	public Cliente findClienteById(Integer id) {
		return clienteRepository.findById(id).get();
	}

	public ClienteDTO findClienteByIdDTO(Integer id) {
		if (!clienteRepository.existsById(id)) {
			throw new NoSuchElementFoundException("O Cliente de id = " + id + " não foi encontrado.");
		}

		return convertEntityToDto(clienteRepository.findById(id).get());
	}

	public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
		clienteDTO.setCpf(clienteDTO.getCpf().replaceAll("[.-]", ""));
		clienteDTO.setTelefone(clienteDTO.getTelefone().replaceAll("[-() ]",""));
		
		Cliente clienteCpfExistente = clienteRepository.findByCpf(clienteDTO.getCpf());
		Cliente clienteEmailExistente = clienteRepository.findByEmail(clienteDTO.getEmail());

		if (clienteDTO.getIdCliente() != null) {

			if (clienteCpfExistente != null && clienteDTO.getIdCliente() != clienteCpfExistente.getIdCliente()) {
				throw new ClienteException("CPF já registrado.");
			} else if (clienteEmailExistente != null
					&& clienteDTO.getIdCliente() != clienteEmailExistente.getIdCliente()) {
				throw new ClienteException("Email já registrado.");
			}

		} else {
			if (clienteCpfExistente != null) {
				throw new ClienteException("CPF já registrado.");
			} else if (clienteEmailExistente != null) {
				throw new ClienteException("Email já registrado.");
			}
		}

		if (!validate(clienteDTO.getEmail())) {
			throw new ClienteException("Email inválido.");
		}
		if (!clienteDTO.getNomeCompleto().matches("[a-zA-Z][a-zA-Z ]*")) {
			throw new ClienteException("Nome com apenas letras.");
		} 
		
		if(clienteDTO.getTelefone().length() > 11 || clienteDTO.getTelefone().length() < 10) {
			throw new ClienteException("O campo telefone deve conter 10 ou 11 digitos.");
		}
		else {

			Cliente novoCliente = clienteRepository.save(convertDtoToEntity(clienteDTO));
			EnderecoDTO endereco = enderecoService.saveEnderecoDTO(novoCliente.getIdCliente(), clienteDTO.getCep(),
					clienteDTO.getNumero(), clienteDTO.getComplemento());

			clienteDTO = convertEntityToDto(clienteRepository.save(novoCliente));
			clienteDTO.setIdEndereco(endereco.getIdEndereco());

			return clienteDTO;
		}

	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean validate(String stringEmail) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(stringEmail);
		return matcher.find();
	}

	public ClienteDTO updateCliente(ClienteDTO clienteDTO) {
		if (clienteDTO.getIdCliente() == null) {
			throw new ClienteException("Não foi informado um id para o Cliente.");
		}
		
		Cliente clienteBD = clienteRepository.existsById(clienteDTO.getIdCliente())
				? clienteRepository.findById(clienteDTO.getIdCliente()).get()
				: null;

		if (clienteBD == null) {
			throw new NoSuchElementFoundException("O Cliente de id = " + clienteDTO.getIdCliente() + " não foi encontrado.");
		}

		Endereco endereco = clienteBD.getEndereco();
		ClienteDTO novoClienteDTO = saveCliente(clienteDTO);
		enderecoService.deleteByIdEndereco(endereco.getIdEndereco());

		return novoClienteDTO;

	}

	public void deleteClienteById(Integer id) {

		if (!clienteRepository.existsById(id)) {
			throw new NoSuchElementFoundException("O Cliente de id = " + id + " não foi encontrado.");
		}

		Cliente clienteBD = clienteRepository.findById(id).get();

		List<Pedido> pedidosCliente = pedidoRepository.findByCliente(clienteBD);

		for (Pedido pedido : pedidosCliente) {
			for (ItemPedido itemPedido : pedido.getItemPedidoList()) {
				itemPedidoService.deleteItemPedidoById(itemPedido.getIdItemPedido());
			}
			
			pedidoRepository.deleteById(pedido.getIdPedido());
		}

		Endereco endereco = clienteBD.getEndereco();
		clienteRepository.deleteById(id);
		enderecoService.deleteByIdEndereco(endereco.getIdEndereco());
	}

	private Cliente convertDtoToEntity(ClienteDTO clienteDTO) {
		Cliente cliente = new Cliente();

		cliente.setIdCliente(clienteDTO.getIdCliente());
		cliente.setEmail(clienteDTO.getEmail());
		cliente.setNomeCompleto(clienteDTO.getNomeCompleto());
		cliente.setCpf(clienteDTO.getCpf());
		cliente.setTelefone(clienteDTO.getTelefone());
		cliente.setDataNascimento(clienteDTO.getDataNascimento());
		if (clienteDTO.getIdEndereco() != null)
			cliente.setEndereco(enderecoService.findEnderecoById(clienteDTO.getIdEndereco()));

		return cliente;
	}

	private ClienteDTO convertEntityToDto(Cliente cliente) {
		ClienteDTO clienteDTO = new ClienteDTO();

		clienteDTO.setIdCliente(cliente.getIdCliente());
		clienteDTO.setEmail(cliente.getEmail());
		clienteDTO.setNomeCompleto(cliente.getNomeCompleto());
		clienteDTO.setCpf(cliente.getCpf());
		clienteDTO.setTelefone(cliente.getTelefone());
		clienteDTO.setDataNascimento(cliente.getDataNascimento());
		if (cliente.getEndereco() != null) {
			clienteDTO.setIdEndereco(cliente.getEndereco().getIdEndereco());
			clienteDTO.setCep(cliente.getEndereco().getCep());
			clienteDTO.setNumero(cliente.getEndereco().getNumero());
			clienteDTO.setComplemento(cliente.getEndereco().getComplemento());
		}

		return clienteDTO;
	}

}

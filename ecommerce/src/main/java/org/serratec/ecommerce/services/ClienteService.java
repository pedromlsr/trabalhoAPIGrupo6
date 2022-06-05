package org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.serratec.ecommerce.dtos.ClienteDTO;
import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.entities.Pedido;
import org.serratec.ecommerce.exceptions.ClienteException;
import org.serratec.ecommerce.exceptions.EnderecoException;
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
		List<Cliente> listClienteEntidade = clienteRepository.findAll();
		List<ClienteDTO> listClienteDTO = new ArrayList<ClienteDTO>();

		if (listClienteEntidade.isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum cliente encontrado.");
		}

		for (Cliente cliente : listClienteEntidade) {
			listClienteDTO.add(EntidadeParaDTO(cliente));
		}
		return listClienteDTO;
	}

	public Cliente findClienteById(Integer idCliente) {
		return clienteRepository.findById(idCliente).isPresent() ? clienteRepository.findById(idCliente).get() : null;
	}

	public ClienteDTO findClienteByIdDTO(Integer idCliente) {
		if (!clienteRepository.existsById(idCliente)) {
			throw new NoSuchElementFoundException("O cliente de Id = " + idCliente + " não foi encontrado.");
		}

		return EntidadeParaDTO(clienteRepository.findById(idCliente).get());
	}

	public ClienteDTO saveCliente(ClienteDTO clienteDTO) throws EnderecoException {

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
		} else {

			Cliente novoCliente = clienteRepository.save(DTOParaEntidade(clienteDTO));
			enderecoService.saveEnderecoDTO(novoCliente.getIdCliente(), clienteDTO.getCep(), clienteDTO.getNumero(),
					clienteDTO.getComplemento());

			return EntidadeParaDTO(clienteRepository.save(novoCliente));
		}

	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean validate(String stringEmail) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(stringEmail);
		return matcher.find();
	}

	public ClienteDTO updateCliente(ClienteDTO clienteDTO) throws EnderecoException {

		if (clienteDTO.getIdCliente() == null) {
			throw new ClienteException("Não foi informado um ID");
		}
		Cliente clienteBD = clienteRepository.findById(clienteDTO.getIdCliente()).isPresent()
				? clienteRepository.findById(clienteDTO.getIdCliente()).get()
				: null;

		if (clienteBD == null) {
			throw new NoSuchElementFoundException("Não foi encontrado um cliente para o ID informado.");
		}

		return saveCliente(clienteDTO);

	}

	public void deleteClienteById(Integer idCliente) {

		if (!clienteRepository.findById(idCliente).isPresent()) {
			throw new NoSuchElementFoundException("Não foi encontrado um cliente para o ID informado.");
		}

		Cliente clienteBD = clienteRepository.findById(idCliente).get();

		List<Pedido> pedidosCliente = pedidoRepository.findByCliente(clienteBD);

		for (Pedido pedido : pedidosCliente) {
			for (ItemPedido itemPedido : pedido.getItemPedidoList()) {
				itemPedidoService.deleteItemPedidoById(itemPedido.getIdItemPedido());
			}
			pedidoRepository.deleteById(pedido.getIdPedido());
		}

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
		if (clienteDTO.getIdEndereco() != null)
			cliente.setEndereco(enderecoService.findEnderecoById(clienteDTO.getIdEndereco()));

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
			clienteDTO.setCep(cliente.getEndereco().getCep());
			clienteDTO.setNumero(cliente.getEndereco().getNumero());
			clienteDTO.setComplemento(cliente.getEndereco().getComplemento());
		}

		return clienteDTO;
	}

}

package org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.serratec.ecommerce.dtos.CadastroEnderecoDTO;
import org.serratec.ecommerce.dtos.EnderecoDTO;
import org.serratec.ecommerce.entities.Cliente;
import org.serratec.ecommerce.entities.Endereco;
import org.serratec.ecommerce.exceptions.EnderecoException;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
import org.serratec.ecommerce.repositories.ClienteRepository;
import org.serratec.ecommerce.repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EnderecoService {
	@Autowired
	EnderecoRepository enderecoRepository;

	@Autowired
	ClienteRepository clienteRepository;

	public List<EnderecoDTO> findAllEndereco() {
		if (enderecoRepository.findAll().isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum endereço encontrado.");
		} else {
			List<EnderecoDTO> enderecoDTOList = new ArrayList<>();

			for (Endereco endereco : enderecoRepository.findAll()) {
				enderecoDTOList.add(convertEntityToDto(endereco));
			}
			return enderecoDTOList;
		}
	}

	public Endereco findEnderecoById(Integer idEndereco) {
		if (!enderecoRepository.existsById(idEndereco)) {
			throw new NoSuchElementFoundException("O endereço de id = " + idEndereco + " não foi encontrado.");
		}

		return enderecoRepository.findById(idEndereco).get();
	}

	public EnderecoDTO findEnderecoByIdDTO(Integer idEndereco) {
		if (!enderecoRepository.existsById(idEndereco)) {
			throw new NoSuchElementFoundException("O endereço de id = " + idEndereco + " não foi encontrado.");
		}

		return convertEntityToDto(enderecoRepository.findById(idEndereco).get());
	}

	public EnderecoDTO saveEnderecoDTO(Integer idCliente, String cep, String numero, String complemento) {
		String limpezaCep = cep.replaceAll("[.-]", "");

		if (!clienteRepository.existsById(idCliente)) {
			throw new NoSuchElementFoundException("O cliente de id = " + idCliente + " não foi encontrado.");
		}

		if (limpezaCep.length() != 8) {
			throw new EnderecoException("O CEP informado é inválido. O CEP deve conter 8 números.");
		}

		if (numero == null) {
			numero = "s/n";
		}

		if (complemento == null) {
			complemento = "";
		}

		Endereco endereco = cepDTOParaEndereco(consultarEnderecoPorCep(limpezaCep));
		endereco.setNumero(numero);
		endereco.setComplemento(complemento);
		endereco.setCep(limpezaCep);

		EnderecoDTO novoEndereco = convertEntityToDto(enderecoRepository.save(endereco));

		Cliente cliente = clienteRepository.findById(idCliente).get();
		cliente.setEndereco(endereco);
		cliente.getEndereco().setIdEndereco(endereco.getIdEndereco());

		return novoEndereco;
	}

	public EnderecoDTO updateEnderecoDTO(Integer idEndereco, EnderecoDTO enderecoDTO) {
		if (!enderecoRepository.existsById(idEndereco)) {
			throw new NoSuchElementFoundException("O endereço com id =" + idEndereco + " não foi encontrado.");
		}

		enderecoDTO.setIdEndereco(idEndereco);
		String limpezaCep = enderecoDTO.getCep().replaceAll("[.-]", "");

		if (limpezaCep.length() != 8) {
			throw new EnderecoException("O CEP informado é inválido. O CEP deve conter 8 números.");
		}

		return convertEntityToDto(enderecoRepository.save(convertDtoToEntity(enderecoDTO)));
	}

	public void deleteByIdEndereco(Integer idEndereco) {
		List<Integer> listaIdEnderecosCadastrados = new ArrayList<>();

		for (Cliente cliente : clienteRepository.findAll()) {
			if (cliente.getEndereco() != null && cliente.getEndereco().getIdEndereco() == idEndereco) {
				listaIdEnderecosCadastrados.add(cliente.getEndereco().getIdEndereco());
			}

		}
		if (!enderecoRepository.existsById(idEndereco)) {
			throw new NoSuchElementFoundException("O endereço com id =" + idEndereco + " não foi encontrado.");
		} else if (!listaIdEnderecosCadastrados.isEmpty()) {
			throw new EnderecoException(
					"Não foi possível excluir esse endereço, existem clientes com esse endereço cadastrado.");
		} else {
			enderecoRepository.deleteById(idEndereco);
		}
	}

	public CadastroEnderecoDTO consultarEnderecoPorCep(String cep) {
		RestTemplate restTemplate = new RestTemplate();
		String uri = "https://viacep.com.br/ws/{cep}/json/";

		Map<String, String> params = new HashMap<String, String>();
		params.put("cep", cep);

		CadastroEnderecoDTO cadastroCepDTO = restTemplate.getForObject(uri, CadastroEnderecoDTO.class, params);

		return cadastroCepDTO;
	}

	private Endereco convertDtoToEntity(EnderecoDTO enderecoDTO) {
		Endereco endereco = new Endereco();

		endereco.setBairro(enderecoDTO.getBairro());
		endereco.setCep(enderecoDTO.getCep());
		endereco.setCidade(enderecoDTO.getCidade());
		endereco.setComplemento(enderecoDTO.getComplemento());
		endereco.setNumero(enderecoDTO.getNumero());
		endereco.setRua(enderecoDTO.getRua());

		return endereco;
	}

	private EnderecoDTO convertEntityToDto(Endereco endereco) {
		EnderecoDTO enderecoDTO = new EnderecoDTO();

		enderecoDTO.setIdEndereco(endereco.getIdEndereco());
		enderecoDTO.setBairro(endereco.getBairro());
		enderecoDTO.setCep(endereco.getCep());
		enderecoDTO.setCidade(endereco.getCidade());
		enderecoDTO.setComplemento(endereco.getComplemento());
		enderecoDTO.setNumero(endereco.getNumero());
		enderecoDTO.setRua(endereco.getRua());

		return enderecoDTO;
	}

	private Endereco cepDTOParaEndereco(CadastroEnderecoDTO cadCepDTO) {
		Endereco endereco = new Endereco();
		endereco.setBairro(cadCepDTO.getBairro());
		endereco.setCep(cadCepDTO.getCep());
		endereco.setCidade(cadCepDTO.getLocalidade());
		endereco.setRua(cadCepDTO.getLogradouro());
		endereco.setUf(cadCepDTO.getUf());

		return endereco;
	}

}

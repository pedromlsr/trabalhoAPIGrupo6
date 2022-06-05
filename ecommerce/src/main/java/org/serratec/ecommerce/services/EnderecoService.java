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
			return null;
		} else {
			List<EnderecoDTO> enderecoDTOList = new ArrayList<>();

			for (Endereco endereco : enderecoRepository.findAll()) {
				enderecoDTOList.add(EntidadeParaDTO(endereco));
			}
			return enderecoDTOList;
		}
	}

	public EnderecoDTO findEnderecoById(Integer idEndereco) {
		if (enderecoRepository.findById(idEndereco).isPresent()) {
			return enderecoRepository.findById(idEndereco).isPresent()
					? EntidadeParaDTO(enderecoRepository.findById(idEndereco).get())
					: null;
		} else {
			throw new NoSuchElementFoundException("Endereço de id " + idEndereco + " não foi encontrado.");
		}
	}

	public EnderecoDTO saveEnderecoDTO(Integer idCliente, String cep, String numero, String complemento)
			throws EnderecoException {
		String limpezaCep = cep.replaceAll("[.-]", "");

		if (!clienteRepository.findById(idCliente).isPresent()) {
			throw new NoSuchElementFoundException(
					"Cliente com id " + idCliente + " não foi encontrado para cadastro do endereço");
		}

		if (limpezaCep.length() != 8) {
			throw new EnderecoException("CEP inválido. CEP deve conter 8 números");
		}

		if (numero == null) {
			numero = "s/n";
		}

		CadastroEnderecoDTO cadCepDTO = consultarEnderecoPorCep(limpezaCep);
		Endereco endereco = cepDTOParaEndereco(cadCepDTO);

		endereco.setNumero(numero);
		endereco.setComplemento(complemento);
		endereco.setCep(limpezaCep);

		return EntidadeParaDTO(enderecoRepository.save(endereco));
	}

	public EnderecoDTO updateEnderecoDTO(Integer idEndereco, EnderecoDTO enderecoDTO) throws EnderecoException {
		if (!enderecoRepository.findById(idEndereco).isPresent()) {
			throw new NoSuchElementFoundException("Endereço com id " + idEndereco + " não foi encontrado");
		}

		enderecoDTO.setIdEndereco(idEndereco);
		String limpezaCep = enderecoDTO.getCep().replaceAll("[.-]", "");

		if (limpezaCep.length() != 8) {
			throw new EnderecoException("CEP inválido. CEP deve conter 8 números");
		}

		return EntidadeParaDTO(enderecoRepository.save(DTOParaEntidade(enderecoDTO)));
	}

	public void deleteByIdEndereco(Integer idEndereco) throws Exception {
		List<Integer> listaIdEnderecosCadastrados = new ArrayList<>();
		
		for (Cliente cliente : clienteRepository.findAll()) {
			if (cliente.getEndereco() != null && cliente.getEndereco().getIdEndereco() == idEndereco) {
				listaIdEnderecosCadastrados.add(cliente.getEndereco().getIdEndereco());
			}

		}
		if (!enderecoRepository.findById(idEndereco).isPresent()) {
			throw new NoSuchElementFoundException("Endereço com id " + idEndereco + " não foi encontrado");
		} else if (!listaIdEnderecosCadastrados.isEmpty()) {
			throw new EnderecoException("Não foi possível excluir esse endereço, existem clientes com esse endereço cadastrado");
		}else {
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

	private Endereco DTOParaEntidade(EnderecoDTO enderecoDTO) {
		Endereco endereco = new Endereco();

		endereco.setBairro(enderecoDTO.getBairro());
		endereco.setCep(enderecoDTO.getCep());
		endereco.setCidade(enderecoDTO.getCidade());
		endereco.setComplemento(enderecoDTO.getComplemento());
		endereco.setNumero(enderecoDTO.getNumero());
		endereco.setRua(enderecoDTO.getRua());

		return endereco;
	}

	private EnderecoDTO EntidadeParaDTO(Endereco endereco) {
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

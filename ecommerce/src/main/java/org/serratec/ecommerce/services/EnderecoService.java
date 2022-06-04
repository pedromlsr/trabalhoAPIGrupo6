package org.serratec.ecommerce.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.serratec.ecommerce.dtos.CadastroEnderecoDTO;
import org.serratec.ecommerce.dtos.EnderecoDTO;
import org.serratec.ecommerce.entities.Endereco;
import org.serratec.ecommerce.repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	
	public CadastroEnderecoDTO consultarEnderecoPorCep(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://viacep.com.br/ws/{cep}/json/";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cep", cep);

        CadastroEnderecoDTO cadastroCepDTO = restTemplate.getForObject(uri,CadastroEnderecoDTO.class, params);

        return cadastroCepDTO;
    }
	
	public Endereco saveEnderecoDTO(String cep, String numero, String complemento) {
		String LimpezaCep = cep.replaceAll("[.-]", "");
		CadastroEnderecoDTO cadCepDTO = consultarEnderecoPorCep(LimpezaCep);
		Endereco endereco = cepDTOParaEndereco(cadCepDTO);
		
		endereco.setNumero(numero);
		endereco.setComplemento(complemento);
		
		return enderecoRepository.save(endereco);
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

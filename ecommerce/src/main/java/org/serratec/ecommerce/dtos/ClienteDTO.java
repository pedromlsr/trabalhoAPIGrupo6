package org.serratec.ecommerce.dtos;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClienteDTO {
	private Integer idCliente;

	@NotBlank(message = "Campo email não informado.")
	@Email(message = "Campo email inválido.")
	private String email;

	@NotBlank(message = "Campo nome não informado.")
	private String nomeCompleto;

	@NotBlank(message = "Campo cpf não informado.")
	@CPF(message = "Campo cpf inválido.")
	private String cpf;

	@NotBlank(message = "Campo telefone não informado.")
	@Size(min = 10, max = 11, message = "O campo telefone deve conter 10 ou 11 digitos.")
	@Pattern(regexp = "[0-9]{10}|[0-9]{11}", message = "Confira o padrão do campo telefone.")
	private String telefone;

	@NotNull(message = "Campo data de nascimento não informado.")
	@Past(message = "A data de nascimento tem que estar no passado.")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataNascimento;
	
	private Integer idEndereco;

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(Integer idEndereco) {
		this.idEndereco = idEndereco;
	}

}

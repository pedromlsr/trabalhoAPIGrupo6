package org.serratec.ecommerce.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "cliente")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idCliente")
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank(message = "Campo não informado.")
	@Column(name = "id_cliente")
	private Integer idCliente;

	@NotBlank(message = "Campo não informado.")
	@Email(message = "Campo inválido.")
	@Column(name = "email")
	private String email;

	@NotBlank(message = "Campo não informado.")
	@Column(name = "nome_completo")
	private String nomeCompleto;

	@NotBlank(message = "Campo não informado.")
	@CPF(message = "Campo inválido.")
	@Column(name = "cpf")
	private String cpf;

	@NotBlank(message = "Campo não informado.")
	@Column(name = "telefone")
	private String telefone;

	@Min(value = 1900, message = "Ano de nascimento deve ser maior que 1900.")
	@Max(value = 2023, message = "Ano de nascimento deve ser menor que 2023.")
	@Column(name = "data_nascimento")
	private Date dataNascimento;

	@ManyToOne
	@JoinColumn(name = "id_endereco", referencedColumnName = "id_endereco")
	private Endereco endereco;

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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

}
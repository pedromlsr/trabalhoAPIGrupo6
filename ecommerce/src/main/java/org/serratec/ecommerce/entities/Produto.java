package org.serratec.ecommerce.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "produto")
@JsonIdentityInfo(scope = Produto.class,generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProduto")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_produto")
	private Integer idProduto;

	@Column(name = "nome")
	@NotBlank(message = "O nome do produto não pode estar em branco")
	private String nomeProduto;

	@Column(name = "descricao")
	@NotBlank(message = "A descrição do produto não pode estar em branco")
	private String descricaoProduto;

	@Column(name = "qtd_estoque")
	@NotNull(message = "A quantidade de produto em estoque não pode estar em branco")
	private Integer qtdEstoque;

	@Column(name = "data_cadastro")
	private LocalDate dataCadastro;

	@Column(name = "valor_unitario")
	@NotNull(message = "O valor do produto não pode estar em branco")
	private Double valorUnitario;

	@Column(name = "imagem")
	private String nomeImagemProduto;

	@ManyToOne
	@JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
	private Categoria categoria;

	@Override
	public String toString() {
		return "<br>Nome: " + nomeProduto + "<br>Descrição: " + descricaoProduto + "<br>Valor: " + valorUnitario
				+ "<hr>";
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public Integer getQtdEstoque() {
		return qtdEstoque;
	}

	public void setQtdEstoque(Integer qtdEstoque) {
		this.qtdEstoque = qtdEstoque;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public String getNomeImagemProduto() {
		return nomeImagemProduto;
	}

	public void setNomeImagemProduto(String nomeImagemProduto) {
		this.nomeImagemProduto = nomeImagemProduto;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}

package org.serratec.ecommerce.dtos;

import java.time.LocalDate;

public class ProdutoDTO {

	private Integer idProduto;
	private String nomeProduto;
	private String descricaoProduto;
	private Integer qtdEstoque;
	private LocalDate dataCadastro;
	private Double valorUnitario;
	private String nomeImagemProduto;
	//private CategoriaDTO categoria;
	
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
	
	

}

package org.serratec.ecommerce.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProdutoPostDTO {

	private Integer idProduto;
	
	@NotBlank(message="O nome do produto não pode estar em branco")
	private String nomeProduto;
	
	@NotBlank(message="A descrição do produto não pode estar em branco")
	private String descricaoProduto;
	
	@NotNull(message="A quantidade de produto em estoque não pode estar em branco")
	@Min(value=0,message="A quantidade no estoque deve ser igual ou maior do que 0")
	private Integer qtdEstoque;	
	
	@NotNull(message="O valor do produto não pode estar em branco")
	@Min(value=0,message="O valor do produto deve ser igual ou maior do que 0")
	private Double valorUnitario;
	
	@NotNull(message="Deve ser informada uma categoria através do seu ID")
	private Integer idCategoria;
	
	public Integer getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
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
	public Double getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
	
	

}

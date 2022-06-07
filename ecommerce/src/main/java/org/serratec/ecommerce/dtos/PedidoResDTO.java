package org.serratec.ecommerce.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PedidoResDTO {
	private Integer idPedido;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataPedido;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataEnvio;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataEntrega;

	private String descricaoStatus;

	private Integer idCliente;

	private String nomeCliente;

	private Double valorLiqTotal;

	public Integer getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}

	public LocalDate getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDate dataPedido) {
		this.dataPedido = dataPedido;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public LocalDate getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(LocalDate dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getDescricaoStatus() {
		return descricaoStatus;
	}

	public void setDescricaoStatus(String descricaoStatus) {
		this.descricaoStatus = descricaoStatus;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public Double getValorLiqTotal() {
		return valorLiqTotal;
	}

	public void setValorLiqTotal(Double valorLiqTotal) {
		this.valorLiqTotal = valorLiqTotal;
	}

}

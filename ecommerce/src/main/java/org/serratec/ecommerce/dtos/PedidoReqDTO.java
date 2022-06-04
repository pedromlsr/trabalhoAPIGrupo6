package org.serratec.ecommerce.dtos;


import java.util.List;

import org.serratec.ecommerce.entities.ItemPedido;

public class PedidoReqDTO {
	

	private Integer idPedido;

	private Integer idCliente;


	private List<ItemPedido> itemPedidoList;

	private Double valorLiqTotal;

	public Integer getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}


	public List<ItemPedido> getItemPedidoList() {
		return itemPedidoList;
	}

	public void setItemPedidoList(List<ItemPedido> itemPedidoList) {
		this.itemPedidoList = itemPedidoList;
	}

	public Double getValorLiqTotal() {
		return valorLiqTotal;
	}

	public void setValorLiqTotal(Double valorLiqTotal) {
		this.valorLiqTotal = valorLiqTotal;

	}

}

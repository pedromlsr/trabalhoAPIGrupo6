package org.serratec.ecommerce.services;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.repositories.ItemPedidoRepository;
import org.serratec.ecommerce.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemPedidoService {
	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	@Autowired
	ProdutoService produtoService;
	
	@Autowired
	PedidoRepository pedidoRepository;

	public PedidoReqDTO salvarItemPedido(PedidoReqDTO pedidoReqDTO) {

		for (ItemPedido itemPedido : pedidoReqDTO.getItemPedidoList()) {
			itemPedido.setPedido(pedidoRepository.findById(pedidoReqDTO.getIdPedido()).get());
			itemPedido.setProduto(produtoService.findProdutoById(itemPedido.getProduto().getIdProduto()));

			itemPedido.setValorBruto(itemPedido.getPrecoVenda() * itemPedido.getQuantidade());
			itemPedido.setValorLiquido(itemPedido.getValorBruto() - (itemPedido.getValorBruto() * itemPedido.getPercentualDesconto()));

			pedidoReqDTO.setValorLiqTotal(pedidoReqDTO.getValorLiqTotal() + itemPedido.getValorLiquido());

			itemPedidoRepository.save(itemPedido);

		}

		return pedidoReqDTO;
	}

//	public List<ItemPedido> findAllItemPedido() {
//		return itemPedidoRepository.findAll();
//	}
//
//	public ItemPedido findItemPedidoById(Integer idItemPedido) {
//		return itemPedidoRepository.findById(idItemPedido).isPresent()
//				? itemPedidoRepository.findById(idItemPedido).get()
//				: null;
//	}
//
//	public ItemPedido saveItemPedido(ItemPedido itemPedido) {
//		return itemPedidoRepository.save(itemPedido);
//	}
//
//	public ItemPedido updateItemPedido(ItemPedido itemPedido) {
//		return itemPedidoRepository.save(itemPedido);
//	}
//
//	public void deleteItemPedidoById(Integer idItemPedido) {
//		itemPedidoRepository.deleteById(idItemPedido);
//	}
}

package org.serratec.ecommerce.services;

import java.util.List;

import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.repositories.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemPedidoService {
	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	public List<ItemPedido> findAllItemPedido() {
		return itemPedidoRepository.findAll();
	}

	public ItemPedido findItemPedidoById(Integer idItemPedido) {
		return itemPedidoRepository.findById(idItemPedido).isPresent()
				? itemPedidoRepository.findById(idItemPedido).get()
				: null;
	}

	public ItemPedido saveItemPedido(ItemPedido itemPedido) {
		return itemPedidoRepository.save(itemPedido);
	}

	public ItemPedido updateItemPedido(ItemPedido itemPedido) {
		return itemPedidoRepository.save(itemPedido);
	}

	public void deleteItemPedidoById(Integer idItemPedido) {
		itemPedidoRepository.deleteById(idItemPedido);
	}
}

package org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.serratec.ecommerce.dtos.ItemPedidoDTO;
import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.entities.ItemPedido;
import org.serratec.ecommerce.exceptions.ItemPedidoException;
import org.serratec.ecommerce.exceptions.NoSuchElementFoundException;
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

	public List<ItemPedido> findAllItemPedido() {
		return itemPedidoRepository.findAll();
	}

	public List<ItemPedidoDTO> findAllItemPedidoDTO() {
		if (itemPedidoRepository.findAll().isEmpty()) {
			throw new NoSuchElementFoundException("Nenhum ItemPedido encontrado.");
		}

		List<ItemPedidoDTO> listItemPedidoDTO = new ArrayList<>();

		for (ItemPedido itemPedido : itemPedidoRepository.findAll()) {

			listItemPedidoDTO.add(convertEntityToDto(itemPedido));
		}

		return listItemPedidoDTO;
	}

	public ItemPedidoDTO findItemPedidoByIdDTO(Integer id) {
		if (!itemPedidoRepository.existsById(id)) {
			throw new NoSuchElementFoundException("O ItemPedido de id = " + id + " não foi encontrado.");
		}

		return convertEntityToDto(itemPedidoRepository.findById(id).get());
	}

	public PedidoReqDTO salvarItemPedido(PedidoReqDTO pedidoReqDTO) {

		pedidoReqDTO.setValorLiqTotal(0.00);
		
		for (ItemPedido itemPedido : pedidoReqDTO.getItemPedidoList()) {

			Integer idProdutoItemPedido = itemPedido.getProduto().getIdProduto();

			itemPedido.setPedido(pedidoRepository.findById(pedidoReqDTO.getIdPedido()).get());

			if (produtoService.findProdutoById(idProdutoItemPedido) == null) {
				throw new NoSuchElementFoundException(
						"O produto de id = " + idProdutoItemPedido + "não foi encontrado");
			}

			itemPedido.setProduto(produtoService.findProdutoById(itemPedido.getProduto().getIdProduto()));

			if (itemPedido.getProduto().getQtdEstoque() < itemPedido.getQuantidade()) {
				throw new ItemPedidoException(" O estoque do Produto de id = " + idProdutoItemPedido
						+ " não possui quantidade suficiente para este pedido");
			}
			itemPedido.getProduto().setQtdEstoque(itemPedido.getProduto().getQtdEstoque() - itemPedido.getQuantidade());
			itemPedido.setValorBruto(itemPedido.getPrecoVenda() * itemPedido.getQuantidade());
			itemPedido.setValorLiquido(
					itemPedido.getValorBruto() - (itemPedido.getValorBruto() * itemPedido.getPercentualDesconto()));

			pedidoReqDTO.setValorLiqTotal(pedidoReqDTO.getValorLiqTotal() + itemPedido.getValorLiquido());

			itemPedidoRepository.save(itemPedido);

		}
		return pedidoReqDTO;
	}

	public void deleteItemPedidoById(Integer idItemPedido) {
		itemPedidoRepository.deleteById(idItemPedido);
	}

	public ItemPedidoDTO convertEntityToDto(ItemPedido itemPedido) {
		ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO();

		itemPedidoDTO.setIdItemPedido(itemPedido.getIdItemPedido());
		itemPedidoDTO.setIdPedido(itemPedido.getPedido().getIdPedido());
		itemPedidoDTO.setIdProduto(itemPedido.getProduto().getIdProduto());
		itemPedidoDTO.setNomeProduto(itemPedido.getProduto().getNomeProduto());
		itemPedidoDTO.setPercentualDesconto(itemPedido.getPercentualDesconto());
		itemPedidoDTO.setPrecoVenda(itemPedido.getPrecoVenda());
		itemPedidoDTO.setQuantidade(itemPedido.getQuantidade());
		itemPedidoDTO.setValorBruto(itemPedido.getValorBruto());
		itemPedidoDTO.setValorLiquido(itemPedido.getValorLiquido());

		return itemPedidoDTO;
	}

}

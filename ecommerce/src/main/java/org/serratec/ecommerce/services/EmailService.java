package org.serratec.ecommerce.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.entities.ItemPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Value("${spring.mail.host}")
	private String mailHost;

	@Value("${spring.mail.port}")
	private String mailPort;

	@Value("${spring.mail.username}")
	private String mailUserName;

	@Value("${spring.mail.password}")
	private String mailPassword;

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	ClienteService clienteService;

	@Autowired
	EnderecoService enderecoService;
	
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void enviarEmailHtml(PedidoReqDTO pedidoReqDTO) {
		try {
		MimeMessage email = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(email, "utf-8");
		
		helper.setFrom("grupo6serratec2022.1@outlook.com");
//		helper.setTo("grupo6serratec2022.1@outlook.com");
		helper.setTo(clienteService.findClienteById(pedidoReqDTO.getIdCliente()).getEmail());
		helper.setSubject("Ecommerce Serratec - Seu Pedido: Nº " + pedidoReqDTO.getIdPedido());
		helper.setText(montarCorpoEmail(pedidoReqDTO), true);
		javaMailSender.send(email);
		} catch (MessagingException e) {
			System.out.println("Erro ao enviar o email.");
		}
	}

	private String montarCorpoEmail(PedidoReqDTO pedidoReqDTO) {
		String produtoEmail = "";
		
		for (ItemPedido itemPedido : pedidoReqDTO.getItemPedidoList()) {
			produtoEmail += "<b>> Produto: " + itemPedido.getProduto().getNomeProduto() + " (id #" + itemPedido.getProduto().getIdProduto() + ")</b><br>" +
							"- Preço: " + String.format("%.2f", itemPedido.getPrecoVenda()) + "<br>" +
							"- Qtd.: x" + itemPedido.getQuantidade() + "<br>" +
							"- Desconto: R$ " + String.format("%.2f", itemPedido.getPrecoVenda() * itemPedido.getPercentualDesconto() * itemPedido.getQuantidade()) + "<br>" +
							"- Total: R$ " + String.format("%.2f", itemPedido.getValorLiquido()) + "<br><br>";
		}
		
		String corpoEmail =
				"<body style = \"background-image: url('https://i.pinimg.com/originals/2e/52/6d/2e526dac6eafd1650d8918538230dc3d.jpg'); background-repeat: no-repeat; background-attachment: fixed; background-size: cover; background-position: center;\"> " +
				"<div style = \"padding: 30px\"><h1>Olá, " + clienteService.findClienteById(pedidoReqDTO.getIdCliente()).getNomeCompleto() + "!</h1><br>" +
				"<p>Seu pedido foi realizado com sucesso!</p>" +
				"<p>Os produtos serão enviados assim que o pagamento for confirmado.</p>" +
				"<p>Número do pedido: " + pedidoReqDTO.getIdPedido() + "</p><br><hr>" +
				"<h1>Dados do comprador:</h1><br>" +
				"<h1>Info:</h1>" +
				"<p>" + clienteService.findClienteById(pedidoReqDTO.getIdCliente()) + "</p><br>" +
				"<h1>Endereço:</h1>" +
				"<p>" + enderecoService.findEnderecoById(clienteService.findClienteById(pedidoReqDTO.getIdCliente()).getEndereco().getIdEndereco()) + "<p><br><hr>" +
				"<h1>Produtos:</h1><br>" + 
				"<p>" + produtoEmail + "</p>" +
				"<h3>Valor da compra: R$ " + String.format("%.2f", pedidoReqDTO.getValorLiqTotal()) + "</h3><br><hr>" +
				"<h1>Obrigado pela preferência! :)</h1></div></body>";

		return corpoEmail;
	}

}

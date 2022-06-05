package org.serratec.ecommerce.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.serratec.ecommerce.entities.ItemPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
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

	public void enviarEmailTexto(PedidoReqDTO pedidoReqDTO) {
		SimpleMailMessage email = new SimpleMailMessage();

		email.setFrom("grupo6serratec2022.1@outlook.com");
		email.setTo("grupo6serratec2022.1@outlook.com");
//		email.setTo(clienteService.findClienteById(pedidoReqDTO.getIdCliente()).getEmail());
		email.setSubject("Pedido: " + pedidoReqDTO.getIdPedido());
		email.setText(montarCorpoEmail(pedidoReqDTO));

//		Podemos definir o email do remente no arquivo de propriedades ou configurar aqui sempre que quiser
//		Cuidado no momento de usar um servidor real, para setar um remetente válido abaixo

		javaMailSender.send(email);

	}

	public void enviarEmailHtml(PedidoReqDTO pedidoReqDTO) throws MessagingException {
		MimeMessage email = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(email, "utf-8");
//		String htmlMsg = "";
		
		helper.setFrom("grupo6serratec2022.1@outlook.com");
		helper.setTo("grupo6serratec2022.1@outlook.com");
		helper.setSubject("Pedido: " + pedidoReqDTO.getIdPedido());
		helper.setText(montarCorpoEmail(pedidoReqDTO), true);
		javaMailSender.send(email);
		
	}

	private String montarCorpoEmail(PedidoReqDTO pedidoReqDTO) {
//		List<String> listaNomeProduto = new ArrayList<>();
//		List<String> listaDescProduto = new ArrayList<>();
//		List<Integer> listaQuantProduto = new ArrayList<>();
		
		String produtoEmail = "";
		
		for (ItemPedido itemPedido : pedidoReqDTO.getItemPedidoList()) {
//			listaNomeProduto.add(itemPedido.getProduto().getNomeProduto());
//			listaDescProduto.add(itemPedido.getProduto().getDescricaoProduto());
//			listaQuantProduto.add(itemPedido.getQuantidade());

			produtoEmail += "> " + itemPedido.getProduto().getNomeProduto() + " --- x" + itemPedido.getQuantidade() + " --- R$ " + String.format("%.2f", itemPedido.getValorLiquido()) + "<br>";
		}
		
		
		String corpoEmail =
				"<h3>Olá, " + clienteService.findClienteById(pedidoReqDTO.getIdCliente()).getNomeCompleto() + "!</h3><br>" +
				"<p>Seu pedido foi realizado com sucesso!</p>" +
				"<p>Os produtos serão enviados assim que o pagamento for confirmado.</p><br><hr>" +
				"<h3>Dados do comprador:</h3><br>" +
				"<p>Info:</p>" +
				"<p>" + clienteService.findClienteById(pedidoReqDTO.getIdCliente()) + "</p><br>" +
				"<p>Endereço:</p>" +
				"<p>" + enderecoService.findEnderecoById(clienteService.findClienteById(pedidoReqDTO.getIdCliente()).getEndereco().getIdEndereco()) + "<p><br><hr>" +
				"<h3>Produtos:</h3><br>" + 
				"<p>" + produtoEmail + "</p>" +
				"<p><b>Total: R$ " + String.format("%.2f", pedidoReqDTO.getValorLiqTotal()) + "</b></p>";

		return corpoEmail;
	}

}

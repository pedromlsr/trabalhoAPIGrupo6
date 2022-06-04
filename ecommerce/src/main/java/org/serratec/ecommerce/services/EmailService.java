package org.serratec.ecommerce.services;

import org.serratec.ecommerce.dtos.PedidoReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
	
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void enviarEmailTexto(PedidoReqDTO pedidoReqDTO) {
		SimpleMailMessage mensagem = new SimpleMailMessage();
		
		mensagem.setFrom("teste@teste.com");
		mensagem.setTo("teste@teste.com");
//		mensagem.setTo(clienteService.findClienteById(pedidoReqDTO.getIdCliente()).getEmail());
		mensagem.setSubject("Pedido: " + pedidoReqDTO.getIdPedido());
		mensagem.setText("Aloaloaloaaaolaoalaaal");
		
//		Podemos definir o email do remente no arquivo de propriedades ou configurar aqui sempre que quiser
//		Cuidado no momento de usar um servidor real, para setar um remetente válido abaixo
		
		javaMailSender.send(mensagem);
	}
}

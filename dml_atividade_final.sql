
INSERT INTO status (id_status, desc_status) VALUES
(1, 'Aguardando pagamento'),
(2, 'Enviado'),
(3, 'Entregue');

INSERT INTO endereco (cep, rua, bairro, cidade, numero, complemento, UF) values 
('25710110', 'Rua Viúva Lima', 'Itamarati', 'Petrópolis', '426','Fundos', 'RJ');

INSERT INTO cliente (email, nome_completo, cpf, telefone, data_nascimento, id_endereco) values
('fabio@hotmail.com', 'Fabio Freitas Costa', '54818881309', '2422334455', '1994-04-25',1);

INSERT INTO pedido (data_pedido, id_status, id_cliente) values 
('2022-06-06', 1, 1);

INSERT INTO categoria (nome, descricao) values
('Esportes','Descrição de categoria de esportes');

INSERT INTO produto (nome, descricao, qtd_estoque, data_cadastro, valor_unitario, id_categoria) values
('Bola de futebol', 'Bola da copa 2022', 100, '2022-06-01', 199, 1); 

INSERT INTO item_pedido (quantidade, preco_venda, percentual_desconto, id_pedido, id_produto) values
(1, 199, 0, 1, 1);
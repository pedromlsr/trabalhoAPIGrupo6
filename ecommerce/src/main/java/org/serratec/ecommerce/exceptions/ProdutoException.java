package org.serratec.ecommerce.exceptions;

public class ProdutoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProdutoException(String message) {
		super(message);
	}
}
package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class InvalidSelectedOptionException extends Exception{

	private static final long serialVersionUID = 1L;

	public String toString() {
		String message = "Opção inválida, tente novamente!";
		return Utils.generateMessage(message);
	}
	
}

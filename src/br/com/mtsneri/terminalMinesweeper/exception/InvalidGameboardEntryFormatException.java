package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class InvalidGameboardEntryFormatException extends Exception{

	private static final long serialVersionUID = 1L;

	public String toString() {
		String message = "Formato inválido para as entradas, tente novamente! (EXEMPLO: 2,3)";
		return Utils.generateMessage(message);
	}
	
}

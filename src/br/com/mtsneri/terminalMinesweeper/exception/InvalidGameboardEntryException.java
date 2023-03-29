package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class InvalidGameboardEntryException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public String toString() {
		String message = "As entradas para o tabuleiro são inválidas, verifique os limites informados e tente novamente.";
		return Utils.generateMessage(message);
	}
	
}

package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class MarkedFieldNotGeneratedException extends Exception{

	private static final long serialVersionUID = 1L;

	public String toString() {
		String message = "Você não pode marcar um campo antes de abrir o primeiro.";
		return Utils.generateMessage(message);
	}
	
}


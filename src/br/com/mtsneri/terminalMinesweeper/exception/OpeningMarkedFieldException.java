package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class OpeningMarkedFieldException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public String toString() {
		String message = "Você não pode abrir um campo que está marcado!";
		return Utils.generateMessage(message);
	}
	
}

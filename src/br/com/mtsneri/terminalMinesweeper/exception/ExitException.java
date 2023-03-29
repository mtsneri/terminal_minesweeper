package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class ExitException extends RuntimeException{
 
	private static final long serialVersionUID = 1L;

	public String toString() {
		String message = "Você saiu do jogo!";
		return Utils.generateMessage(message);
	}
	
}

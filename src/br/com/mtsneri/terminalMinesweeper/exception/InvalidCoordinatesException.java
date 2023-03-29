package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class InvalidCoordinatesException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public String toString() {
		String message = "As coordenadas informadas estão fora do limite do tabuleiro! Verifique e tente novamente.";
		return Utils.generateMessage(message);
	}
	
}
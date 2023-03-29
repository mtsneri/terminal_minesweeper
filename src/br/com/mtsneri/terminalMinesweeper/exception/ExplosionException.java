package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class ExplosionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public String toString() {
		String message = "Explodiu tudo! Você perdeu :(";
		return Utils.generateMessage(message);
	}
	
}

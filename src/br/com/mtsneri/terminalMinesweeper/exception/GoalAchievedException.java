package br.com.mtsneri.terminalMinesweeper.exception;

import br.com.mtsneri.terminalMinesweeper.utils.Utils;

public class GoalAchievedException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public String toString() {
		String message = "Você ganhou! Parabéns!";
		return Utils.generateMessage(message);
	}
	
}

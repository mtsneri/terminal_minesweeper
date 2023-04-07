package br.com.mtsneri.terminalMinesweeper;

import br.com.mtsneri.terminalMinesweeper.exception.InvalidGameboardEntryException;
import br.com.mtsneri.terminalMinesweeper.model.Gameboard;
import br.com.mtsneri.terminalMinesweeper.utils.Utils;
import br.com.mtsneri.terminalMinesweeper.view.GameboardConsole;

public class Application {
	public static final String softwareVersion = "0.0.3";
	public static final String softwareAuthor = "Mateus Neri de Souza";
	
	// Main Method
	public static void main(String[] args) {
		Utils.softwarePresentation();
		Application.start();
	}
	
	// Static Methods
	public static void start() {
		try {
			Gameboard gameboard = new Gameboard(Utils.getUserGameboardEntries());
			new GameboardConsole(gameboard);
		} catch (InvalidGameboardEntryException e) {
			System.out.println(e);
			Application.start();
		}
	}

}

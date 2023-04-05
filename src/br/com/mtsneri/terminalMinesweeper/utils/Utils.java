package br.com.mtsneri.terminalMinesweeper.utils;

import java.util.Scanner;

import br.com.mtsneri.terminalMinesweeper.Application;
import br.com.mtsneri.terminalMinesweeper.model.Gameboard;

public class Utils {
	
	// Static Methods
	private static String generateMessageContainer(int size) {
		String messageContainer = "";
		for(int i = 0; i < size; i++) {
			messageContainer += "-";
		}
		return messageContainer;
	}
	
	public static String generateMessage(String message) {
		int messageLength = message.length();
		String messageContainer = Utils.generateMessageContainer(messageLength);
		String generatedMessage = String.format("%n%s%n%s%n%s%n", messageContainer, message, messageContainer);
		return generatedMessage;
	}
	
	public static void softwarePresentation() {
		System.out.println("Campo Minado - Versão: " + Application.softwareVersion);
		System.out.println("Autor: " + Application.softwareAuthor);
	}
	
	public static int[] getUserGameboardEntries() {
		try {
			@SuppressWarnings("resource")
			Scanner entry = new Scanner(System.in);

			int x = 0;
			int y = 0;
			int mines = 0;
			
			System.out.println("------------------------------------------------------------------------------------------------------");
			System.out.printf("%n# Limites para criação do Tabuleiro%n");
			System.out.printf("%nLinhas    [Mínimo:   %d] [Máximo:   %d]", Gameboard.MIN_ROWS, Gameboard.MAX_ROWS);
			System.out.printf("%nColunas   [Mínimo:   %d] [Máximo:   %d]", Gameboard.MIN_COLUMNS, Gameboard.MAX_COLUMNS);
			System.out.printf("%nMinas     [Mínimo:  %.0f%%] [Máximo: %.0f%%] (Porcentagem em relação à quantidade de campos no Tabuleiro)\n\n", Gameboard.MIN_MINES_FACTOR * 100, Gameboard.MAX_MINES_FACTOR * 100);
			System.out.println("------------------------------------------------------------------------------------------------------");
			System.out.println("\nDigite as informações do seu Tabuleiro a seguir\n\n");

			System.out.printf("Quantidade de linhas: ");
			x = Integer.parseInt(entry.nextLine());
			
			System.out.printf("Quantidade de colunas: ");
			y = Integer.parseInt(entry.nextLine());

			System.out.printf("Quantidade de minas: ");
			mines = Integer.parseInt(entry.nextLine());

			System.out.println("\n");
			
			int entries[] = {x, y, mines};
			
			return entries;
		} catch (NumberFormatException e) {
			String message = "O valor informado é inválido. Deve ser informado um NÚMERO dentro dos limites informados!";
			System.out.println(Utils.generateMessage(message));
			return Utils.getUserGameboardEntries();
		}
	}
}

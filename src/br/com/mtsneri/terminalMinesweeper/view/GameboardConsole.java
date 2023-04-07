package br.com.mtsneri.terminalMinesweeper.view;

import java.util.Scanner;
import java.util.function.Predicate;

import br.com.mtsneri.terminalMinesweeper.exception.ExitException;
import br.com.mtsneri.terminalMinesweeper.exception.ExplosionException;
import br.com.mtsneri.terminalMinesweeper.exception.GoalAchievedException;
import br.com.mtsneri.terminalMinesweeper.exception.InvalidCoordinatesException;
import br.com.mtsneri.terminalMinesweeper.exception.InvalidGameboardEntryFormatException;
import br.com.mtsneri.terminalMinesweeper.exception.InvalidSelectedOptionException;
import br.com.mtsneri.terminalMinesweeper.exception.MarkedFieldNotGeneratedException;
import br.com.mtsneri.terminalMinesweeper.exception.OpeningMarkedFieldException;
import br.com.mtsneri.terminalMinesweeper.model.Field;
import br.com.mtsneri.terminalMinesweeper.model.Gameboard;

public class GameboardConsole {

	// Instance Variables
	private Gameboard gameboard;
	private Scanner scanner = new Scanner(System.in);

	// Constructor
	public GameboardConsole(Gameboard gameboard) {
		this.gameboard = gameboard;
		this.executeGame();
	}

	// Instance Methods
	private void executeGame() {
		try {
			System.out.println("Seu Tabuleiro foi criado com sucesso.\nVocê iniciou uma nova partida, boa sorte!");

			boolean continueGame = true;

			while (continueGame) {
				gameFlow();

				System.out.println("Outra partida? (S/n) ");
				String userAnswer = scanner.nextLine();

				if (userAnswer.equalsIgnoreCase("n")) {
					continueGame = false;
				} else {
					this.gameboard.restart();
				}
			}
		} catch (ExitException e) {
			System.out.println(e);
		} finally {
			scanner.close();
		}
	}

	private void gameFlow() {
		try {
			boolean isGameboardGoalAchieved = this.gameboard.goalAchieved();
			boolean isFieldsCreated = !this.gameboard.fields.isEmpty();
			boolean isFieldsNotCreated = !isFieldsCreated;

			while (!isGameboardGoalAchieved || isFieldsNotCreated) {
				if (isFieldsNotCreated) {
					this.gameboard.emptyGameboardPresentation();
				}

				if (isFieldsCreated) {
					System.out.println("\n" + this.gameboard);
				}

				String entry = this.getUserEntryValue("\nDigite (linha, coluna): ");
				entry = entry.replace(" ", "");
				this.validateCoordinatesFormat(entry);
				
				String[] coordinates = entry.split(",");
				int rowValue = Integer.parseInt(coordinates[0]);
				int columnValue = Integer.parseInt(coordinates[1]);
				this.validateCoordinatesRange(rowValue, columnValue);
				
				entry = this.getUserEntryValue("\n1 - Abrir \n2 - Alternar Marcação\n\nEscolha: ");
				this.executeSelectedOption(entry, rowValue, columnValue, isFieldsNotCreated);	
				
				isFieldsCreated = !this.gameboard.fields.isEmpty();
				isFieldsNotCreated = !isFieldsCreated;
				
				// Test
				if(rowValue == 5 && columnValue == 5) {
					this.gameboard.fields.stream().forEach(v -> v.setGoalAchiev(true));
				}
				
				isGameboardGoalAchieved = this.gameboard.goalAchieved();
				this.checkIfGameboardGoalIsAchieved(isGameboardGoalAchieved);
			}

		} catch (ExplosionException | MarkedFieldNotGeneratedException | InvalidCoordinatesException
				| OpeningMarkedFieldException | InvalidGameboardEntryFormatException | InvalidSelectedOptionException | GoalAchievedException e) {
			
			if (e instanceof ExplosionException) {
				this.gameboard.fields.stream().forEach(field -> field.setGoalAchiev(true));
				System.out.println(e);
				System.out.println(this.gameboard.revealFields());
			}
			
			if (e instanceof GoalAchievedException) {
				System.out.println(e);
				System.out.println(this.gameboard.revealFields());
			}

			if (e instanceof MarkedFieldNotGeneratedException || 
					e instanceof InvalidCoordinatesException ||
					e instanceof OpeningMarkedFieldException ||
					e instanceof InvalidGameboardEntryFormatException ||
					e instanceof InvalidSelectedOptionException) 
			{
				System.out.println(e);
				this.gameFlow();
			}
		}
	}

	private void validateCoordinatesFormat(String entry) throws InvalidGameboardEntryFormatException {
		String entryValidFormat = String.format("^\\d*,\\d*$");
		
		if(!entry.matches(entryValidFormat)) {
			throw new InvalidGameboardEntryFormatException();
		}
	}
	
	private void validateCoordinatesRange(int row, int column) throws InvalidCoordinatesException {
		boolean isRowInLimitRange = row >= 1 && row <= this.gameboard.getTotalRows();
		boolean isColumnInLimitRange = column >= 1 && column <= this.gameboard.getTotalColumns();

		boolean isCoordinateValid = isRowInLimitRange && isColumnInLimitRange;
		boolean isCoordinateInvalid = !isCoordinateValid;

		if (isCoordinateInvalid) {
			throw new InvalidCoordinatesException();
		}
	}

	private void executeSelectedOption(String entry, int rowValue, int columnValue, boolean isFieldsNotCreated) throws MarkedFieldNotGeneratedException, InvalidSelectedOptionException {
		final int OPEN = 1;
		final int MARK = 2;
		
		int userEntry = Integer.parseInt(entry);
		
		Predicate<Field> selectedField = field -> field.getRow() == rowValue && field.getColumn() == columnValue;
		
		if (userEntry == OPEN) {
			this.gameboard.openField(rowValue, columnValue);
		}

		if (userEntry == MARK && isFieldsNotCreated) {
			throw new MarkedFieldNotGeneratedException();
		}

		if (userEntry == MARK) {
			this.gameboard.fields.stream().filter(selectedField).findFirst().get().toggleMarkup();
		}

		if (userEntry != OPEN && userEntry != MARK) {
			throw new InvalidSelectedOptionException();
		}
	}
	
	private void checkIfGameboardGoalIsAchieved(boolean isGameboardGoalAchieved) throws GoalAchievedException {
		if(isGameboardGoalAchieved) {
			throw new GoalAchievedException();
		}
	}
	
	private String getUserEntryValue(String entryRequestMessage) {
		System.out.print(entryRequestMessage);
		String userEntry = scanner.nextLine();

		if (userEntry.equalsIgnoreCase("sair")) {
			throw new ExitException();
		}

		return userEntry;
	}

}

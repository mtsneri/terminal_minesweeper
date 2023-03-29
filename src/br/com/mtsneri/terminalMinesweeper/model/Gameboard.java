package br.com.mtsneri.terminalMinesweeper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;

import br.com.mtsneri.terminalMinesweeper.exception.ExplosionException;
import br.com.mtsneri.terminalMinesweeper.exception.InvalidGameboardEntryException;
import br.com.mtsneri.terminalMinesweeper.exception.OpeningMarkedFieldException;

public class Gameboard {

	// Static Variables
	public static final double MIN_MINES_FACTOR = 0.05;
	public static final double MAX_MINES_FACTOR = 0.6;
	public static final int MIN_ROWS = 4;
	public static final int MAX_ROWS = 9;
	public static final int MIN_COLUMNS = 4;
	public static final int MAX_COLUMNS = 9;

	// Instance Variables
	private final int TOTAL_ROWS;
	private final int TOTAL_COLUMNS;
	private final int TOTAL_FIELDS;
	private final int MIN_MINES;
	private final int MAX_MINES;
	private final int TOTAL_MINES;

	private boolean gameRestarted = false;
	public int qtdeCamposClicados = 0;
	
	public List<Integer> minesPosition = new ArrayList<>();
	public List<Field> fields = new ArrayList<>();

	// Constructor
	public Gameboard(int entries[]) throws InvalidGameboardEntryException {
		
		int numRows = entries[0];
		int numColumns = entries[1];
		int numMines = entries[2];
		
		boolean isEntryInRowsRange = numRows >= Gameboard.MIN_ROWS && numRows <= Gameboard.MAX_ROWS;
		boolean isEntryInColumnsRange = numColumns >= Gameboard.MIN_COLUMNS && numColumns <= Gameboard.MAX_COLUMNS;
		
		int totalFields = numRows * numColumns;
		int minMines = (int) Math.floor(totalFields * Gameboard.MIN_MINES_FACTOR);
		int maxMines = (int) Math.floor(totalFields * Gameboard.MAX_MINES_FACTOR);
		
		boolean isMineEntryValid = numMines >= minMines && numMines <= maxMines; 
		
		boolean isEntryValid = isEntryInRowsRange && isEntryInColumnsRange && isMineEntryValid;
		
		if (isEntryValid) {
			this.TOTAL_ROWS = numRows;
			this.TOTAL_COLUMNS = numColumns;
			this.TOTAL_FIELDS = totalFields;
			this.MIN_MINES = minMines;
			this.MAX_MINES = maxMines;
			this.TOTAL_MINES = numMines;
		} else {
			throw new InvalidGameboardEntryException();
		}
	}

	// Instance Methods
	private void generateFields() {
		int row = 1;
		int column = 1;

		for (int i = 0; i < this.TOTAL_FIELDS; i++) {
			this.fields.add(new Field(row, column));
			this.fields.get(i).setGameboard(this);
			column++;

			if (column > this.TOTAL_COLUMNS) {
				column = 1;
				row++;
			}
		}
	}

	private void associateNeighbors() {
		for (Field fieldOne : this.fields) {
			for (Field fieldTwo : this.fields) {
				fieldOne.addNeighbor(fieldTwo);
			}
		}
	}

	private void sortMines(int row, int column) {
		Predicate<Field> findIndexOfFirstFieldOpened = field -> (field.getRow() == row) && (field.getColumn() == column);
		Predicate<Field> isFieldMined = field -> field.isMined();
		int indexOfFirstFieldOpened = this.fields
				.indexOf(this.fields.stream().filter(findIndexOfFirstFieldOpened).findFirst().get());
		long minesSorteds = this.fields.stream().filter(isFieldMined).count();

		while (minesSorteds < (long) this.getTotalMines()) {
			int randomPosition = ThreadLocalRandom.current().nextInt(0, this.getTotalFields());

			if (randomPosition == indexOfFirstFieldOpened) {
				continue;
			}

			if (this.minesPosition.isEmpty()) {
				this.fields.get(randomPosition).setMined(true);

				if (this.fields.get(indexOfFirstFieldOpened).minesInTheNeighborhood() > 0) {
					this.fields.get(randomPosition).setMined(false);
					continue;
				}

				this.minesPosition.add(randomPosition);
				minesSorteds = this.fields.stream().filter(isFieldMined).count();
				continue;
			}

			for (Integer position : minesPosition) {
				if (randomPosition == position) {
					continue;
				}
			}

			this.fields.get(randomPosition).setMined(true);

			if (this.fields.get(indexOfFirstFieldOpened).minesInTheNeighborhood() > 0) {
				this.fields.get(randomPosition).setMined(false);
				continue;
			}

			minesPosition.add(randomPosition);
			minesSorteds = this.fields.stream().filter(isFieldMined).count();
		}
	}

	public boolean goalAchieved() {
		Predicate<Field> isGoalAchieved = field -> field.goalAchieved();
		return this.fields.stream().allMatch(isGoalAchieved);
	}

	public void restart() {
		Consumer<Field> restartField = field -> field.restart();
		this.fields.stream().forEach(restartField);
		this.setGameRestarted(true);
	}

	public void generateValues(int row, int column) {
		this.fields.forEach(field -> field.generateValue(row, column));
	}

	public void openField(int row, int column) {
		try {
			Predicate<Field> fieldToOpen = field -> (field.getColumn() == column) && (field.getRow() == row);
			Consumer<Field> open = field -> field.open();

			if (this.fields.isEmpty()) {
				this.generateFields();
				this.sortMines(row, column);
				this.generateValues(row, column);
				this.associateNeighbors();
			}

			if (this.getGameRestarted()) {
				this.sortMines(row, column);
				this.generateValues(row, column);
				this.setGameRestarted(false);
			}

			this.fields.stream().filter(fieldToOpen).findFirst().ifPresent(open);
			
		} catch (ExplosionException | OpeningMarkedFieldException e) {
			throw e;
		}
	}

	public void toggleMarkup(int row, int column) {
		Predicate<Field> fieldToMark = field -> (field.getColumn() == column) && (field.getRow() == row);
		Consumer<Field> mark = field -> field.mark();
		this.fields.stream().filter(fieldToMark).findFirst().ifPresent(mark);
	}

	public String revealFields() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i <= this.getTotalColumns(); i++) {
			if (i == 0) {
				sb.append("     ");
				continue;
			}
			sb.append(" " + i + " ");
		}

		sb.append("\n\n");
		
		Consumer<Field> getFieldsValues = field -> {
			if (field.getColumn() == 1) {
				sb.append(" " + field.getRow() + "   ");
			}
			
			sb.append(" ");
			sb.append(field.reveal());
			sb.append(" ");

			if ((field.getColumn() == field.getGameboard().getTotalRows()) && (field.getColumn() != 1)) {
				sb.append("\n");
			}
		};

		this.fields.stream().forEach(getFieldsValues);
		return sb.toString();
	}

	public void emptyGameboardPresentation() {
		System.out.println();
		for(int i = 1; i <= this.getTotalColumns(); i++) {
			if(i == 1) {
				System.out.print("     ");
			}
			System.out.print(" " + i + " ");
		}
		
		System.out.println("\n");
		
		for (int i = 1; i <= this.getTotalRows(); i++) {
			for (int j = 1; j <= this.getTotalColumns(); j++) {
				if(j == 1) {
					System.out.print(" " + i + "   ");
				}
				System.out.print(" ? ");				

				if (this.getTotalColumns() == j) {
					System.out.println();
				}
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i <= this.getTotalColumns(); i++) {
			if (i == 0) {
				sb.append("     ");
				continue;
			}
			sb.append(" " + i + " ");
		}

		sb.append("\n\n");

		Consumer<Field> getFieldsValues = field -> {
			if (field.getColumn() == 1) {
				sb.append(" " + field.getRow() + "   ");
			}

			sb.append(" ");
			sb.append(field.toString());
			sb.append(" ");

			if ((field.getColumn() == field.getGameboard().getTotalColumns()) && (field.getColumn() != 1)) {
				sb.append("\n");
			}
		};

		this.fields.stream().forEach(getFieldsValues);
		return sb.toString();
	}

	// Getters and Setters
	public int getMinMines() {
		return this.MIN_MINES;
	}

	public int getMaxMines() {
		return this.MAX_MINES;
	}

	public int getTotalMines() {
		return this.TOTAL_MINES;
	}

	public int getTotalRows() {
		return this.TOTAL_ROWS;
	}

	public int getTotalColumns() {
		return this.TOTAL_COLUMNS;
	}

	public int getTotalFields() {
		return this.TOTAL_FIELDS;
	}

	public boolean getGameRestarted() {
		return this.gameRestarted;
	}

	public void setGameRestarted(boolean isGameRestarted) {
		this.gameRestarted = isGameRestarted;
	}

}

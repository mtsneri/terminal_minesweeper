package br.com.mtsneri.terminalMinesweeper.model;

import java.util.ArrayList;
import java.util.List;

import br.com.mtsneri.terminalMinesweeper.exception.ExplosionException;
import br.com.mtsneri.terminalMinesweeper.exception.OpeningMarkedFieldException;

public class Field {

	// Instance Variables
	private final int ROW;
	private final int COLUMN;
	public List<Field> neighbors = new ArrayList<>();
	private Gameboard gameboard;
	private String VALUE;
	private boolean isMined = false;
	private boolean isOpened = false;
	private boolean isMarked = false;
	private boolean goalAchieved = false;
	
	// Constructor
	Field(Integer linha, Integer coluna) {
		this.ROW = linha;
		this.COLUMN = coluna;
	}

	// Instance Methods
	public void generateValue(int firstOpenedFieldRow, int firstOpenedFieldColumn) {
		boolean isTheFirstOpenedField = this.getRow() == firstOpenedFieldRow && this.getColumn() == firstOpenedFieldColumn;
		
		if (isTheFirstOpenedField) {
			this.setValue(" ");
			return;
		}

		if (this.isMarked()) {
			this.setValue("x");
		} else if (this.isOpened() && this.isMined()) {
			this.setValue("*");
		} else if (this.isOpened() && this.minesInTheNeighborhood() > 0) {
			this.setValue(Long.toString(this.minesInTheNeighborhood()));
		} else if (this.isOpened()) {
			this.setValue(" ");
		} else {
			this.setValue("?");
		}
	}

	public boolean open() {
		if (this.isOpened()) {
			return false;
		}

		if (this.isMarked()) {
			throw new OpeningMarkedFieldException();
		}

		if (this.isMined()) {
			this.setOpened(true);
			throw new ExplosionException();
		}

		this.setOpened(true);

		if (this.safeNeighborhood()) {
			this.neighbors.forEach(v -> v.open());
		}

		return true;
	}

	public boolean safeNeighborhood() {
		return this.neighbors.stream().noneMatch(v -> v.isMined());
	}

	public void mark() {
		if (this.isClosed()) {
			this.setMarked(true);
			return;
		}

		System.out.println("O campo está aberto, não é possível marcá-lo!");
	}

	public void markOff() {
		if (this.isClosed() && this.isMarked()) {
			this.setMarked(false);
			return;
		}

		System.out.println("O campo está aberto e/ou desmarcado, portanto não é possível desmarcá-lo");
	}

	public void toggleMarkup() {
		if (this.isClosed()) {
			this.setMarked(!this.isMarked());
		}
	}

	boolean addNeighbor(Field neighbor) {
		boolean isInRowRange = (neighbor.ROW >= this.ROW - 1 && neighbor.ROW <= this.ROW + 1);
		boolean isInColumnRange = (neighbor.COLUMN >= this.COLUMN - 1 && neighbor.COLUMN <= this.COLUMN + 1);
		boolean notMe = !(neighbor.ROW == this.ROW && neighbor.COLUMN == this.COLUMN);

		if (isInRowRange && isInColumnRange && notMe) {
			this.neighbors.add(neighbor);
			return true;
		}

		return false;
	}

	long minesInTheNeighborhood() {
		return this.neighbors.stream().filter(v -> v.isMined()).count();
	}

	void restart() {
		this.setMined(false);
		this.setOpened(false);
		this.setMarked(false);
		this.setGoalAchiev(false);
		this.setValue("");
	}

	public boolean goalAchieved() {
		boolean unraveled = !this.isMined() && this.isOpened();
		boolean secured = this.isMined() && this.isMarked();

		if (unraveled || secured) {
			this.setGoalAchiev(true);
		}
		return this.getGoalAchiev();
	}

	public String reveal() {

		if (this.isMined()) {
			this.setValue("*");
		} else if (this.minesInTheNeighborhood() > 0) {
			this.setValue(Long.toString(this.minesInTheNeighborhood()));
		} else {
			this.setValue(" ");
		}

		return this.getValue();
	}
	
	public String toString() {
		
		if (this.isMarked()) {
			this.setValue("x");
		} else if (this.isOpened() && this.isMined()) {
			this.setValue("*");
		} else if (this.isOpened() && this.minesInTheNeighborhood() > 0) {
			this.setValue(Long.toString(this.minesInTheNeighborhood()));
		} else if (this.isOpened()) {
			this.setValue(" ");
		} else {
			this.setValue("?");
		}

		return this.getValue();
	}

	// Getters and Setters
	public String getValue() {
		return this.VALUE;
	}

	public void setValue(String value) {
		this.VALUE = value;
	}

	public int getRow() {
		return this.ROW;
	}

	public int getColumn() {
		return this.COLUMN;
	}

	public boolean isMined() {
		return this.isMined;
	}

	public void setMined(boolean isMined) {
		this.isMined = isMined;
	}

	public boolean isOpened() {
		return this.isOpened;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public boolean isClosed() {
		return !this.isOpened;
	}

	public boolean isMarked() {
		return this.isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

	public Gameboard getGameboard() {
		return this.gameboard;
	}

	public void setGameboard(Gameboard gameboard) {
		this.gameboard = gameboard;
	}

	public void setGoalAchiev(boolean isGoalAchieved) {
		this.goalAchieved = isGoalAchieved;
	}

	public boolean getGoalAchiev() {
		return this.goalAchieved;
	}

}

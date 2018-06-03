/**
 * Tile - Handles the data used in a Minesweeper tile
 * 
 * @author Dylan Weber
 * @version 6/3/2018
 */
package game;

import graphics.TileGraphics;

public class Tile
{
	private boolean isRevealed;
	private boolean isMine;
	private int numberOfSurroundingMines;
	private int row;
	private int column;
	private TileGraphics graphics;
	private MinesweeperBoard parentBoard;
	
	//The image shown on top of the tile
	public enum FlagState
	{
		NONE, FLAG, QUESTION_MARK, FALSE_FLAG;
	}
	
	private FlagState flagState;
	
	public Tile(MinesweeperBoard parentBoard, int row, int column)
	{
		this.row = row;
		this.column = column;

		this.isRevealed = false;
		this.isMine = false;
		this.numberOfSurroundingMines = 0;
		this.flagState = FlagState.NONE;
		
		this.parentBoard = parentBoard;
		this.graphics = new TileGraphics(this);
	}
	
	public void resetTile()
	{
		setRevealed(false);
		setMine(false);
		setNumberOfSurroundingMines(0);
		setFlagState(FlagState.NONE);
	}
	
	public boolean isRevealed()
	{
		return isRevealed;
	}
	
	public void setRevealed(boolean isRevealed)
	{
		this.isRevealed = isRevealed;
		graphics.updateGraphics();
	}
	
	public boolean isMine()
	{
		return isMine;
	}
	
	public void setMine(boolean isMine)
	{
		this.isMine = isMine;
		graphics.updateGraphics();
	}

	public int getNumberOfSurroundingMines()
	{
		return numberOfSurroundingMines;
	}

	public void setNumberOfSurroundingMines(int numberOfSurroundingMines)
	{
		this.numberOfSurroundingMines = numberOfSurroundingMines;
		graphics.updateGraphics();
	}
	
	public TileGraphics getGraphics()
	{
		return graphics;
	}

	public int getRow()
	{
		return row;
	}

	public int getColumn()
	{
		return column;
	}

	public MinesweeperBoard getParentBoard()
	{
		return parentBoard;
	}

	public FlagState getFlagState()
	{
		return flagState;
	}

	public void setFlagState(FlagState flagState)
	{
		this.flagState = flagState;
		graphics.updateGraphics();
	}
}

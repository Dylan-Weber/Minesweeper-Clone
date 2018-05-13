package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controllers.UIManager;
import game.Tile.FlagState;

public class MinesweeperBoard
{
	private Tile[][] tiles;
	private UIManager controller;
	private int numberOfMines;
	private int revealedTiles;
	private boolean gameLost;
	
	public MinesweeperBoard(UIManager controller, int rows, int columns, int numberOfMines)
	{
		this.controller = controller;
		resetBoard(rows, columns, numberOfMines);
	}
	
	public void resetBoard(int rows, int columns, int numberOfMines)
	{
		int totalTiles =  rows * columns;
		
		if (numberOfMines > totalTiles)
		{
			//Caps the number of mines at the number of tiles
			numberOfMines = totalTiles;
		}
		
		this.numberOfMines = numberOfMines;
		this.revealedTiles = 0;
		this.gameLost = false;
		
		ArrayList<Tile> unchosenTiles = new ArrayList<>(totalTiles);

		//Checks if the board already exists to save memory
		if(tiles != null && rows == numberOfRows() && columns == numberOfColumns())
		{
			for (int i = 0; i < rows; i++)
			{
				for (int j = 0; j < columns; j++)
				{
					Tile currentTile = getTile(i, j);
					currentTile.resetTile();
					unchosenTiles.add(currentTile);
				}
			}
		}
		else
		{
			this.tiles = new Tile[rows][columns];

			for (int i = 0; i < rows; i++)
			{
				for (int j = 0; j < columns; j++)
				{
					setTile(i, j, new Tile(this, i, j));
					unchosenTiles.add(getTile(i, j));
				}
			}
		}
		
		Random mineChooser = new Random();
		for (int i = 0; i < numberOfMines; i++)
		{
			int tileNum = mineChooser.nextInt(unchosenTiles.size());
					
			//Removes a randomly chosen tile from the list and makes it a mine
			//Removal ensures it won't be chosen again.
			unchosenTiles.remove(tileNum).setMine(true);
		}
		
		//Sets the number of surrounding mines for each mine
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				getTile(i, j).setNumberOfSurroundingMines(countNumberOfSurroundingMines(i, j));
			}
		}
	}
	
	/**
	 * Reveals a tile clicked on by the player. If it reveals a mine, the player loses.
	 * Calls recursiveRevealTile() to reveal all surrounding tiles as well.
	 * @param row - The row of the tile being revealed
	 * @param column - The column of the tile being revealed
	 * @return - true if the tile is a mine. false if it isn't.
	 */
	public void revealTile(int row, int column)
	{
		Tile currentTile = getTile(row, column);

		if (currentTile != null && !currentTile.isRevealed())
		{
			if(revealedTiles == 0)
			{
				firstRevealCheck(row, column);
			}
			
			currentTile.setFlagState(FlagState.NONE);
			currentTile.setRevealed(true);
			if(!currentTile.isMine())
			{
				revealedTiles++;
			}
			
			if (!currentTile.isMine() && currentTile.getNumberOfSurroundingMines() == 0)
			{
				revealTile(row, column - 1);
				revealTile(row, column + 1);
				revealTile(row - 1, column);
				revealTile(row - 1, column - 1);
				revealTile(row - 1, column + 1);
				revealTile(row + 1, column);
				revealTile(row + 1, column - 1);
				revealTile(row + 1, column + 1);
			}
			
			if(currentTile.isMine())
			{
				gameLost = true;
			}
		}
	}
	
	/**
	 * Makes sure the player doesn't click on a mine on the first turn
	 */
	private void firstRevealCheck(int row, int column)
	{
		Tile currentTile = getTile(row, column);
		
		if(currentTile.isMine()) 
		{
			outer:
			for (int i = 0; i < numberOfRows(); i++)
			{
				for (int j = 0; j < numberOfColumns(); j++)
				{
					Tile loopTile = getTile(i, j);
					if(!(i == row && j == column) && !loopTile.isMine())
					{
						loopTile.setMine(true);
						currentTile.setMine(false);
						
						//Updates the clicked tiles and the surrounding tiles
						for(Tile tile : getSurroundingTiles(i, j))
						{
							if(tile != null)
							{
								tile.setNumberOfSurroundingMines(tile.getNumberOfSurroundingMines() + 1);
							}
						}
						
						for(Tile tile : getSurroundingTiles(row, column))
						{
							if(tile != null)
							{
								tile.setNumberOfSurroundingMines(tile.getNumberOfSurroundingMines() - 1);
							}
						}
						
						break outer;
					}
				}
			}
		}
	}
	
	private List<Tile> getSurroundingTiles(int row, int column)
	{
		ArrayList<Tile> surroundingTiles = new ArrayList<>();
		surroundingTiles.add(getTile(row, column - 1));
		surroundingTiles.add(getTile(row, column + 1));
		surroundingTiles.add(getTile(row - 1, column));
		surroundingTiles.add(getTile(row - 1, column - 1));
		surroundingTiles.add(getTile(row - 1, column + 1));
		surroundingTiles.add(getTile(row + 1, column));
		surroundingTiles.add(getTile(row + 1, column - 1));
		surroundingTiles.add(getTile(row + 1, column + 1));

		return surroundingTiles;
	}
	
	private int countNumberOfSurroundingMines(int row, int column)
	{
		int mineCount = 0;
		for(Tile tile: getSurroundingTiles(row, column))
		{
			if(tile != null && tile.isMine())
			{
				mineCount++;
			}
		}
		
		return mineCount;
	}
	
	public void revealAllTiles()
	{
		for (int i = 0; i < numberOfRows(); i++)
		{
			for (int j = 0; j < numberOfColumns(); j++)
			{
				Tile currentTile = getTile(i, j);
				currentTile.setRevealed(true);
				if(currentTile.getFlagState() == FlagState.FLAG )
				{
					if(!currentTile.isMine())
					{
						currentTile.setFlagState(FlagState.FALSE_FLAG);
					}
				}
				else
				{
					currentTile.setFlagState(FlagState.NONE);
				}
			}
		}
	}
	
	public int numberOfRows()
	{
		return tiles.length;
	}
	
	public int numberOfColumns()
	{
		return tiles[0].length;
	}
	
	public int numberOfTilesLeft()
	{
		return numberOfRows() * numberOfColumns() - revealedTiles;
	}
	
	/**
	 * @param row - the row of the tile
	 * @param column - the column of the tile
	 * @return - The tile at position (row, column) - if row or column are out of bounds, it returns null
	 */
	public Tile getTile(int row, int column)
	{
		if (row >= 0 && row < numberOfRows() && column >= 0 && column < numberOfColumns())
		{
			return tiles[row][column];
		}
		else
		{
			return null;
		}
	}
	
	private void setTile(int row, int column, Tile tile)
	{
		tiles[row][column] = tile;
	}
	
	public UIManager getController()
	{
		return controller;
	}
	
	public boolean gameLost()
	{
		return gameLost;
	}
	
	public boolean gameWon()
	{
		return numberOfMines == 0 || numberOfTilesLeft() <= numberOfMines;
	}
}

package controllers;

import game.MinesweeperBoard;
import graphics.TileGraphics;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UIManager
{
	public static final int DEFAULT_ROWS = 9;
	public static final int DEFAULT_COLUMNS = 9;
	public static final int DEFAULT_MINES = 10;
	
	@FXML GridPane mineGrid;
	@FXML Label rowsLabel, columnsLabel, minesLabel;
	@FXML Text winOrLossText;
	@FXML TextField rowsField, columnsField, numberOfMinesField;
	@FXML Button resetButton;
	
	MinesweeperBoard board;
	
	@FXML public void initialize()
	{
		rowsField.setText("" + DEFAULT_ROWS);
		columnsField.setText("" + DEFAULT_COLUMNS);
		numberOfMinesField.setText("" + DEFAULT_MINES);
		
		//Sets the column and row constraints of the grid to the size of a tile so that no overlaps or gaps appear.
		double size = TileGraphics.TILE_SIDE_LENGTH + TileGraphics.TILE_BORDER_WIDTH;
		for(ColumnConstraints col : mineGrid.getColumnConstraints())
		{
			col.setMinWidth(size);
			col.setMaxWidth(size);
		}
		
		for(RowConstraints row: mineGrid.getRowConstraints())
		{
			row.setMinHeight(size);
			row.setMaxHeight(size);
		}
		
		resetBoard(DEFAULT_ROWS, DEFAULT_COLUMNS, DEFAULT_MINES);
	}
	
	@FXML protected void handleResetButtonAction(ActionEvent event)
	{
		int rows = 0, columns = 0, numberOfMines = -1;
		
		try
		{
			rows = Integer.parseInt(rowsField.getText().trim());
		}
		catch(NumberFormatException e) { }
		
		if(rows <= 0)
		{
			rowsLabel.setTextFill(Color.RED);
		}
		else
		{
			rowsLabel.setTextFill(Color.BLACK);
		}
		
		try
		{
			columns = Integer.parseInt(columnsField.getText().trim());
		}
		catch(NumberFormatException e) { }
		
		if(columns <= 0)
		{			
			columnsLabel.setTextFill(Color.RED);
		}
		else
		{
			columnsLabel.setTextFill(Color.BLACK);
		}
		
		try
		{
			numberOfMines = Integer.parseInt(numberOfMinesField.getText().trim());
		}
		catch(NumberFormatException e) { }
		
		if(numberOfMines < 0)
		{
			minesLabel.setTextFill(Color.RED);
		}
		else
		{
			minesLabel.setTextFill(Color.BLACK);
		}
		
		if(rows > 0 && columns > 0 && numberOfMines >= 0)
		{
			resetBoard(rows, columns, numberOfMines);
		}
	}
	
	public void resetBoard(int rows, int columns, int numberOfMines)
	{
		winOrLossText.setVisible(false);
		
		if(board == null)
		{
			board = new MinesweeperBoard(this, rows, columns, numberOfMines);
		}
		else
		{
			board.resetBoard(rows, columns, numberOfMines);
		}
		
		mineGrid.getChildren().clear();
		
		for(int i = 0; i < board.numberOfRows(); i++)
		{
			for(int j = 0; j < board.numberOfColumns(); j++)
			{
				mineGrid.add(board.getTile(i, j).getGraphics(), j, i);
			}
		}
		
		checkForWinOrLoss();
	}
	
	public void revealTile(int row, int column)
	{
		board.revealTile(row, column);
		
		checkForWinOrLoss();
	}
	
	private void checkForWinOrLoss()
	{
		if(board.gameLost())
		{
			winOrLossText.setText("GAME OVER!");
			winOrLossText.setFill(Color.RED);
			winOrLossText.setVisible(true);
			board.revealAllTiles();
		}
		
		if(board.gameWon())
		{
			winOrLossText.setText("YOU WIN!");
			winOrLossText.setFill(Color.LIMEGREEN);
			winOrLossText.setVisible(true);
			board.revealAllTiles();
		}
	}
}

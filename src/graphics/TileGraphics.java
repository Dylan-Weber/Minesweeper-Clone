/**
 * TileGraphics - A graphical display of a tile
 * 
 * @author Dylan Weber
 * @version 6/3/2018
 */
package graphics;

import game.Tile;
import game.Tile.FlagState;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TileGraphics extends StackPane
{
	public static final double TILE_SIDE_LENGTH = 40.0;
	public static final double TILE_BORDER_WIDTH = 1.0;
	
	public static final ImagePattern FLAG_IMAGE_PATTERN = new ImagePattern(new Image("Flag.png"));
	public static final ImagePattern QUESTION_MARK_IMAGE_PATTERN = new ImagePattern(new Image("Question Mark.png"));
	public static final ImagePattern MINE_IMAGE_PATTERN = new ImagePattern(new Image("Mine.png"));
	public static final ImagePattern FALSE_FLAG_IMAGE_PATTERN = new ImagePattern(new Image("False Flag.png"));

	private Tile parent;
	private Rectangle boundary, imageFrame;
	private Text numberText;
	
	public TileGraphics(Tile parent)
	{
		this.parent = parent;

		//Defines the square that bounds the 
		boundary = new Rectangle(TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
		boundary.setStroke(Color.DARKGRAY);
		boundary.setStrokeWidth(TILE_BORDER_WIDTH);
		
		//Defines the square that holds the image. Subtracts the stroke width so that it doesn't overlap.
		imageFrame = new Rectangle(TILE_SIDE_LENGTH - boundary.getStrokeWidth(), TILE_SIDE_LENGTH - boundary.getStrokeWidth());
				
		numberText = new Text();
		numberText.setFont(Font.font("Trebuchet", FontWeight.NORMAL, 20));

		this.getChildren().addAll(boundary, numberText, imageFrame);

		this.updateGraphics();
		
		addMouseFunctions();
	}
	
	//Adds functions that occur on a left or right mouse click
	private void addMouseFunctions()
	{
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() 
		{
            @Override
            public void handle(MouseEvent event) {
        		if(!parent.isRevealed() && !parent.getParentBoard().gameWon() && !parent.getParentBoard().gameLost())
        		{
	            	if(event.getButton() == MouseButton.PRIMARY)
	            	{
	            		if(parent.getFlagState() != FlagState.FLAG && parent.getFlagState() != FlagState.QUESTION_MARK)
	            		{
	            			reveal();
	            		}
	            	}
	            	else //Secondary mouse button
	                {
		            		FlagState flagState = parent.getFlagState();
		                	switch(flagState)
		                	{
								case NONE:
			                		parent.setFlagState(FlagState.FLAG);
			                		break;
			                	case FLAG:
			                		parent.setFlagState(FlagState.QUESTION_MARK);
									break;
								case QUESTION_MARK:
									parent.setFlagState(FlagState.NONE);
									break;
								default:
									break;
		                	}
		                	updateGraphics();
	                }
        		}
            }
        });
	}
	
	public void reveal()
	{
		//Calls back up to the board so that surrounding tiles can be revealed
		parent.getParentBoard().getController().revealTile(parent.getRow(), parent.getColumn());
	}
	
	//Makes the TileGraphics look as it should according to the current state of its parent tile
	public void updateGraphics()
	{
		FlagState flagState = parent.getFlagState();
		int number = parent.getNumberOfSurroundingMines();
		numberText.setText("" + number);
		
		//The flag will stay on the board after a game over
		if(parent.isRevealed() && parent.getFlagState() != FlagState.FLAG)
		{
			if(parent.isMine())
			{
				numberText.setVisible(false);
				imageFrame.setVisible(true);
				imageFrame.setFill(MINE_IMAGE_PATTERN);
				if(parent.getParentBoard().gameWon())
				{
					boundary.setFill(Color.LIMEGREEN);
				}
				else
				{
					boundary.setFill(Color.RED);

				}
			}
			else
			{
				if(parent.getFlagState() == FlagState.FALSE_FLAG)
				{
					imageFrame.setVisible(true);
					imageFrame.setFill(FALSE_FLAG_IMAGE_PATTERN);
				}
				else if(number > 0)
				{
					numberText.setVisible(true);
					imageFrame.setVisible(false);
					switch(number)
					{
						case 1:
							numberText.setFill(Color.BLUE);
							break;
						case 2:
							numberText.setFill(Color.GREEN);
							break;
						case 3:
							numberText.setFill(Color.ORANGERED);
							break;
						case 4:
							numberText.setFill(Color.PURPLE);
							break;
						case 5:
							numberText.setFill(Color.MAROON);
							break;
						case 6:
							numberText.setFill(Color.TURQUOISE);
							break;
						case 7:
							numberText.setFill(Color.BLACK);
							break;
						case 8:
							numberText.setFill(Color.GRAY);
							break;
					}
				}
				else
				{
					imageFrame.setVisible(false);
				}
				boundary.setFill(Color.WHITE);
			}
		}
		else
		{
			numberText.setVisible(false);
			switch(flagState)
        	{
				case NONE:
					imageFrame.setVisible(false);
            		break;
            	case FLAG:
					imageFrame.setVisible(true);
					imageFrame.setFill(FLAG_IMAGE_PATTERN);
					break;
				case QUESTION_MARK:
					imageFrame.setVisible(true);
					imageFrame.setFill(QUESTION_MARK_IMAGE_PATTERN);
					break;
				default:
					break;
        	}
			
			boundary.setFill(Color.GRAY);
		}
	}
	
	public Text getNumberText()
	{
		return numberText;
	}
}

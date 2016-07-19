import java.awt.Color;
import java.awt.GridLayout;
import java.util.Stack;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Boggle_GUI extends Application{
	
	final int BOARD_ROWS = 4;
	final int BOARD_COLS = 4;
	
	int score;
	int timeLeft;
	int totalWords;
	int wordsSubmitted;
	
	String wordEntered;
	
	// Main Scene
	BorderPane mainLayoutPane;
	GridPane boardPane;
	HBox infoPane;
	VBox containerPane;
	
	Label textAreaLabel;
	Label timeLabel;
	Label scoreLabel;
	Label wordsSubmittedLabel;
	Label wordEnteredLabel;
	
	TextArea wordsSubmittedDisplay;
	
	GameBoard gameBoard;
	
	
	public Boggle_GUI(){
		//---
		score = 0;
		timeLeft = 0;
		wordsSubmitted = 0;
		totalWords = 0;
		wordEntered = "";
		
		// UI Components
		mainLayoutPane = new BorderPane();
		boardPane = new GridPane();
		infoPane = new HBox();
		containerPane = new VBox();
		
		textAreaLabel = new Label("Words Submitted:");
		timeLabel = new Label("Time: " + score);
		scoreLabel = new Label("Score: " + timeLeft);
		wordsSubmittedLabel = new Label("Words Entered: " + wordsSubmitted + "/" + totalWords);
		wordEnteredLabel = new Label("Word Entered: " + wordEntered);
		
		gameBoard = new GameBoard(boardPane);
		
		
		wordsSubmittedDisplay = new TextArea();
		
	}
	
	public static void main(String[] args){
		launch(args);
		
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Set Stage Settings
		primaryStage.setTitle("Boggle");	
		primaryStage.setScene(new Scene(mainLayoutPane, 600, 400));
		
		Trie trie = new Trie();
		trie.buildTrieFromFile("/Users/AlecGriffin/Projects/Eclipse/Boggle2/src/enable1.txt");
		
		// Event Trigger Upon User Pressing "ENTER" key 
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			
			

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					boolean validWord = trie.isWordPresent(wordEntered);
					if(validWord)
						wordsSubmittedDisplay.appendText(wordEntered + "\n");
					wordEntered = "";
					wordEnteredLabel.setText("Word Entered: ");
					gameBoard.resetBoard();
				}
				
			}
			
		});
		//Main Layout
		mainLayoutPane.setCenter(boardPane);
		mainLayoutPane.setLeft(containerPane);
		boardPane.setStyle("-fx-padding: 20px");
		
		//Info Pane
		infoPane.getChildren().addAll(timeLabel, scoreLabel, wordsSubmittedLabel);
		infoPane.setSpacing(15);
		timeLabel.setStyle("-fx-postion:relative ");
		
		//Container Pane
		containerPane.getChildren().addAll(textAreaLabel,wordEnteredLabel, wordsSubmittedDisplay, infoPane);
		wordsSubmittedDisplay.setPrefHeight(350);
		wordsSubmittedDisplay.setPrefWidth(200);

		// Show Stage
		primaryStage.show();
	}

	
	public class GameBoard{
		final String[][] LETTER_DISTRIBUTION_1 = {
				{  "AAEEGN",  
				   "ELRTTY", 
				   "AOOTTW",
				   "ABBJOO"},
				
				{  "EHRTVW",   
				   "CIMOTU", 
				   "DISTTY", 
				   "EIOSST"},
				
				{  "DELRVY",
				   "ACHOPS", 
				   "HIMNQU",  
				   "EEINSU"},  
				
				{"EEGHNW",   
				   "AFFKPS",   
				   "HLNNRZ",  
				   "DEILRX"}
		};
		
		Button[][] buttonArray;
		boolean[][] lettersSelectedArray; 
		final int COLUMN_OF_LETTERS_TO_USE = 0;
		GridPane boardPane;
		Stack<int[]> path;
		
		
		
		public GameBoard(GridPane boardPane){
			// Array Containing Button Objects
			buttonArray = new Button[BOARD_ROWS][BOARD_COLS];
			
			// boolean Array listing whether a button has been selected or not (Hmmm? Better Way to implement?)
			lettersSelectedArray = new boolean[BOARD_ROWS][BOARD_COLS];
			this.boardPane = (GridPane) boardPane;
			
			// Used in EventHadnling
			path = new Stack<int[]>();
			
			
			

			
			createButtons();
		}
		
		public void createButtons(){
			
			// Button Event Handler for Game Board
			EventHandler<ActionEvent> buttonEventHandler = new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Button buttonPressed = (Button) event.getSource();
					int[] rowAndCol = findButtonPosition(buttonArray, buttonPressed);
					int currentRow = rowAndCol[0];
					int currentCol = rowAndCol[1];
					
					if(path.isEmpty()){
						// First Letter
						buttonPressed.setStyle("-fx-background-color: yellow");
						path.push(rowAndCol);
						lettersSelectedArray[currentRow][currentCol] = true;
						wordEntered += buttonPressed.getText();
						wordEnteredLabel.setText("Word Entered: " + wordEntered);
					}else{
						int prevRow = path.peek()[0];
						int prevCol = path.peek()[1];
						if(checkIfButtonIsANeighbor(prevRow, prevCol, currentRow, currentCol) && !lettersSelectedArray[currentRow][currentCol]){
							System.out.println(path.peek()[0] + "---" + path.peek()[1]);
							buttonPressed.setStyle("-fx-background-color: yellow");
							path.push(rowAndCol);
							lettersSelectedArray[currentRow][currentCol] = true;
							wordEntered += buttonPressed.getText();
							wordEnteredLabel.setText("Word Entered: " + wordEntered);
						}
					}
					
//					FOR DEBUGGING:
//					System.out.println("Current Row: " + rowOfPressedButton + " Col: " + colOfPressedButton);
//					System.out.println("Previous --- Row: " + previousRow + " Col: " + previousCol);
//					System.out.println(checkIfButtonIsANeighbor(previousRow, previousCol, rowOfPressedButton, colOfPressedButton) + "\n");
				}
				
			};
			
			for(int row = 0; row < BOARD_ROWS; row++){
				for(int col = 0; col < BOARD_COLS; col++){
					
					Button b = new Button(LETTER_DISTRIBUTION_1[row][col].charAt(COLUMN_OF_LETTERS_TO_USE) + "");
					buttonArray[row][col] = b;
					
					// Set Button details
					b.setStyle("-fx-background-color: #DCDCDC; -fx-border-color: black; -fx-border-width: 1 1 1 ");	
					b.setOnAction(buttonEventHandler);
					b.setMinSize(75, 75);
					
					boardPane.add(b, row, col);
				}
			}
		}
		
		
		// Post: Returns true if the indexes of the currently pressed button are located adjacent to the previously pressed button
		private boolean checkIfButtonIsANeighbor(int previousRow, int previousCol, int currentRow, int currentCol){	
			
			for(int row = -1; row <= 1; row++){
				for(int col = -1; col <= 1; col++){
					
//						System.out.println(previousRow + row + " " + (previousCol + col));
						if((row != 0 || col != 0) && previousRow + row == currentRow && previousCol + col == currentCol){
					
							return true;
						}

				}
			}
					
			return false;
		}
		
		// Post: Given an Array of the ButtonGrid and the Button Pressed, Returns the location of the Button Pressed. 
		private int[] findButtonPosition(Button[][] buttonArray, Button buttonPressed){
			int[] rowAndCol = new int[2];
			
			for(int row = 0; row < buttonArray.length; row++){
				for(int col = 0; col < buttonArray.length; col++){
					if(buttonPressed.equals(buttonArray[row][col])){
						rowAndCol[0] = row;
						rowAndCol[1] = col;
						return rowAndCol;
					}
				}
			}
			
			return rowAndCol;
		}
		
		public void buildWordEntered(int row, int col){
			wordEntered += buttonArray[row][col].getText();
		}
		
		
		public void resetBoard(){
			path.clear();
			
			for(int row = 0; row < BOARD_ROWS; row++){
				for(int col = 0; col < BOARD_COLS; col++){
					buttonArray[row][col].setStyle("");
					lettersSelectedArray[row][col] = false;
				}
			}
		}

	}


}

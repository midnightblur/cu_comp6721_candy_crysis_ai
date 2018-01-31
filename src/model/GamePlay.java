package model;

import java.util.ArrayList;
import java.util.TreeMap;

import static model.Config.*;

public class GamePlay {
    private TreeMap<Character, Character> gameState;
    private char emptyCellChar;
    
    /**
     * Initialize a new game from an input string representing the initial state
     *
     * @param inputString the input string
     */
    public GamePlay(String inputString) {
        gameState = new TreeMap<>();
        emptyCellChar = Character.MIN_VALUE;
        
        ArrayList<Character> input = readInitialState(inputString);
        for (int i = 0; i < input.size(); i++) {
            char cellChar = GAME_RULES.getCellCharByIndex(i);
            if (input.get(i) == GAME_RULES.CANDY.e.getChar()) {
                emptyCellChar = cellChar;
            }
            gameState.put(cellChar, input.get(i));
        }
        drawGameState();
        System.out.println(isGoalState());
    }
    
    /**
     * Read the input string and return the array if candy characters
     * Validate all the candy character based on the game rules
     *
     * @param inputString the input string
     * @return the array of candy characters from the input string
     */
    private ArrayList<Character> readInitialState(String inputString) {
        // Validate the input & Break it into an array of characters
        ArrayList<Character> initialState = new ArrayList<>();
        while (!inputString.isEmpty()) {
            inputString = inputString.trim();
            char candyChar = inputString.charAt(0);
            inputString = inputString.substring(1);
            if (GAME_RULES.isValidCandyChar(candyChar)) {
                initialState.add(candyChar);
            } else {
                throw new IllegalArgumentException(GAME_RULES.INVALID_CANDY);
            }
        }
        
        // Return the valid array
        return initialState;
    }
    
    /**
     * Gets the current state of the board
     *
     * @return the state of the board
     */
    public TreeMap<Character, Character> getGameState() {
        return gameState;
    }
    
    /**
     * Draw the state of the board to console
     */
    private void drawGameState() {
        StringBuilder toDisplayString = new StringBuilder();
        int columnCount = 0;
        for (Character candy : gameState.values()) {
            if (columnCount == 5) {
                toDisplayString.append(System.lineSeparator());
                columnCount = 0;
            }
            if (candy == 'e') {
                candy = ' ';
            }
            toDisplayString.append(Character.toString(candy)).append(" ");
            columnCount++;
        }
        System.out.println(toDisplayString);
    }
    
    /**
     * Get the cell character that does not hold any candy
     *
     * @return the current empty cell character
     */
    private char getEmptyCellChar() {
        return emptyCellChar;
    }
    
    /**
     * Move the candy at the input cell to the empty cell if possible
     * Only move the candy if the move is valid
     *
     * @param cellChar the cell character that the moved candy is in
     */
    public void moveCandy(char cellChar) {
        if (GAME_RULES.isValidMove(cellChar, emptyCellChar)) {
            gameState.put(emptyCellChar, gameState.get(cellChar));
            gameState.put(cellChar, GAME_RULES.CANDY.e.getChar());
        }
    }
    
    /**
     * Check if the current state is the goal state
     *
     * @return true if the top and bottom row are identical, false otherwise
     */
    public boolean isGoalState() {
        for (int i = 0; i < 5; i++) {
            char topCellChar = (char)(i + 65);
            char bottomCellChar = (char)(i + 65 + GAME_RULES.GOAL_STATE_MATCHING_CELL_DISTANCE);
            if (gameState.get(topCellChar) != gameState.get(bottomCellChar)) {
                return false;
            }
        }
        return true;
    }
}

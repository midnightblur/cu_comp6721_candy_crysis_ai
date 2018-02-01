package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static model.Config.*;

public class GamePlay {
    private TreeMap<Character, Character> gameState;
    private ArrayList<Character> stepsTaken;
    private char emptyCellChar;
    
    /**
     * Initialize a new game from an input string representing the initial state
     *
     * @param inputString the input string
     */
    public GamePlay(String inputString) {
        gameState = new TreeMap<>();
        stepsTaken = new ArrayList<>();
        emptyCellChar = Character.MIN_VALUE;
        
        // Initialize the initial state
        ArrayList<Character> input = readInitialState(inputString);
        for (int i = 0; i < input.size(); i++) {
            char cellChar = GAME_RULES.getCellCharByIndex(i);
            if (input.get(i) == GAME_RULES.CANDY.e.getChar()) {
                emptyCellChar = cellChar;
            }
            gameState.put(cellChar, input.get(i));
        }
        
        // Display empty cell instead of character 'e'
        for (Map.Entry entry : gameState.entrySet()) {
            if ((char) entry.getValue() == GAME_RULES.CANDY.e.getChar()) {
                entry.setValue(' ');
                break;
            }
        }
    }
    
    /**
     * Read the input string and return the array if candy characters
     * Validate all the candy character based on the game rules
     *
     * @param inputString the input string
     *
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
    public void drawGameState() {
        System.out.println();
        String printFormat = "=====================" + System.lineSeparator() +
//                        "|   |   |   |   |   |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
//                        "|   |   |   |   |   |" + System.lineSeparator() +
                        "=====================" + System.lineSeparator() +
//                        "|   |   |   |   |   |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
//                        "|   |   |   |   |   |" + System.lineSeparator() +
                        "=====================" + System.lineSeparator() +
//                        "|   |   |   |   |   |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
//                        "|   |   |   |   |   |" + System.lineSeparator() +
                        "=====================" + System.lineSeparator();
        ArrayList<Character> cellChars = new ArrayList<>(gameState.keySet());
        ArrayList<Character> candyChars = new ArrayList<>(gameState.values());
        System.out.printf(printFormat,
                cellChars.get(0), cellChars.get(1), cellChars.get(2), cellChars.get(3), cellChars.get(4),
                candyChars.get(0), candyChars.get(1), candyChars.get(2), candyChars.get(3), candyChars.get(4),
                cellChars.get(5), cellChars.get(6), cellChars.get(7), cellChars.get(8), cellChars.get(9),
                candyChars.get(5), candyChars.get(6), candyChars.get(7), candyChars.get(8), candyChars.get(9),
                cellChars.get(10), cellChars.get(11), cellChars.get(12), cellChars.get(13), cellChars.get(14),
                candyChars.get(10), candyChars.get(11), candyChars.get(12), candyChars.get(13), candyChars.get(14)
        );
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
            gameState.put(cellChar, ' ');
            emptyCellChar = cellChar;
            stepsTaken.add(cellChar);
            printStepsTaken();
        } else {
            System.out.println("");
        }
    }
    
    /**
     * Check if the current state is the goal state
     *
     * @return true if the top and bottom row are identical, false otherwise
     */
    public boolean isGoalState() {
        for (int i = 0; i < 5; i++) {
            char topCellChar = (char) (i + 65);
            char bottomCellChar = (char) (i + 65 + GAME_RULES.GOAL_STATE_MATCHING_CELL_DISTANCE);
            if (gameState.get(topCellChar) != gameState.get(bottomCellChar)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Print out all the steps taken
     */
    public void printStepsTaken() {
        System.out.println(stepsTaken);
    }
}

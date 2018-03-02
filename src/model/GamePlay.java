package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static model.Config.*;

public class GamePlay {
    private TreeMap<Character, Character> gameState;
    private ArrayList<Character> stepsTaken;
    private char emptyCellChar;
    private HashMap<Character, Integer> candiesCount;
    
    /**
     * Initialize a new game from an input string representing the initial state
     *
     * @param inputString the input string
     */
    public GamePlay(String inputString) {
        gameState = new TreeMap<>();
        stepsTaken = new ArrayList<>();
        emptyCellChar = Character.MIN_VALUE;
        candiesCount = new HashMap<>();
        
        // Initialize the initial state
        ArrayList<Character> input = readInitialState(inputString);
        for (int i = 0; i < input.size(); i++) {
            char candyChar = input.get(i);
            char cellChar = GAME_RULES.getCellCharByIndex(i);
            if (input.get(i) == GAME_RULES.CANDY.e.getChar()) {
                emptyCellChar = cellChar;
            }
            gameState.put(cellChar, candyChar);
            candiesCount.put(candyChar, candiesCount.getOrDefault(candyChar, 0) + 1);
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
     * Draw the state of the board to console
     */
    public void drawGameState() {
        System.out.println();
        String printFormat = "=====================" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "=====================" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "=====================" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
                        "| %c | %c | %c | %c | %c |" + System.lineSeparator() +
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


    public ArrayList<Character> getStepsTaken() {
        return stepsTaken;
    }
    
    /**
     * Move the candy at the input cell to the empty cell if possible
     * Only move the candy if the move is valid
     *
     * @param cellChar the cell character that the moved candy is in
     */
    public boolean moveCandy(char cellChar) {
        // Users type in 'exit' or 'next'
        if (cellChar == Character.MIN_VALUE || cellChar == Character.MAX_VALUE)
            return true;
        
        if (GAME_RULES.isValidMove(cellChar, emptyCellChar)) {
            gameState.put(emptyCellChar, gameState.get(cellChar));
            gameState.put(cellChar, ' ');
            emptyCellChar = cellChar;
            stepsTaken.add(cellChar);
            printStepsTaken();
            return true;
        } else {
            return  false;
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
//        System.out.println(stepsTaken);
            for(Character c: stepsTaken){
                System.out.print(c);
            }
    }
    
    /**
     * Get the total number of candies of one kind
     *
     * @param candyChar the candy kind character
     * @return the number of candies of that kind
     */
    public int getCandyCount(char candyChar) {
        return candiesCount.getOrDefault(candyChar, 0);
    }
    
    /**
     * Get the candy at a cell
     *
     * @param cellChar the cell character
     * @return the candy character in the cell
     */
    public char getCandyAt(char cellChar) {
        return gameState.get(cellChar);
    }
    
    /**
     * Check if either the top or the bottom row is valid
     * A valid row is a row does not have more than half candies out of total of any kind
     *
     * @return 0 if now row is valid, 1 if top row is valid, 2 if bottom row is valid, 3 if both rows are valid
     */
    public int hasValidRow() {
        int result = 0;
        
        Map<Character, Integer> candiesCountTopRow = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            char cellChar = (char) (i + 65);
            char candyChar = getCandyAt(cellChar);
            candiesCountTopRow.put(candyChar, candiesCountTopRow.getOrDefault(candyChar, 0) + 1);
        }
        
        Map<Character, Integer> candiesCountBottomRow = new HashMap<>();
        for (int i = 10; i < 15; i++) {
            char cellChar = (char) (i + 65);
            char candyChar = getCandyAt(cellChar);
            candiesCountBottomRow.put(candyChar, candiesCountBottomRow.getOrDefault(candyChar, 0) + 1);
        }
        
        boolean isTopRowValid = isValidRow(candiesCountTopRow);
        boolean isBottomRowValid = isValidRow(candiesCountBottomRow);;
        
        if (isTopRowValid && isBottomRowValid)
            result = 3;
        else if (isTopRowValid)
            result = 1;
        else if (isBottomRowValid)
            result = 2;
        
        return result;
    }
    
    /**
     * Check if a row is valid given its candies count
     *
     * @param candiesCount candies count data
     * @return true if the data is valid, false otherwise
     */
    private boolean isValidRow(Map<Character, Integer> candiesCount) {
        for (char candyChar : candiesCount.keySet()) {
            int totalCandyCount = getCandyCount(candyChar);
            int candyCountInRow = candiesCount.get(candyChar);
            if (candyCountInRow > totalCandyCount / 2) {
                return false;
            }
        }
        return true;
    }
}

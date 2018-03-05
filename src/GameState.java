import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class GameState implements Comparable<GameState>, Serializable {
    private GameState parentState;
    private ArrayList<GameState> childStates;
    private int actualCostToReach;
    private int heuristicValue;
    private TreeMap<Character, Character> theBoard;
    private ArrayList<Character> stepsTaken;
    private char emptyCellChar;
    private HashMap<Character, Integer> candiesCount;
    
    /**
     * Initialize a new game from an input string representing the initial state
     *
     * @param inputString the input string
     */
    public GameState(String inputString) {
        parentState = null;
        childStates = new ArrayList<>();
        theBoard = new TreeMap<>();
        stepsTaken = new ArrayList<>();
        emptyCellChar = Character.MIN_VALUE;
        candiesCount = new HashMap<>();
        actualCostToReach = 0;
        heuristicValue = Integer.MAX_VALUE;
        
        // Initialize the initial state
        ArrayList<Character> input = readInitialState(inputString);
        for (int i = 0; i < input.size(); i++) {
            char candyChar = input.get(i);
            char cellChar = Config.GAME_RULES.getCellCharByIndex(i);
            if (input.get(i) == Config.GAME_RULES.CANDY.e.getChar()) {
                emptyCellChar = cellChar;
            }
            theBoard.put(cellChar, candyChar);
            candiesCount.put(candyChar, candiesCount.getOrDefault(candyChar, 0) + 1);
        }
        
        // Display empty cell instead of character 'e'
        for (Map.Entry entry : theBoard.entrySet()) {
            if ((char) entry.getValue() == Config.GAME_RULES.CANDY.e.getChar()) {
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
            if (Config.GAME_RULES.isValidCandyChar(candyChar)) {
                initialState.add(candyChar);
            } else {
                throw new IllegalArgumentException(Config.GAME_RULES.INVALID_CANDY);
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
        ArrayList<Character> cellChars = new ArrayList<>(theBoard.keySet());
        ArrayList<Character> candyChars = new ArrayList<>(theBoard.values());
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
    public char getEmptyCellChar() {
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
    public boolean moveCandyAt(char cellChar) {
        // Users type in 'exit' or 'next'
        if (cellChar == Character.MIN_VALUE || cellChar == Character.MAX_VALUE)
            return true;
        
        if (Config.GAME_RULES.isValidMove(cellChar, emptyCellChar)) {
            theBoard.put(emptyCellChar, theBoard.get(cellChar));
            theBoard.put(cellChar, ' ');
            emptyCellChar = cellChar;
            stepsTaken.add(cellChar);
            actualCostToReach++;
//            printStepsTaken();
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
            char bottomCellChar = (char) (i + 65 + Config.GAME_RULES.GOAL_STATE_MATCHING_CELL_DISTANCE);
            if (theBoard.get(topCellChar) != theBoard.get(bottomCellChar)) {
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
        return theBoard.get(cellChar);
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
    
    public int getActualCostToReach() {
        return actualCostToReach;
    }
    
    public double getHeuristicValue() {
        return heuristicValue;
    }
    
    public void setHeuristicValue(int heuristicValue) {
        this.heuristicValue = heuristicValue;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (char candyChar : theBoard.values()) {
            stringBuilder.append(candyChar).append(" ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
    
    public GameState getParentState() {
        return parentState;
    }
    
    public void setParentState(GameState parentState) {
        this.parentState = parentState;
    }
    
    public ArrayList<GameState> getChildStates() {
        return childStates;
    }
    
    public TreeMap<Character, Character> getTheBoard() {
        return theBoard;
    }
    
    public void addNewChild(GameState newChildState) {
        childStates.add(newChildState);
    }
    
    public int getChildCount() {
        return childStates.size();
    }
    
    public ArrayList<Character> getRow(int rowIndex) {
        int i = rowIndex * 5;
        ArrayList<Character> row = new ArrayList<>();
        for (int j = 65 + i; j < 65 + i + 5; j++) {
            char cellChar = (char) (j);
            row.add(theBoard.get(cellChar));
        }
        return row;
    }
    
    private void clearAllChild() {
        childStates.clear();
    }
    
    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param otherGameState the object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(GameState otherGameState) {
        int thisCost = this.heuristicValue + this.actualCostToReach;
        int otherCost = otherGameState.heuristicValue + otherGameState.actualCostToReach;
        if (thisCost != otherCost)
            return (thisCost - otherCost);
        
        return this.toString().compareTo(otherGameState.toString());
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (object instanceof GameState) {
            GameState otherGameState = (GameState) object;
            return (object.toString().compareTo(otherGameState.toString()) == 0);
        }
        
        return false;
    }
    
    public static GameState deepClone(GameState object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            GameState gameState = (GameState) ois.readObject();
            gameState.setParentState(null);
            gameState.clearAllChild();
            return gameState;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

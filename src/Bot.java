import java.util.*;

public class Bot {
    private OpenList openList;
    private HashSet<String> closedList;
    
    public Bot() {
        openList = new OpenList();
        closedList = new HashSet<>();
    }
    
    /**
     * Take the initial state of a game as input, draw the states space then use Heuristic to make move decisions
     *
     * @param rootGameState the initial game state
     */
    public GameState play(GameState rootGameState) {
        openList.addNewItem(rootGameState);
        
        while (!openList.isEmpty()) {
            GameState bestNewState = openList.pollFirstItem();
            if (bestNewState != null && !isAlreadyProcessed(bestNewState)) {
                if (!bestNewState.isGoalState()) {
                    processState(bestNewState);
                } else {
                    return bestNewState;
                }
            }
        }
        
        return null;
    }
    
    /**
     * From the given state, generate all possible new states and add them to the open list, sorted by their h(n) + g(n)
     *
     * @param gameState the game state
     */
    private void processState(GameState gameState) {
        closedList.add(gameState.toString());
        ArrayList<Character> validMovesList = Config.GAME_RULES.getCellsMovableTo(gameState.getEmptyCellChar());
        for (char move : validMovesList) {
            GameState childState = GameState.deepClone(gameState);
            if (childState != null) {
                childState.moveCandyAt(move);
                childState.setParentState(gameState);
                computeHeuristicValue(childState);
                gameState.addNewChild(childState);
                openList.addNewItem(childState);
            }
        }
    }
    
    /**
     * Check if a game state is processed before or not
     * There are many ways to get to a game state (different cost),
     * hence it is possible to have a game state multiple times in the open list
     *
     * @param gameState the game state
     * @return true if it is processed before, false otherwise
     */
    private boolean isAlreadyProcessed(GameState gameState) {
        return closedList.contains(gameState.toString());
    }
    
    public int computeHeuristicValue(GameState gameState) {
        int hValue = heuristic1(gameState);
        gameState.setHeuristicValue(hValue);
        return hValue;
    }
    
    /**
     * Player could move any tile to the empty slot
     * Heuristic value is the minimum # of tiles need to move to reach goal state
     *
     * @param gameState the game state
     * @return the heuristic value
     */
    private int heuristic1(GameState gameState) {
        int heuristicValue = 0;
        int hasValidRow = gameState.hasValidRow();
        switch (hasValidRow) {
            case 0:
                heuristicValue += 10;
                break;
            case 1:
            case 2:
            case 3: {
                ArrayList<Character> row0 = gameState.getRow(0);
                ArrayList<Character> row2 = gameState.getRow(2);
                for (int i = 0; i < row0.size(); i++) {
                    if (row0.get(i) != row2.get(i))
                        heuristicValue++;
                }
                break;
            }
        }
        return heuristicValue;
    }
}

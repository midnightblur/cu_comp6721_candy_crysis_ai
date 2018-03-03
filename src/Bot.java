import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Bot {
    private TreeSet<GameState> openList = new TreeSet<>();
    private Map<String, Boolean> processedBoard = new HashMap<>();
    /**
     * Take the initial state of a game as input, draw the states space then use Heuristic to make move decisions
     *
     * @param rootGameState the initial game state
     */
    public boolean play(GameState rootGameState) {
        openList.add(rootGameState);
        
        while (!openList.isEmpty()) {
            GameState bestNewState = openList.pollFirst();
            if (bestNewState != null && !isAlreadyProcessed(bestNewState)) {
                if (!bestNewState.isGoalState()) {
                    processState(bestNewState);
                    bestNewState.drawGameState();
                    System.out.println(bestNewState.toString());
                } else {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * From the given state, generate all possible new states and add them to the open list, sorted by their h(n) + g(n)
     *
     * @param gameState the game state
     */
    private void processState(GameState gameState) {
        processedBoard.putIfAbsent(gameState.toString(), true);
        ArrayList<Character> validMoves = Config.GAME_RULES.getCellsMovableTo(gameState.getEmptyCellChar());
        for (char moveChar : validMoves) {
            GameState childState = GameState.deepClone(gameState);
            if (childState != null) {
                childState.setParentState(gameState);
                childState.moveCandyAt(moveChar);
                gameState.addNewChild(childState);
                openList.add(childState);
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
        return processedBoard.getOrDefault(gameState.toString(), false);
    }
}

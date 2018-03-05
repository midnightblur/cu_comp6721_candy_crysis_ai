import java.util.HashMap;
import java.util.TreeSet;

public class OpenList {
    private HashMap<String, GameState> mapping; // Keep track of the best object for each game state
    private TreeSet<GameState> orderedSet; // The real open list
    
    public OpenList() {
        mapping = new HashMap<>();
        orderedSet = new TreeSet<>();
    }
    
    /**
     * Add a new game state to the data structure
     * Only add the given game state if there is no other game state
     * having the same board with lower (or equal) heuristic value
     *
     * @param gameState the game state
     * @return true if the game state is added, false otherwise
     */
    public boolean addNewItem(GameState gameState) {
        String gameStateString = gameState.toString();
        GameState currentState = mapping.getOrDefault(gameStateString, null);
        if (currentState == null) {
            addToList(gameState);
            return true;
        } else {
            double currentCost = currentState.getActualCostToReach() + currentState.getHeuristicValue();
            double newCost = gameState.getActualCostToReach() + gameState.getHeuristicValue();
            if (newCost < currentCost) {
                removeFromList(currentState);
                addToList(gameState);
                return true;
            }
            return false;
        }
    }
    
    /**
     * Remove and return the first element in the open list
     *
     * @return the first game state in the open list
     */
    public GameState pollFirstItem() {
        if (!orderedSet.isEmpty()) {
            GameState firstItem = orderedSet.pollFirst();
            assert firstItem != null;
            mapping.remove(firstItem.toString());
            return firstItem;
        }
        return null;
    }
    
    /**
     * Check if the list is empty
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return orderedSet.isEmpty();
    }
    
    private void addToList(GameState gameState) {
        mapping.put(gameState.toString(), gameState);
        orderedSet.add(gameState);
    }
    
    private void removeFromList(GameState gameState) {
        mapping.remove(gameState.toString());
        orderedSet.remove(gameState);
    }
}

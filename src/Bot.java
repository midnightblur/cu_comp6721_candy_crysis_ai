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
//            GameState childState = GameState.deepClone(gameState);
//            GameState childState = new GameState(gameState);
            GameState childState = gameState.clone();
//            childState.drawGameState();
            if (childState != null) {
                childState.moveCandyAt(move);
//                childState.setParentState(gameState);
                computeHeuristicValue(childState);
//                gameState.addNewChild(childState);
                openList.addNewItem(childState);
            }
            else{
                System.out.println("ChildState is null");
            }

        }
//        System.out.print(openList.getSize());
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
     * Player could move any tile to any where
     * Heuristic value is the minimum # of tiles need to move to reach goal state
     *
     * @param gameState the game state
     * @return the heuristic value
     */
    private int heuristic1(GameState gameState) {
        int heuristicValue = 0;
//        int hasValidRow = gameState.hasValidRow();
//        switch (hasValidRow) {
//            case 0:
//                heuristicValue += 10;
////                break;
//            case 1:
//            case 2:
//            case 3: {
//                ArrayList<Character> row0 = gameState.getRow(0);
//                ArrayList<Character> row2 = gameState.getRow(2);
//                for (int i = 0; i < row0.size(); i++) {
//                    if (row0.get(i) != row2.get(i))
//                        heuristicValue++;
//                }
//                break;
//            }
//        }

        // compare the Character in first row and third row one by one, if one of them is empty, search its neighbour,
        // if its neighbour can match the opposite row, then plus 1. else plus 2. If none of them are empty, plus 2 as well
        ArrayList<Character> row0 = gameState.getRow(0);
        ArrayList<Character> row1 = gameState.getRow(1);
        ArrayList<Character> row2 = gameState.getRow(2);
        for (int i = 0; i < row0.size(); i++) {
            if (row0.get(i) != row2.get(i)){
                if(row0.get(i) == ' '){
                    heuristicValue += getHeuristic(row0,row1,row2,i);
                }
                else if(row2.get(i) == ' '){
                    heuristicValue += getHeuristic(row2,row1,row0,i);
                }
                else{
                    heuristicValue += 2;
                }

            }
        }
        return heuristicValue;
    }

    /**
     * @param row0
     * @param row1
     * @param row2
     * @param i
     * @return
     */
    private int getHeuristic(ArrayList<Character> row0, ArrayList<Character> row1,ArrayList<Character> row2,int i){
        int heuristic = 0;
        if(i-1>=0 && row0.get(i-1) == row2.get(i))
            heuristic ++;
        else if(i+1<=4 && row0.get(i+1) == row2.get(i))
            heuristic ++;
        else if(row1.get(i) == row2.get(i))
            heuristic ++;
        else
            heuristic += 2;
        return heuristic;
    }
}

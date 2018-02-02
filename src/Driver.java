import model.Config;
import model.GamePlay;

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        while (true) {
            int playMode = getPlayMode();
            switch (playMode) {
                case 0:
                    return;
                case 1:
                    manualMode();
                    break;
                case 2:
                    aiMode();
                    break;
                default:
                    break;
            }
        }
    }
    
    private static void manualMode() {
        String inputString = "e r r r r r b w b b w y b r y";
        System.out.print("hello world2");
        GamePlay gamePlay = new GamePlay(inputString);
        while (!gamePlay.isGoalState()) {
            gamePlay.drawGameState();
            char cellToMove = getPlayerInstruction();
            if (cellToMove == Character.MIN_VALUE) {
                break;
            }
            
            gamePlay.moveCandy(cellToMove);
            if (gamePlay.isGoalState()) {
                System.out.println("You win!!!!");
                break;
            }
        }
    }
    
    private static void aiMode() {
    }
    
    /**
     * Let user choose the play mode
     *
     * @return the chosen play mode
     */
    private static int getPlayMode() {
        while (true) {
            displayMainMenu();
            
            Scanner scanner = new Scanner(System.in);
            int input;
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input == 0 || input == 1 || input == 2) {
                    return input;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
    
    /**
     * Display the main menu
     */
    private static void displayMainMenu() {
        System.out.println();
        System.out.println("====MAIN MENU====");
        System.out.println("0. Quit");
        System.out.println("1. Play manually");
        System.out.println("2. AI play");
        System.out.print("Your choice: ");
    }
    
    private static char getPlayerInstruction() {
        while (true) {
            System.out.print("Enter the cell to move: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.compareTo("exit") == 0) {
                return Character.MIN_VALUE;
            }
            else if (input.length() == 1) {
                return input.charAt(0);
            }
        }
    }
}

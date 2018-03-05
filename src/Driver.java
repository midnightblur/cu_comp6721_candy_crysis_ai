import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        while (true) {
            int playMode = getPlayMode();
            switch (playMode) {
                case 0:
                    return;
                case 1:
                    try {
                        manualMode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        aiMode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    private static void manualMode() throws IOException {
        ArrayList<String> inputStringArray = readFile();
        int numberOfMove = 0;
        int noGamesPlayed = 0;
        for (String inputString : inputStringArray) {
            noGamesPlayed++;
            long startTime = System.currentTimeMillis();
            GameState gameState = new GameState(inputString);
            System.out.println("=======================");
            System.out.println("======NEW  PUZZLE======");
            System.out.println("=======================");
            while (!gameState.isGoalState()) {
                boolean isValidMove = false;
                char cellToMove = 0;
                while (!isValidMove) {
                    gameState.drawGameState();
                    cellToMove = getPlayerInstruction();
                    isValidMove = gameState.moveCandyAt(cellToMove);
                    if (!isValidMove) {
                        System.out.print("Invalid input, please input again");
                    }
                }
                
                if (cellToMove == Character.MIN_VALUE) { // 'exit'
                    writeFile(null, 0);
                    return;
                } else if (cellToMove == Character.MAX_VALUE) { // 'next'
                    writeFile(null, 0);
                    break;
                }
                
                if (gameState.isGoalState()) {
                    System.out.println();
                    System.out.println("You win!!!!");
                    numberOfMove += gameState.getStepsTaken().size();
                    
                    // Only write the result to files if the puzzle is solved
                    long endTime = System.currentTimeMillis();
                    writeFile(gameState.getStepsTaken(), endTime - startTime);
                    
                    // when pass all the puzzles, write the steps to output.txt
                    if (noGamesPlayed == inputStringArray.size()) {
                        writeNumber(numberOfMove);
                    }
                    break;
                }
            }
        }
    }
    
    private static void aiMode() throws IOException {
        ArrayList<String> inputStringArray = readFile();
        int numberOfMove = 0;
        int noGamesPlayed = 0;
        for (String inputString : inputStringArray) {
            noGamesPlayed++;
            long startTime = System.currentTimeMillis();
            GameState gameState = new GameState(inputString);
            Bot bot = new Bot();
            GameState goalState = bot.play(gameState);
            
            if (goalState != null) {
                goalState.drawGameState();
                goalState.printStepsTaken();
                numberOfMove += goalState.getStepsTaken().size();
                
                // Only write the result to files if the puzzle is solved
                long endTime = System.currentTimeMillis();
                writeFile(goalState.getStepsTaken(), endTime - startTime);
                
                // when pass all the puzzles, write the steps to output.txt
                if (noGamesPlayed == inputStringArray.size()) {
                    writeNumber(numberOfMove);
                }
            }
        }
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
            System.out.print("Enter the cell to move (or 'exit or 'next'): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.compareTo("") != 0) {
                if (input.compareTo("exit") == 0) {
                    return Character.MIN_VALUE;
                } else if (input.compareTo("next") == 0) {
                    return Character.MAX_VALUE;
                } else {
                    return Character.toUpperCase(input.charAt(0));
                }
            }
        }
    }
    
    /**
     * read the input.txt and convert it to an String arraylist
     *
     * @return return the input string of initial input
     *
     * @throws IOException some files may not be found
     */
    private static ArrayList<String> readFile() throws IOException {
        InputStream inputStream = Driver.class.getResourceAsStream("Sample_Data.txt");
        ArrayList<String> inputStringArray = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            inputStringArray.add(line);
        }
        bufferedReader.close();
        return inputStringArray;
    }
    
    /**
     * write the output to output.txt
     *
     * @param stepsTaken the steps of move
     * @param time       the time you use to pass the puzzle
     *
     * @throws IOException some files may not be found
     */
    private static void writeFile(ArrayList<Character> stepsTaken, long time) throws IOException {
        File outputFile = new File("output.txt");
        outputFile.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile, true));
        if (stepsTaken != null && stepsTaken.size() > 0) {
            for (Character c : stepsTaken) {
                bufferedWriter.write(c);
            }
        } else {
            bufferedWriter.write("No step has taken");
        }
        
        bufferedWriter.newLine();
        bufferedWriter.write(String.valueOf(time) + "ms");
        bufferedWriter.newLine();
        bufferedWriter.close();
    }
    
    /**
     * write the total number of steps to output.txt
     *
     * @param steps the total number of steps to pass all puzzles
     *
     * @throws IOException some files may not be found
     */
    private static void writeNumber(int steps) throws IOException {
        File outputFile = new File("output.txt");
        outputFile.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile, true));
        bufferedWriter.write(String.valueOf(steps));
        bufferedWriter.newLine();
        bufferedWriter.close();
    }
}

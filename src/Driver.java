import model.Config;
import model.GamePlay;
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
                    aiMode();
                    break;
                default:
                    break;
            }
        }
    }
    
    private static void manualMode() throws IOException {
        ArrayList<String> inputStringArray = readFile();
        int numberOfMove = 0;
        for(String inputString: inputStringArray){
            long start = System.currentTimeMillis();
            GamePlay gamePlay = new GamePlay(inputString);
            System.out.println("=======================");
            System.out.println("======NEW  PUZZLE======");
            System.out.println("=======================");
            while (!gamePlay.isGoalState()) {
                gamePlay.drawGameState();
                char cellToMove = getPlayerInstruction();
                if (cellToMove == Character.MIN_VALUE) {
                    return;
                } else if (cellToMove == Character.MAX_VALUE) {
                    break;
                }

                boolean flag = gamePlay.moveCandy(cellToMove);

                while(!flag){
                    System.out.print("Invalid input, please input again");
                    gamePlay.drawGameState();
                    cellToMove = getPlayerInstruction();
                    if (cellToMove == Character.MIN_VALUE) {
                        break;
                    }
                    flag = gamePlay.moveCandy(cellToMove);
                }

                if (gamePlay.isGoalState()) {
                    System.out.println("You win!!!!");
                    numberOfMove += gamePlay.getStepsTaken().size();
                    break;
                }
            }
            long end = System.currentTimeMillis();
            writeFile(gamePlay.getStepsTaken(),end-start);

            // when pass all the puzzles, write the steps to output.txt
            if(inputString.equals(inputStringArray.get(inputStringArray.size()-1))){
                writeNumber(numberOfMove);
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
            System.out.print("Enter the cell to move (or 'exit or 'next'): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.compareTo("") != 0) {
                if (input.compareTo("exit") == 0) {
                    return Character.MIN_VALUE;
                } else if (input.compareTo("next") == 0) {
                    return Character.MAX_VALUE;
                }
                else {
                    return Character.toUpperCase(input.charAt(0));
                }
            }
        }
    }

    /**
     *    read the input.txt and convert it to an String arraylist
     * @return  return the input string of initial input
     * @throws IOException  some files may not be found
     */
    private static ArrayList<String > readFile() throws IOException {
//        File inputFile = new File("./input/Sample_Data.txt");
        InputStream inputStream = Driver.class.getResourceAsStream("Sample_Data.txt");
        ArrayList<String> inputStringArray = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String inputString = "e r r r r r b w b b w y b r y";
        String line = null;
        while((line = bufferedReader.readLine()) != null){
            inputStringArray.add(line);
        }
        bufferedReader.close();
        return inputStringArray;
    }

    /**
     *      write the output to output.txt
     * @param stepsTaken  the steps of move
     * @param time        the time you use to pass the puzzle
     * @throws IOException      some files may not be found
     */
    private static void writeFile(ArrayList<Character> stepsTaken,long time) throws IOException {
        File outputFile = new File("output.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile,true));
        for(Character c: stepsTaken){
            bufferedWriter.write(c);
        }
        bufferedWriter.newLine();
        bufferedWriter.write(String.valueOf(time)+"ms");
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    /**
     *      write the total number of steps to output.txt
     * @param steps  the total number of steps to pass all puzzles
     * @throws IOException  some files may not be found
     */
    private static void writeNumber (int steps) throws IOException {
        File outputFile = new File("output.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile,true));
        bufferedWriter.write(String.valueOf(steps));
        bufferedWriter.newLine();
        bufferedWriter.close();
    }
}

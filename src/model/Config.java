package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Config {
    public static class GAME_RULES {
        public enum CANDY {
            e('e'), r('r'), b('b'), w('w'), y('y'), g('g'), p('p');
            
            private char representedChar;
            CANDY(char representedChar) {this.representedChar = representedChar;}
            public char getChar() {return this.representedChar;}
        }
        
        public static final String INVALID_CELLS = "Cells letter range from A to O";
        public static final String INVALID_INDEX = "Cells index range from 0 to 14";
        public static final String INVALID_CANDY = "Invalid candy letter";
        public static final int GOAL_STATE_MATCHING_CELL_DISTANCE = 10;
        
        public static char getCellCharByIndex(int cellIndex) {
            if (cellIndex < 0 || cellIndex > 14) {
                throw new IllegalArgumentException(INVALID_INDEX);
            }
            
            return (char) (cellIndex + 65);
        }
        
        public static int getCellIndexByChar(char cellChar) {
            cellChar = Character.toUpperCase(cellChar);
            int cellIndex = (int) cellChar - 65;
            if (cellIndex < 0 || cellIndex > 14) {
                throw new IllegalArgumentException(INVALID_CELLS);
            }
            return cellIndex;
        }
        
        public static ArrayList<Character> getCellsMovableTo(char cellChar) {
            cellChar = Character.toUpperCase(cellChar);
            Character cells[];
            switch (cellChar) {
                case 'A':
                    cells = new Character[] {'B', 'F'};
                    break;
                case 'B':
                    cells = new Character[] {'A', 'G', 'C'};
                    break;
                case 'C':
                    cells = new Character[] {'B', 'H', 'D'};
                    break;
                case 'D':
                    cells = new Character[] {'C', 'I', 'E'};
                    break;
                case 'E':
                    cells = new Character[] {'D', 'J'};
                    break;
                case 'F':
                    cells = new Character[] {'A', 'G', 'K'};
                    break;
                case 'G':
                    cells = new Character[] {'B', 'F', 'L', 'H'};
                    break;
                case 'H':
                    cells = new Character[] {'C', 'G', 'M', 'I'};
                    break;
                case 'I':
                    cells = new Character[] {'D', 'H', 'N', 'J'};
                    break;
                case 'J':
                    cells = new Character[] {'E', 'I', 'O'};
                    break;
                case 'K':
                    cells = new Character[] {'F', 'L'};
                    break;
                case 'L':
                    cells = new Character[] {'K', 'G', 'M'};
                    break;
                case 'M':
                    cells = new Character[] {'L', 'H', 'N'};
                    break;
                case 'N':
                    cells = new Character[] {'M', 'I', 'O'};
                    break;
                case 'O':
                    cells = new Character[] {'N', 'J'};
                    break;
                default:
                    throw new IllegalArgumentException(INVALID_CELLS);
            }
            return new ArrayList<>(Arrays.asList(cells));
        }
    
        public static ArrayList<Character> getCellsMovableTo(int cellIndex) {
            char cellChar = getCellCharByIndex(cellIndex);
            return getCellsMovableTo(cellChar);
        }
        
        public static boolean isValidCandyChar(char candyChar) {
            CANDY candies[] = CANDY.values();
            for (CANDY candy : candies) {
                if (candyChar == candy.getChar()) {
                    return true;
                }
            }
            return false;
        }
        
        public static boolean isValidMove(char movedCell, char emptyCell) {
            ArrayList<Character> movableCells = getCellsMovableTo(emptyCell);
            return (movableCells.contains(movedCell));
        }
    }
}

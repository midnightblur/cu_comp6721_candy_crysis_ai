package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Config {
    public static class BOARD_CONFIG {
        private static final String INVALID_CELLS = "Cells letter range from A to O";
        private static final String INVALID_INDEX = "Cells index range from 0 to 14";
        
        public char getCellCharByIndex(int cellIndex) {
            if (cellIndex < 0 || cellIndex > 14) {
                throw new IllegalArgumentException(INVALID_INDEX);
            }
            
            return (char) (cellIndex + 65);
        }
        
        public int getCellIndexByChar(char cellChar) {
            cellChar = Character.toUpperCase(cellChar);
            int cellIndex = (int) cellChar - 65;
            if (cellIndex < 0 || cellIndex > 14) {
                throw new IllegalArgumentException(INVALID_CELLS);
            }
            return cellIndex;
        }
        
        public ArrayList<Character> getCellsMovableTo(char cellChar) {
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
    
        public ArrayList<Character> getCellsMovableTo(int cellIndex) {
            char cellChar = getCellCharByIndex(cellIndex);
            return getCellsMovableTo(cellChar);
        }
    }
}

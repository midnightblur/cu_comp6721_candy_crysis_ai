package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Config {
    public static class BOARD_CONFIG {
        private static final String INVALID_CELLS = "Cells letter range from A to O";
        private static final String INVALID_INDEX = "Cells index range from 0 to 14";
        
        public enum CELLS {
            A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7), I(8), J(9), K(10), L(11), M(12), N(13), O(14);
            
            private final int index;
            
            CELLS(int index) {
                this.index = index;
            }
            
            public int getIndex() {
                return index;
            }
        }
        
        public ArrayList<CELLS> getCellsMovableTo(CELLS emptyCell) {
            CELLS cells[];
            switch (emptyCell) {
                case A:
                    cells = new CELLS[] { CELLS.B, CELLS.F };
                    break;
                case B:
                    cells = new CELLS[] { CELLS.A, CELLS.G, CELLS.C };
                    break;
                case C:
                    cells = new CELLS[] { CELLS.B, CELLS.H, CELLS.D };
                    break;
                case D:
                    cells = new CELLS[] { CELLS.C, CELLS.I, CELLS.E };
                    break;
                case E:
                    cells = new CELLS[] { CELLS.D, CELLS.J };
                    break;
                case F:
                    cells = new CELLS[] { CELLS.A, CELLS.G, CELLS.K };
                    break;
                case G:
                    cells = new CELLS[] { CELLS.B, CELLS.F, CELLS.L, CELLS.H };
                    break;
                case H:
                    cells = new CELLS[] { CELLS.C, CELLS.G, CELLS.M, CELLS.I };
                    break;
                case I:
                    cells = new CELLS[] { CELLS.D, CELLS.H, CELLS.N, CELLS.J };
                    break;
                case J:
                    cells = new CELLS[] { CELLS.E, CELLS.I, CELLS.O };
                    break;
                case K:
                    cells = new CELLS[] { CELLS.F, CELLS.L };
                    break;
                case L:
                    cells = new CELLS[] { CELLS.K, CELLS.G, CELLS.M };
                    break;
                case M:
                    cells = new CELLS[] { CELLS.L, CELLS.H, CELLS.N };
                    break;
                case N:
                    cells = new CELLS[] { CELLS.M, CELLS.I, CELLS.O };
                    break;
                case O:
                    cells = new CELLS[] { CELLS.N, CELLS.J };
                    break;
                default:
                    throw new IllegalArgumentException(INVALID_CELLS);
            }
            return new ArrayList<>(Arrays.asList(cells));
        }
    }
}

package sudokosolver;

public class Sudoku {
    static int size ;
    static int empty = 0;

    public static boolean solveSudoku(int[][] sudoku) {
        size=sudoku.length;
        int row = -1;
        int col = -1;
        boolean isEmpty = true;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (sudoku[i][j] == empty) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }

        if (isEmpty) {
            return true;
        }

        for (int num = 1; num <= size; num++) {
            if (isSafe(sudoku, row, col, num)) {
                sudoku[row][col] = num;
                if (solveSudoku(sudoku)) {
                    return true;
                }
                sudoku[row][col] = empty;
            }
        }
        return false;
    }

    private static boolean isSafe(int[][] sudoku, int row, int col, int num) {
        int sub=(int)Math.sqrt(sudoku.length);
        return !usedInRow(sudoku, row, num) && !usedInCol(sudoku, col, num) && !usedInBox(sudoku, row - row % sub, col - col % sub, num,sub);
    }

    private static boolean usedInRow(int[][] sudoku, int row, int num) {
        for (int col = 0; col < size; col++) {
            if (sudoku[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInCol(int[][] sudoku, int col, int num) {
        for (int row = 0; row < size; row++) {
            if (sudoku[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInBox(int[][] sudoku, int boxStartRow, int boxStartCol, int num,int sub) {
        for (int row = 0; row < sub; row++) {
            for (int col = 0; col < sub; col++) {
                if (sudoku[row + boxStartRow][col + boxStartCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void printBoard(int[][] sudoku) {
        for (int i = 0; i <sudoku.length; i++) {
            for (int j = 0; j < sudoku[0].length; j++) {
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] sudoku = {
                {0,0,3,0},
                {0,0,1,0},
                {0,0,0,1},
                {3,0,2,0}
        };
//        int[][] sudoku={{1,2,0,3,0,4,0,5,6},
//                {7,0,0,0,0,6,0,0,1},
//                {0,0,0,0,0,0,0,0,0},
//                {0,8,0,4,0,9,0,2,0},
//                {0,0,0,0,6,0,0,0,0},
//                {0,3,0,5,0,1,0,8,0},
//                {0,0,0,0,0,0,0,0,0},
//                {9,0,0,2,0,0,0,0,8},
//                {8,4,0,6,0,7,0,1,9}};

        System.out.println("Sudoku puzzle:");
        printBoard(sudoku);

        if ( solveSudoku(sudoku)) {
            System.out.println("\nSolution:");
            printBoard(sudoku);
        } else {
            System.out.println("No solution exists.");
        }
    }
}



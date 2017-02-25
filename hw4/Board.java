import java.util.Stack;

/**
 * Created by wangxing on 2/19/17.
 */
public class Board {
    private int[][] blocks;
    private int n;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        int[][] copiedBlocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            copiedBlocks[i] = new int[n];
            for (int j = 0; j < n; j++) {
                copiedBlocks[i][j] = blocks[i][j];
            }
        }
        this.blocks = copiedBlocks;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int dist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int correct = i * n + j + 1;
                if (blocks[i][j] != 0 && blocks[i][j] != correct) dist++;
            }
        }
        return dist;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int currVal = blocks[i][j];
                if (currVal == 0) continue;
                int currValCorrectI = (currVal - 1) / n;
                int currValCorrectJ = (currVal - 1) % n;
                dist += Math.abs(i - currValCorrectI) + Math.abs(j - currValCorrectJ);
            }
        }
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        // exchange first two non-zero pairs
        int firstNonBlackI = -1;
        int firstNonBlackJ = -1;
        int secondNonBlackI = -1;
        int secondNonBlackJ = -1;
        boolean found = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0) {
                    if (firstNonBlackI == -1) {
                        firstNonBlackI = i;
                        firstNonBlackJ = j;
                    } else if (secondNonBlackI == -1) {
                        secondNonBlackI = i;
                        secondNonBlackJ = j;
                    } else {
                        found = true;
                        break;
                    }
                }
            }
            if (found) break;
        }

        return swapNew(firstNonBlackI, firstNonBlackJ, secondNonBlackI, secondNonBlackJ);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        return this.toString().equals(other.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // find blank board
        int blankBoardI = -1;
        int blankBoardJ = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    blankBoardI = i;
                    blankBoardJ = j;
                    break;
                }
            }
        }
        Stack<Board> neighbors = new Stack<Board>();
        if (blankBoardI - 1 >= 0 && blankBoardI - 1 < n) {
            neighbors.push(swapNew(blankBoardI, blankBoardJ, blankBoardI - 1, blankBoardJ));
        }
        if (blankBoardI + 1 >= 0 && blankBoardI + 1 < n) {
            neighbors.push(swapNew(blankBoardI, blankBoardJ, blankBoardI + 1, blankBoardJ));
        }
        if (blankBoardJ - 1 >= 0 && blankBoardJ - 1 < n) {
            neighbors.push(swapNew(blankBoardI, blankBoardJ, blankBoardI, blankBoardJ - 1));
        }
        if (blankBoardJ + 1 >= 0 && blankBoardJ + 1 < n) {
            neighbors.push(swapNew(blankBoardI, blankBoardJ, blankBoardI, blankBoardJ + 1));
        }
        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] arr0 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        test(arr0);
        int[][] arr1 = {{1, 2, 3}, {4, 0, 5}, {6, 7, 8}};
        test(arr1);
    }

    private Board swapNew(int i0, int j0, int i1, int j1) {
        // make a copy of blocks array
        int[][] copiedBlocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            copiedBlocks[i] = new int[n];
            for (int j = 0; j < n; j++) {
                copiedBlocks[i][j] = blocks[i][j];
            }
        }
        int tmp = copiedBlocks[i0][j0];
        copiedBlocks[i0][j0] = copiedBlocks[i1][j1];
        copiedBlocks[i1][j1] = tmp;
        return new Board(copiedBlocks);
    }

    private static void test(int[][] arr) {
        Board b = new Board(arr);
        System.out.println("b0:");
        System.out.println(b);
        System.out.println("b0's hamming distance: " + b.hamming());
        System.out.println("b0's manhattan distance: " + b.manhattan());
        System.out.println("b0's twin:");
        System.out.println(b.twin());
        System.out.println("b0's neighbors:");
        for (Board neigh : b.neighbors()) {
            System.out.println(neigh);
        }
        System.out.println();
    }
}

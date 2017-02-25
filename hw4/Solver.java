import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

/**
 * Created by wangxing on 2/19/17.
 */
public class Solver {
    private SearchNode answerNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Null initial board!");

        // For searching on original board
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(new SearchNodeComparator());
        SearchNode initNode = new SearchNode(initial, 0, null);
        minPQ.insert(initNode);

        // For searching on twin board
        MinPQ<SearchNode> twinMinPQ = new MinPQ<SearchNode>(new SearchNodeComparator());
        SearchNode twinInitNode = new SearchNode(initial.twin(), 0, null);
        twinMinPQ.insert(twinInitNode);

        while (true) {
            // Processing a step in original search
            SearchNode node = minPQ.delMin();
            if (node.board.isGoal()) {
                answerNode = node;
                break;
            } else {
                for (Board neigh : node.board.neighbors()) {
                    if (node.previous != null && neigh.equals(node.previous.board)) continue;
                    minPQ.insert(new SearchNode(neigh, node.numMoves + 1, node));
                }
            }

            // Processing a step in twin search
            SearchNode twinNode = twinMinPQ.delMin();
            if (twinNode.board.isGoal()) {
                // Twin got answer => there is no answer for original board
                break;
            } else {
                for (Board neigh : twinNode.board.neighbors()) {
                    if (twinNode.previous != null && neigh.equals(twinNode.previous.board)) continue;
                    twinMinPQ.insert(new SearchNode(neigh, twinNode.numMoves + 1, twinNode));
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return answerNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (answerNode != null) {
            return answerNode.numMoves;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (answerNode == null) return null;

        Stack<Board> steps = new Stack<Board>();
        SearchNode node = answerNode;
        while (node != null) {
            steps.push(node.board);
            node = node.previous;
        }
        return steps;
    }

    private static class SearchNode {
        public SearchNode(Board board, int numMoves, SearchNode previous) {
            this.board = board;
            this.numMoves = numMoves;
            this.previous = previous;
        }

        public Board board;
        public int numMoves;
        public SearchNode previous;
    }

    private static class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode n1, SearchNode n2) {
            int val1 = n1.numMoves + n1.board.manhattan();
            int val2 = n2.numMoves + n2.board.manhattan();
            if (val1 < val2) return -1;
            else if (val1 == val2) return 0;
            else return 1;
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufForIsFull;
    private boolean[] isOpenArr;
    private int topCellID;
    private int bottomCellID;
    private int dim;
    private int numOpened = 0;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be positive");
        }

        dim = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufForIsFull = new WeightedQuickUnionUF(n * n + 1);
        isOpenArr = new boolean[n * n];
        topCellID = n * n;
        bottomCellID = n * n + 1;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }

        int id = cellID(row, col);
        openSingle(id);

        int topID = cellID(row - 1, col);
        int bottomID = cellID(row + 1, col);
        int leftID = cellID(row, col - 1);
        int rightID = cellID(row, col + 1);
        if (topID != -1 && isOpenArr[topID]) {
            uf.union(id, topID);
            ufForIsFull.union(id, topID);
        }
        if (bottomID != -1 && isOpenArr[bottomID]) {
            uf.union(id, bottomID);
            ufForIsFull.union(id, bottomID);
        }
        if (leftID != -1 && isOpenArr[leftID]) {
            uf.union(id, leftID);
            ufForIsFull.union(id, leftID);
        }
        if (rightID != -1 && isOpenArr[rightID]) {
            uf.union(id, rightID);
            ufForIsFull.union(id, rightID);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);
        return isOpenArr[cellID(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowCol(row, col);
        return ufForIsFull.connected(topCellID, cellID(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpened;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(topCellID, bottomCellID);
    }

    /* ---------------------------------------------------------
     * Helper functions
     * --------------------------------------------------------- */

    private int cellID(int row, int col) {
        if (row < 1 || row > dim || col < 1 || col > dim) {
            return -1;
        }
        return (row - 1) * dim + (col - 1);
    }

    private void checkRowCol(int row, int col) {
        if (row < 1 || row > dim) {
            throw new IndexOutOfBoundsException("Row should between 1 and " + dim + " but is " + row);
        }
        if (col < 1 || col > dim) {
            throw new IndexOutOfBoundsException("Col should between 1 and " + dim + " but is " + col);
        }
    }

    private void openSingle(int id) {
        isOpenArr[id] = true;
        numOpened++;
        if (cellID(1, 1) <= id && id <= cellID(1, dim)) {
            uf.union(topCellID, id);
            ufForIsFull.union(topCellID, id);
        }
        if (cellID(dim, 1) <= id && id <= cellID(dim, dim)) {
            uf.union(bottomCellID, id);
        }
    }
}

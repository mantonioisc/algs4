import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    /**
     * With virtual top and bottom to ask whether percolates or not.
     */
    private WeightedQuickUnionUF uf;
    /**
     * Only with virtual top, to avoid backwash we won't connect this to virtual bottom. So a full block is the one that
     * connects to the top only on this one
     */
    private WeightedQuickUnionUF topOnlyUf;
    private int N;
    private int ufSize;
    /**
     * To keep track if one block is opened or not. Is not the same opened as being part of a group. For to be opened
     * a block has to be part of group, even a single member group. But the opposite is not true: an unopened block
     * should not be part of a group, even a single member group.
     */
    private boolean[] opened;

    /**
     * create N-by-N grid, with all sites blocked
     *
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be positive");
        }

        this.N = N;
        ufSize = N * N + 2; //1 virtual at the beginning and 1 at the end
        opened = new boolean[ufSize];
        uf = new WeightedQuickUnionUF(ufSize);
        topOnlyUf = new WeightedQuickUnionUF(ufSize);
    }

    /**
     * open site (row i, column j) if it is not open already
     *
     * @param i
     * @param j
     */
    public void open(int i, int j) {
        validate(i, j);

        int p = convert(i, j);
        opened[p] = true;

        //check if an adjacent block is opened, if the do join them
        //up(check if not in first row)
        if (i != 1) {
            int u = convert(i - 1, j);
            if (opened[u]) {
                uf.union(p, u);
                topOnlyUf.union(p, u);
            }
        } else {
            //in 1st row then we must link it with top virtual node in both UF data structures
            uf.union(p, 0);
            topOnlyUf.union(p, 0);
        }
        //down(check if not in last row)
        if (i != N) {
            int d = convert(i + 1, j);
            if (opened[d]) {
                uf.union(p, d);
                topOnlyUf.union(p, d);
            }
        } else {
            /*
            in last row then we must link it with bottom virtual node but only in the main UF data structure, we don't
            link in topOnlyUF to help us avoid backwash
             */
            uf.union(p, ufSize - 1);
        }

        //left(check if is in left border)
        if (j != 1) {
            int l = convert(i, j - 1);
            if (opened[l]) {
                uf.union(p, l);
                topOnlyUf.union(p, l);
            }
        }
        //right(check if in right border)
        if (j != N) {
            int r = convert(i, j + 1);
            if (opened[r]) {
                uf.union(p, r);
                topOnlyUf.union(p, r);
            }
        }
    }

    /**
     * is site (row i, column j) open?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isOpen(int i, int j) {
        validate(i, j);

        return opened[convert(i, j)];
    }

    /**
     * is site (row i, column j) full?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        validate(i, j);
        //if virtual top is connected to the point
        int q = convert(i, j);
        //only if I'm connected from top in topOnlyUF, this avoids backwash
        return topOnlyUf.connected(0, q);
    }

    /**
     * does the system percolate?
     *
     * @return
     */
    public boolean percolates() {
        /*
        check if virtual top and bottom are connected
        Internal range goes from 1 to N*N
        virtual are 0 and (N*N+1)
        because uf is 0 index based while Percolation is 1 index based
        */
        return uf.connected(0, ufSize - 1);
    }

    //PRIVATE METHODS
    private void validate(int i, int j) {
        if (i <= 0 || i > N) {
            throw new IndexOutOfBoundsException("Index i should be between 1 and N");
        }
        if (j <= 0 || j > N) {
            throw new IndexOutOfBoundsException("Index j should be between 1 and N");
        }
    }

    /**
     * Returns index on range 1 to NxN, perfectly excluding the 0 of the top virtual node
     * and avoiding the bottom virtual node which is (NxN + 1)
     *
     * @param i
     * @param j
     * @return
     */
    private int convert(int i, int j) {
        return (i - 1) * N + j;
    }

    public static void main(String[] args) {
        Percolation percolation2 = new Percolation(2);
        assert !percolation2.uf.connected(1, 3);
        percolation2.open(1, 1);
        assert percolation2.isOpen(1, 1);
        assert percolation2.isFull(1, 1);
        assert !percolation2.uf.connected(1, 3);
        assert !percolation2.percolates();

        assert !percolation2.isOpen(2, 1);
        assert !percolation2.isFull(2, 1);
        percolation2.open(2, 1);
        assert percolation2.isOpen(2, 1);
        assert percolation2.isFull(2, 1);
        assert percolation2.uf.connected(1, 3);
        assert percolation2.percolates();

        Percolation percolation10 = new Percolation(10);
        assert percolation10.convert(1, 1) == 1;
        assert percolation10.convert(1, 10) == 10;
        assert percolation10.convert(2, 1) == 11;
        assert percolation10.convert(5, 10) == 50;
        assert percolation10.convert(10, 9) == 99;
        assert percolation10.convert(10, 10) == 100;

        Percolation percolation20 = new Percolation(20);
        assert percolation20.convert(1, 1) == 1;
        assert percolation20.convert(1, 20) == 20;
        assert percolation20.convert(2, 1) == 21;
        assert percolation20.convert(5, 20) == 100;
        assert percolation20.convert(10, 20) == 200;
        assert percolation20.convert(15, 20) == 300;
        assert percolation20.convert(20, 20) == 400;
    }
}

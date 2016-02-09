import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int T;
    private double[] p;

    /**
     * perform T independent experiments on an N-by-N grid
     *
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be positive");
        }
        if (T <= 0) {
            throw new IllegalArgumentException("T should be positive");
        }

        this.T = T;
        p = new double[T];
        for (int t = 0; t < T; t++) {
            p[t] = monteCarloSimulationOfP(N);
        }
    }

    private double monteCarloSimulationOfP(int N) {
        Percolation percolation = new Percolation(N);
        int numberOfOpenSites = 0;
        while (!percolation.percolates()) {
            int i = StdRandom.uniform(1, N + 1);
            int j = StdRandom.uniform(1, N + 1);
            if (!percolation.isOpen(i, j)) {
                percolation.open(i, j);
                numberOfOpenSites++;
            }
        }
        return (double) numberOfOpenSites / (N * N);
    }

    /**
     * sample mean of percolation threshold
     *
     * @return
     */
    public double mean() {
        return StdStats.mean(p);
    }

    /**
     * sample standard deviation of percolation threshold
     *
     * @return
     */
    public double stddev() {
        return StdStats.stddev(p);
    }

    /**
     * low  endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(T);
    }

    /**
     * high endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(T);
    }

    /**
     * test client (described below)
     *
     * @param args
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(N, T);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}

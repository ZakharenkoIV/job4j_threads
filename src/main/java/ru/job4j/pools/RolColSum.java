package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static class Sums {
        private int rowSum = 0;
        private int colSum = 0;

        public Sums() {
        }

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        @Override
        public String toString() {
            return "rowSum = " + rowSum + ", colSum = " + colSum;
        }
    }

    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[Math.max(matrix.length, matrix[0].length)];
        for (int i = 0; i < sums.length; i++) {
            sums[i] = new Sums();
        }
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                sums[col].colSum += matrix[row][col];
                sums[row].rowSum += matrix[row][col];
            }
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        int length = Math.max(matrix.length, matrix[0].length);
        Sums[] sums = new Sums[length];
        for (int i = 0; i < length; i++) {
            futures.put(i, getTask(matrix, length, i));
        }
        for (Integer key : futures.keySet()) {
            sums[key] = futures.get(key).get();
        }
        return sums;
    }

    private static CompletableFuture<Sums> getTask(int[][] matrix, int length, int fix) {
        return CompletableFuture.supplyAsync(() -> {
            int rowSum = 0;
            int colSum = 0;
            for (int i = 0; i < length; i++) {
                rowSum += matrix[fix][i];
                colSum += matrix[i][fix];
            }
            return new Sums(rowSum, colSum);
        });
    }
}
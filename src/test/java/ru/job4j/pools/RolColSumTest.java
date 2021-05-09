package ru.job4j.pools;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RolColSumTest {
    int[][] matrix;
    RolColSum.Sums[] sums;

    @Before
    public void create() {
        matrix = new int[3][3];
        matrix[0][0] = 1;
        matrix[0][1] = 2;
        matrix[0][2] = 3;
        matrix[1][0] = 4;
        matrix[1][1] = 5;
        matrix[1][2] = 6;
        matrix[2][0] = 7;
        matrix[2][1] = 8;
        matrix[2][2] = 9;
        sums = new RolColSum.Sums[3];
        sums[0] = new RolColSum.Sums(6, 12);
        sums[1] = new RolColSum.Sums(15, 15);
        sums[2] = new RolColSum.Sums(24, 18);
    }

    @Test
    public void sum() {
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(Arrays.toString(result), is(Arrays.toString(sums)));
    }

    @Test
    public void asyncSum() throws ExecutionException, InterruptedException {
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        assertThat(Arrays.toString(result), is(Arrays.toString(sums)));
    }
}
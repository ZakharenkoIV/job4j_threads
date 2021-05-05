package ru.job4j;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParallelLookupTest {

    @Test
    public void indexIsFound() {
        Integer[] array = new Integer[35];
        for (int i = 0; i < 35; i++) {
            array[i] = i;
        }
        int result = new ParallelLookup<>(array, 34).indexOf();
        assertThat(result, is(34));
    }

    @Test
    public void indexNotFound() {
        Integer[] array = new Integer[35];
        for (int i = 0; i < 35; i++) {
            array[i] = i;
        }
        int result = new ParallelLookup<>(array, 40).indexOf();
        assertThat(result, is(-1));
    }
}
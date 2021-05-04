package ru.job4j;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelLookup<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T object;

    public ParallelLookup(T[] array, T object) {
        this.array = array;
        this.object = object;
    }

    @Override
    protected Integer compute() {
        if (array.length <= 10) {
            return linearSearch(object);
        }
        ParallelLookup<T> leftPart = new ParallelLookup<>(Arrays.copyOfRange(
                array, 0, array.length / 2), object);

        ParallelLookup<T> rightPart = new ParallelLookup<>(Arrays.copyOfRange(
                array, array.length / 2 + 1, array.length - 1), object);
        leftPart.fork();
        rightPart.fork();
        return Math.max(rightPart.join(), leftPart.join());
    }

    public int indexOf(T[] array, T object) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelLookup<>(array, object));
    }

    private int linearSearch(T t) {
        int result = -1;
        for (int i = 0; i <= 10; i++) {
            if (array[i].equals(t)) {
                result = i;
            }
        }
        return result;
    }
}

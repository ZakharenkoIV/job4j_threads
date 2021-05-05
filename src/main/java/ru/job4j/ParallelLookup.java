package ru.job4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelLookup<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T object;
    private final int start;
    private final int and;

    public ParallelLookup(T[] array, T object) {
        this.array = array;
        this.object = object;
        this.start = 0;
        this.and = array.length - 1;
    }

    public ParallelLookup(T[] array, T object, int start, int and) {
        this.array = array;
        this.object = object;
        this.start = start;
        this.and = and;
    }

    @Override
    protected Integer compute() {
        if (and - start <= 10) {
            return linearSearch(object, start, and);
        }
        int mid = (start + and) / 2;
        ParallelLookup<T> leftPart = new ParallelLookup<>(array, object, start, mid);
        ParallelLookup<T> rightPart = new ParallelLookup<>(array, object, mid + 1, and);
        leftPart.fork();
        rightPart.fork();
        return Math.max(rightPart.join(), leftPart.join());
    }

    public int indexOf() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelLookup<>(array, object));
    }

    private int linearSearch(T object, int start, int and) {
        int result = -1;
        for (int i = start; i <= and; i++) {
            if (array[i].equals(object)) {
                result = i;
                break;
            }
        }
        return result;
    }
}

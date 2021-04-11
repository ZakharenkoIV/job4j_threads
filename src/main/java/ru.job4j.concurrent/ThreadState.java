package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName() + " -- do work")
        );
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName() + " -- do work")
        );
        printInfo(first);
        printInfo(second);
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                || second.getState() != Thread.State.TERMINATED) {
            printInfo(first);
            printInfo(second);
        }
        printInfo(first);
        printInfo(second);
        System.out.println("work completed");
    }

    private static void printInfo(Thread thread) {
        System.out.println(thread.getName() + " -- " + thread.getState());
    }
}
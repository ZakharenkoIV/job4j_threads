package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName() + " -- do work")
        );
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName() + " -- do work")
        );
        System.out.println(first.getName() + " -- " + first.getState());
        System.out.println(second.getName() + " -- " + first.getState());
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                && second.getState() != Thread.State.TERMINATED) {
            System.out.println(first.getName() + " -- " + first.getState());
            System.out.println(second.getName() + " -- " + second.getState());
        }
        System.out.println(first.getName() + " -- " + first.getState());
        System.out.println(second.getName() + " -- " + second.getState());
        if (first.getState() == Thread.State.TERMINATED
                && second.getState() == Thread.State.TERMINATED) {
            System.out.println("work completed" + Thread.activeCount());
        }
    }
}
package ru.job4j.patterns;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private int capacity = 10;
    private int size = 0;

    public SimpleBlockingQueue() {
    }

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void offer(T value) {
        while (size == capacity) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (queue.offer(value)) {
            size++;
            this.notifyAll();
        }
    }

    public synchronized T poll() {
        while (size == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        T result = queue.poll();
        size--;
        this.notifyAll();
        return result;
    }

    public static void main(String[] args) {
        SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>();
        Thread consumer = new Thread(() -> {
            String s = queue.poll();
        });
        Thread producer = new Thread(() -> {
            String s = "s";
            queue.offer(s);
        });
        producer.start();
        consumer.start();
    }
}

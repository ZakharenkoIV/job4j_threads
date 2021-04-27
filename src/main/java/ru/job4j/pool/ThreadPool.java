package ru.job4j.pool;

import ru.job4j.buffer.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>();

    public ThreadPool() {
        int size = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < size; i++) {
            threads.add(createConsumer());
            threads.get(i).start();
        }
    }

    public void work(Runnable job) {
        new Thread(job).start();
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool();
        for (int i = 1; i < 21; i++) {
            int finalI = i;
            pool.work(() -> System.out.println("work №" + finalI));
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        pool.shutdown();
    }

    private Thread createConsumer() {
        return new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    tasks.poll().run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}


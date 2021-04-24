package ru.job4j.buffer;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleBlockingQueueTest {
    private SimpleBlockingQueue<Integer> queue;
    private Thread producer;
    private Thread consumer;

    @Before
    public void create() {
        queue = new SimpleBlockingQueue<>();
        producer = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(2);
                queue.offer(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumer = new Thread(() -> {
            try {
                System.out.println(queue.poll());
                System.out.println(queue.poll());
            //    System.out.println(queue.poll());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void poll() throws InterruptedException {
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(3, is(queue.poll()));
    }
}
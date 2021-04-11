package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("tmp.xml")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead = 0;
            while (bytesRead != -1) {
                long startDownload = System.currentTimeMillis();
                bytesRead = in.read(dataBuffer, 0, 1024);
                int actualSpeed = (int) ((System.currentTimeMillis() - startDownload) / 1000);
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                try {
                    Thread.sleep(difference(speed, actualSpeed));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int difference(int requiredSpeed, int actualSpeed) {
        return requiredSpeed < actualSpeed ? 0 : ((requiredSpeed - actualSpeed) * 1000);
    }
}
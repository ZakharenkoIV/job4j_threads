package ru.job4j.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditFile {

    private volatile File file;

    public EditFile(File file) {
        this.file = file;
    }

    public synchronized void saveContent(String content) throws IOException {
        try (BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < content.length(); i += 1) {
                buffer.write(content.charAt(i));
            }
        }
    }
}

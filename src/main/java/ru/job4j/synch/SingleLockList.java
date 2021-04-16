package ru.job4j.synch;

import net.jcip.annotations.ThreadSafe;

import java.io.*;
import java.util.Iterator;
import java.util.List;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    private final List<T> list;

    public SingleLockList(List<T> list) {
        this.list = copy(list);
    }

    public synchronized void add(T value) {
        list.add(value);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return copy(this.list).iterator();
    }

    @SuppressWarnings(value = "unchecked")
    private List<T> copy(List<T> list) {
        List<T> newList = list;
        try {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 ObjectOutputStream objectStream = new ObjectOutputStream(out)) {
                objectStream.writeObject(list);
                try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                    newList = (List<T>) new ObjectInputStream(in).readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newList;
    }
}
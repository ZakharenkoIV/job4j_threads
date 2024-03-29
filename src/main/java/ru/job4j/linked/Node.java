package ru.job4j.linked;

public class Node<T> {
    private final Node<T> next;
    private final T value;

    public Node(Node<T> next, T value) {
        this.next = next;
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }

    public Node<T> copy(Node<T> next, T value) {
        return new Node<>(next, value);
    }
}
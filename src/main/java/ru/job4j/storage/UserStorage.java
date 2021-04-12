package ru.job4j.storage;

import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

@ThreadSafe
public class UserStorage {
    private final List<User> list;

    public UserStorage() {
        this.list = new ArrayList<>();
    }

    public synchronized boolean add(User user) {
        return list.add(new User(user.getId(), user.getAmount()));
    }

    public synchronized boolean update(User user) {
        boolean result = false;
        for (User u : list) {
            if (user.getId() == u.getId()) {
                list.set(list.indexOf(u), new User(user.getId(), user.getAmount()));
                result = true;
                break;
            }
        }
        return result;
    }

    public synchronized boolean delete(User user) {
        return list.remove(user);
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        if (list.get(fromId).getAmount() >= amount) {
            fulfillYourContract(fromId, amount, (x, y) -> x - y);
            fulfillYourContract(toId, amount, Integer::sum);
            result = true;
        }
        return result;
    }

    private void fulfillYourContract(int id, int amount, BinaryOperator<Integer> binaryOperator) {
        list.get(id).setAmount(binaryOperator.apply(list.get(id).getAmount(), amount));
    }
}


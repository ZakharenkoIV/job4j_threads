package ru.job4j.storage;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

@ThreadSafe
public class UserStorage {
    private final Map<Integer, User> map;

    public UserStorage() {
        this.map = new HashMap<Integer, User>();
    }

    public synchronized boolean add(User user) {
        map.put(user.getId(), user.clone());
        return map.get(user.getId()).equals(user);
    }

    public synchronized boolean update(User user) {
        map.replace(user.getId(), user.clone());
        return map.get(user.getId()).equals(user);
    }

    public synchronized boolean delete(User user) {
        return map.remove(user.getId(), user);
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        if (map.get(fromId).getAmount() >= amount) {
            fulfillYourContract(fromId, amount, (x, y) -> x - y);
            fulfillYourContract(toId, amount, Integer::sum);
            result = true;
        }
        return result;
    }

    private void fulfillYourContract(int id, int amount, BinaryOperator<Integer> binaryOperator) {
        map.get(id).setAmount(binaryOperator.apply(map.get(id).getAmount(), amount));
    }
}


package ru.job4j.storage;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class UserStorage {
    private final Map<Integer, User> map = new HashMap<>();

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
        User sender = map.get(fromId);
        User recipient = map.get(toId);
        if (sender != null && recipient != null && sender.getAmount() >= amount) {
            sender.setAmount(sender.getAmount() - amount);
            recipient.setAmount(recipient.getAmount() + amount);
            result = true;
        }
        return result;
    }
}


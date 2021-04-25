package ru.job4j.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) != null;
    }

    public boolean update(Base model) {
        memory.computeIfAbsent(model.getId(), (id) -> {
            if (memory.get(id).getVersion() != model.getVersion()) {
                throw new OptimisticException("Versions are not equal");
            }
            Base newModel = new Base(model.getId(), model.getVersion() + 1);
            newModel.setName(model.getName());
            return newModel;
        });
        return true;
    }

    public void delete(Base model) {
        memory.remove(model.getId(), model);
    }

    public static class OptimisticException extends RuntimeException {
        public OptimisticException(String message) {
            super(message);
        }
    }
}
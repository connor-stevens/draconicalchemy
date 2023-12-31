package com.cleanroommc.draconicalchemy.util;

import java.util.*;

public class RandomCollection<E> {
    private final NavigableMap<Integer, E> randomMap = new TreeMap<Integer, E>();
    private final Map<E, Integer> items = new HashMap<>();
    private boolean updated = false;
    private int total = 0;

    public RandomCollection() {}

    public void add(int weight, E result) {
        items.put(result, weight);
        updated = false;
    }

    public void remove(E item) {
        items.remove(item);
        updated = false;
    }

    public void update() {
        for (Integer key : randomMap.keySet()) {
            randomMap.remove(key);
        }
        total = 0;
        for (E item : items.keySet()) {
            int weight = items.get(item);
            if (weight > 0) {
                total += weight;
                randomMap.put(total, item);
            }
        }
        updated = true;
    }

    public int getTotal() {
        if (!updated) {
            update();
        }
        return total;
    }

    public E next(Random random) {
        try {
            if (!updated) {
                update();
            }
            int value = random.nextInt(total);
            return randomMap.higherEntry(value).getValue();
        } catch (Exception e) {
            return null;
        }
    }

    public Map<E, Integer> getMap() {
        return items;
    }

    public Integer getWeight(E key) {
        return items.getOrDefault(key, 0);
    }
}

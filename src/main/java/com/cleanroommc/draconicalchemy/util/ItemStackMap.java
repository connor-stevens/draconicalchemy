package com.cleanroommc.draconicalchemy.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

//Do not use for anything performance important
//Also naming is really dumb
public class ItemStackMap<E> {
    List<ItemStack> keys;
    List<E> items;

    public ItemStackMap() {
        keys = new ArrayList<>();
        items = new ArrayList<>();
    }

    public void put(ItemStack key, E item) {
        if (keys.size() != 0) {
            for (int i = 0; i < keys.size(); i++) {
                if (key.isItemEqual(keys.get(i))) {
                    items.set(i, item);
                    return;
                }
            }
        }

        keys.add(key);
        items.add(item);
    }

    public E get(ItemStack key) {
        if (keys.size() != 0) {
            for (int i = 0; i < keys.size(); i++) {
                if (key.isItemEqual(keys.get(i))) {
                    return items.get(i);
                }
            }
        }
        return null;
    }

    public E getOrDefault(ItemStack key, E def) {
        E ret = get(key);
        if (ret == null) {
            return def;
        } else {
            return ret;
        }
    }

    public List<ItemStack> getKeys() {
        return keys;
    }

    public List<E> getItems() {
        return items;
    }
}

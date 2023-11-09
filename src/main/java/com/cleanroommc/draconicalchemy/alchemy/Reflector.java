package com.cleanroommc.draconicalchemy.alchemy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class Reflector {
    Block block;
    int capacity;

    public Reflector(Block block, int capacity) {
        this.block = block;
        this.capacity = capacity;
    }
}

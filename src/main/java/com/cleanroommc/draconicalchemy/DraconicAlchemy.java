package com.cleanroommc.draconicalchemy;

import com.cleanroommc.draconicalchemy.alchemy.AlchemyRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid=DraconicAlchemy.MODID)
public class DraconicAlchemy {
    static final String MODID = "draconicalchemy";

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        AlchemyRegistry.registerBlastWave(AlchemyRegistry.CHAOTICBLASTWAVE);
        AlchemyRegistry.CHAOTICBLASTWAVE.depositions.add(10, Blocks.IRON_BLOCK.getDefaultState());
        AlchemyRegistry.CHAOTICBLASTWAVE.depositions.add(20, Blocks.GOLD_BLOCK.getDefaultState());
    }
}

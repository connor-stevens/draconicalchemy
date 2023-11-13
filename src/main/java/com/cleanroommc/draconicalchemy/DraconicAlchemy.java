package com.cleanroommc.draconicalchemy;

import com.cleanroommc.draconicalchemy.alchemy.AlchemyRegistry;
import com.cleanroommc.draconicalchemy.alchemy.BlastWave;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.http.cookie.SM;

@Mod(modid=DraconicAlchemy.MODID)
public class DraconicAlchemy {
    static final String MODID = "draconicalchemy";

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        AlchemyRegistry.registerBlastWave(AlchemyRegistry.CHAOTICBLASTWAVE);
        BlastWave SMELTERY = new BlastWave("Smelting", true);
        AlchemyRegistry.registerBlastWave(SMELTERY);
        for (IBlockState state : Blocks.FURNACE.getBlockState().getValidStates()) {
            AlchemyRegistry.CHAOTICBLASTWAVE.addConversion(state, 50, SMELTERY);
        }
        SMELTERY.depositions.add(1, Blocks.AIR.getDefaultState());
        SMELTERY.addReaction(Blocks.IRON_ORE.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), 5);
        AlchemyRegistry.CHAOTICBLASTWAVE.depositions.add(1, Blocks.LAVA.getDefaultState());
        AlchemyRegistry.CHAOTICBLASTWAVE.depositions.add(200, Blocks.AIR.getDefaultState());
    }
}

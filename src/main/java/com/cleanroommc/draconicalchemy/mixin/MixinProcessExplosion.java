package com.cleanroommc.draconicalchemy.mixin;

import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.utils.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProcessExplosion.class)
public abstract class MixinProcessExplosion {
    @Shadow
    private IBlockState lavaState;

    @Inject(method = "<init>", at = @At("RETURN"))
    protected void overrideFluid(BlockPos origin, int radius, WorldServer world, int minimumDelayTime, CallbackInfo ci) {
        lavaState = Blocks.WATER.getDefaultState();
        LogHelper.info("Injection successful, altered deposited fluid");
    }

}

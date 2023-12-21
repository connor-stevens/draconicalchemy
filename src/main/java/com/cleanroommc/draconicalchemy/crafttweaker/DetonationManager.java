package com.cleanroommc.draconicalchemy.crafttweaker;

import com.brandon3055.brandonscore.handlers.ProcessHandler;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.cleanroommc.draconicalchemy.alchemy.BlastWave;
import com.cleanroommc.draconicalchemy.mixin.MixinProcessExplosion;
import com.cleanroommc.draconicalchemy.util.IAimable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IFacing;
import crafttweaker.api.world.IWorld;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.draconicalchemy.DetonationManager")
public class DetonationManager implements IAction {
    public IWorld iWorld;
    public IBlockPos iBlockPos;
    public int range;

    public float direction = -1f;
    public float accuracy = defaultAccuracy;
    public static float defaultAccuracy = 1f;
    public BlastWave blastWave;

    public DetonationManager(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range, IFacing facing, float accuracy) {
        this.iWorld = iWorld;
        this.blastWave = blastWave;
        this.iBlockPos = iBlockPos;
        this.range = range;
        if (facing == null) {
            direction = -1;
        } else {
            this.direction = CraftTweakerMC.getFacing(facing).getHorizontalAngle();
        }
        this.accuracy = accuracy;
    }

    public DetonationManager(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range, float direction, float accuracy) {
        this.iWorld = iWorld;
        this.blastWave = blastWave;
        this.iBlockPos = iBlockPos;
        this.range = range;
        this.direction = (float) ((direction * Math.PI)/ 180f);
        if (this.direction < 0) {
            this.direction += Math.PI * 2;
        }
        this.accuracy = accuracy;
    }

    public DetonationManager(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range) {
        this(iWorld, blastWave, iBlockPos, range, null, 0);
    }

    @ZenMethod
    public static void detonate(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range, IFacing iFacing) {
        IAction action = new DetonationManager(iWorld, blastWave, iBlockPos, range, iFacing, defaultAccuracy);
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void detonate(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range, IFacing iFacing, float accuracy) {
        IAction action = new DetonationManager(iWorld, blastWave, iBlockPos, range, iFacing, accuracy);
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void detonate(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range, float direction) {
        IAction action = new DetonationManager(iWorld, blastWave, iBlockPos, range, direction, defaultAccuracy);
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void detonate(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range, float direction, float accuracy) {
        IAction action = new DetonationManager(iWorld, blastWave, iBlockPos, range, direction, accuracy);
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void detonate(IWorld iWorld, BlastWave blastWave, IBlockPos iBlockPos, int range) {
        IAction action = new DetonationManager(iWorld, blastWave, iBlockPos, range);
        CraftTweakerAPI.apply(action);
    }

    @Override
    public void apply() {
        if (iWorld.isRemote()) {
            CraftTweakerAPI.logError("This function (DetonationManager.detonate()) must be executed on the server");
            return;
        }
        WorldServer worldServer = (WorldServer) CraftTweakerMC.getWorld(iWorld);

        BlockPos blockPos = CraftTweakerMC.getBlockPos(iBlockPos);

        ProcessExplosion processExplosion = new ProcessExplosion(blockPos, range, worldServer, 0);

        IAimable aiming = (IAimable) processExplosion;

        if (direction > 0) {
           aiming.directBlast(direction, accuracy);
        }

        aiming.setBlastWave(blastWave);

        ProcessHandler.addProcess(processExplosion);
    }

    @Override
    public String describe() {
        return "Detonation Attempt";
    }
}

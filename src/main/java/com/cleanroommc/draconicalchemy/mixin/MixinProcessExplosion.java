package com.cleanroommc.draconicalchemy.mixin;

import com.brandon3055.brandonscore.lib.DelayedExecutor;
import com.brandon3055.brandonscore.lib.ShortPos;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.SimplexNoise;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.lib.ExplosionHelper;
import com.brandon3055.draconicevolution.network.PacketExplosionFX;
import com.brandon3055.draconicevolution.utils.LogHelper;
import com.cleanroommc.draconicalchemy.alchemy.AlchemyRegistry;
import com.cleanroommc.draconicalchemy.alchemy.BlastWave;
import com.cleanroommc.draconicalchemy.alchemy.Polarisation;
import com.cleanroommc.draconicalchemy.alchemy.Transmutation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Mixin(value = ProcessExplosion.class, remap = false)
public abstract class MixinProcessExplosion {

    @Shadow
    public static DamageSource fusionExplosion;

    /**
     * The origin of the explosion.
     */
    @Shadow
    @Final
    public Vec3D origin;
    @Shadow
    @Final
    private WorldServer world;
    @Shadow
    @Final
    private MinecraftServer server;
    @Shadow
    @Final
    private int minimumDelay;
    @Shadow
    public double[] angularResistance;
    @Shadow
    public boolean isDead = false;
    @Shadow
    public int radius = 0;
    @Shadow
    public int maxRadius;
    @Shadow
    public double circumference = 0;
    @Shadow
    public double meanResistance = 0;
    @Shadow
    protected boolean calculationComplete = false;
    @Shadow
    protected boolean detonated = false;
    @Shadow
    protected long startTime = -1;
    @Shadow
    protected long calcWait = 0;
    @Shadow
    public boolean lava = true;
    @Shadow
    public HashSet<Integer> blocksToUpdate = new HashSet<>();
    @Shadow
    public LinkedList<HashSet<Integer>> destroyedBlocks = new LinkedList<>();
    @Shadow
    public HashSet<Integer> lavaPositions = new HashSet<>();
    @Shadow
    public HashSet<Integer> destroyedCache = new HashSet<>();
    @Shadow
    public HashSet<Integer> scannedCache = new HashSet<>();
    @Shadow
    public ShortPos shortPos;
    @Shadow
    private IBlockState lavaState;

    public HashSet<Integer>[] depositionPosition;
    public HashSet<Integer>[] reactionPosition;
    private int[][] angularBlastWaveConversion;
    private int[] blastWaveConversionBuffer;
    private int[] activeAngularWaves;
    private int activeWaveID;

    private BlastWave activeWave;

    @Inject(method = "<init>", at = @At("RETURN"))
    protected void overrideFluid(BlockPos origin, int radius, WorldServer world, int minimumDelayTime, CallbackInfo ci) {
        LogHelper.dev("Constructor successful");
        angularBlastWaveConversion = new int[121][AlchemyRegistry.numWaves()];
        blastWaveConversionBuffer = new int[AlchemyRegistry.numWaves()];
        depositionPosition = new HashSet[AlchemyRegistry.numWaves()];
        for (int i = 0; i < AlchemyRegistry.numWaves(); i++) {
            depositionPosition[i] = new HashSet<>();
        }
       reactionPosition = new HashSet[AlchemyRegistry.numWaves()];
        for (int i = 0; i < AlchemyRegistry.numWaves(); i++) {
            reactionPosition[i] = new HashSet<>();
        }
        activeAngularWaves = new int[121];
    }
    /**
     * @author yodamax
     * @reason Used for testing, to avoid time delay
     */
    //@Overwrite
    @Shadow
    public void updateProcess() {
        server.currentTime = MinecraftServer.getCurrentTimeMillis();
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }

        if (calcWait > 0) {
            calcWait--;
            return;
        }

        if (!calculationComplete) {
            long t = System.currentTimeMillis();
            updateCalculation();
            t = System.currentTimeMillis() - t;
            calcWait = t / 40;
            LogHelper.dev("Calculation Progress: " + Utils.round((((double) radius / (double) maxRadius) * 100D), 100) + "% " + (Runtime.getRuntime().freeMemory() / 1000000));
            if (calcWait > 0) {
                LogHelper.dev("Explosion Calc loop took " + t + "ms! Waiting " + calcWait + " ticks before continuing");
            }
        }
        else if (minimumDelay == -1) {
            isDead = true;
        }
        else {
            //Remove delay for test
            if (/*(System.currentTimeMillis() - startTime) / 1000 >= minimumDelay*/ true) {
                detonate();
            }
        }
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateCalculation() {
        BlockPos originPos = origin.getPos();

        double maxCoreHeight = 20D * (maxRadius / 150D);

        boolean debug = true;

        Vec3D posVecUp = new Vec3D();
        Vec3D posVecDown = new Vec3D();
        for (int x = originPos.getX() - radius; x < originPos.getX() + radius; x++) {
            for (int z = originPos.getZ() - radius; z < originPos.getZ() + radius; z++) {
                double dist = Utils.getDistanceAtoB(x, z, originPos.getX(), originPos.getZ());
                if (dist < radius && dist >= radius - 1) {
                    posVecUp.set(x + 0.5, origin.y, z + 0.5);
                    double radialAngle = getRadialAngle(posVecUp);
                    double radialResistance = getRadialResistance(radialAngle);
                    double angularLoad = (meanResistance / radialResistance) * 1;
                    double radialPos = 1D - (radius / (double) maxRadius);
                    double coreFalloff = Math.max(0, (radialPos - 0.8) * 5);
                    coreFalloff = 1 - ((1 - coreFalloff) * (1 - coreFalloff) * (1 - coreFalloff));
                    double coreHeight = coreFalloff * maxCoreHeight;
                    double edgeNoise = Math.max(0, (-radialPos + 0.2) * 5);
                    double edgeScatter = edgeNoise * world.rand.nextInt(10);
                    double sim = SimplexNoise.noise(x / 50D, z / 50D);
                    edgeNoise = 1 + (Math.abs(sim) * edgeNoise * 8);

                    //set the active blast wave
                    activeWaveID = getActiveBlastWave(radialAngle);
                    activeWave = AlchemyRegistry.waveTypes.get(activeWaveID);

                    double power = (10000 * radialPos * radialPos * radialPos * angularLoad * edgeNoise) + edgeScatter;
                    double heightUp = 20 + ((5D + (radius / 10d)) * angularLoad);
                    double heightDown = coreHeight + ((5D + (radius / 10d)) * angularLoad * (1 - coreFalloff));
                    heightDown += (Math.abs(sim) * 4) + world.rand.nextDouble();
                    heightUp += (Math.abs(sim) * 4) + world.rand.nextDouble();

                    posVecDown.set(posVecUp);
                    double resist = trace(posVecUp, power/* * (1 + 8 * radialPos)*/, (int) heightUp * 3, 1, 0, 0);
                    resist += trace(posVecDown.subtract(0, 1, 0), power, (int) heightDown, -1, 0, 0);
                    resist *= 1 / angularLoad;


                    if (radialPos < 0.8) {
                        addRadialResistance(radialAngle, resist);
                    }

                    if (debug) {
                        LogHelper.dev("Radius: " + radius + " meanresist: " + meanResistance + " radialresist: " + radialResistance + " heightdown: " + heightDown + " angularLoad: " + angularLoad);
                        debug = false;
                    }
                }
             }
        }

        recalcResist();
        radius++;
        circumference = 2 * Math.PI * radius;

        destroyedBlocks.add(destroyedCache);
        destroyedCache = new HashSet<>();
        scannedCache = new HashSet<>();

        if (radius >= maxRadius) {
            LogHelper.dev("Explosion Calculation Completed!");
            calculationComplete = true;
        }
    }


    //region Math Stuff

    @Shadow
    private void recalcResist() {};

    @Shadow
    public abstract double getRadialResistance(double radialPos);

    @Shadow
    public abstract void addRadialResistance(double radialPos, double power);

    //Experiment that that was never actually used.
    private void smoothResist(int range) {
        double[] smoothing = new double[121];
        for (int i = 0; i < 121; i++) {
            for (int j = -range; j < range; j++) {
                smoothing[i] += angularResistance[Math.floorMod((i+j), 121)];
            }
            smoothing[i] /= 2 * range + 1;
        }
        for (int i = 0; i < 121; i++) {
            angularResistance[i] = smoothing[i];
        }
    }

    @Shadow
    public abstract double getRadialAngle(Vec3D pos);

    //Get the index of the active blast wave
    public int getActiveBlastWave(double radialPos) {
        int min = MathHelper.floor(radialPos);
        if (min >= angularResistance.length) {
            min -= angularResistance.length;
        }
        int max = MathHelper.ceil(radialPos);
        if (max >= angularResistance.length) {
            max -= angularResistance.length;
        }

        double delta = radialPos - min;
        if (world.rand.nextDouble() > delta) {
            return activeAngularWaves[max];
        } else {
            return activeAngularWaves[min];
        }
    }

    public void addBlastWaveConversionFactor(double radialPos, int waveType, int power) {
        int min = MathHelper.floor(radialPos);
        if (min >= angularResistance.length) {
            min -= angularResistance.length;
        }
        int max = MathHelper.ceil(radialPos);
        if (max >= angularResistance.length) {
            max -= angularResistance.length;
        }

        double delta = radialPos - min;

        angularBlastWaveConversion[min][waveType] += angularBlastWaveConversion[min][waveType] + power * (1-delta);
        if (angularBlastWaveConversion[min][waveType] > 100) {
            angularBlastWaveConversion[min][waveType] = 0;
            activeAngularWaves[min] = waveType;
        }

        angularBlastWaveConversion[max][waveType] += angularBlastWaveConversion[max][waveType] + power * delta;
        if (angularBlastWaveConversion[max][waveType] > 100) {
            angularBlastWaveConversion[max][waveType] = 0;
            activeAngularWaves[max] = waveType;
        }
    }

    //endregion
    /**
     * @author Yodamax
     * @reason Addon alters behaviour of Draconic Evolution explosions.
     */
    @Overwrite
    private double trace(Vec3D posVec, double power, int dist, int traceDir, double totalResist, int travel) {
        if (dist > 100) {
            dist = 100;
        }
        if (dist > power) {
            dist = (int)Math.round(power);
        }
        if (dist <= 0 || power <= 0 || posVec.y < 0 || posVec.y > 255) {
            return totalResist;
        }

        dist--;
        travel++;
        Integer iPos = shortPos.getIntPos(posVec);

        if (scannedCache.contains(iPos) || destroyedCache.contains(iPos)) {
            posVec.add(0, traceDir, 0);
            return trace(posVec, power, dist, traceDir, totalResist, travel);
        }

        BlockPos pos = posVec.getPos();

        double r = 1;

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!block.isAir(state, world, pos)) {
            Material mat = state.getMaterial();
            double effectivePower = (power / 10) * ((double) dist / (dist + travel));

            r = block.getExplosionResistance(null);

            if (activeWave.converters.containsKey(state)) {
                Polarisation polarisation = activeWave.converters.get(state);
                addBlastWaveConversionFactor(getRadialAngle(posVec), polarisation.output.id, polarisation.power);
            }


            if (effectivePower >= r) {
                //Add deposition to depositions list
                if ((dist == 1 || power < (r / radius) / travel) && traceDir == -1 && !world.isAirBlock(pos.down()) ) {
                    dist = 0;
                    //destroyedCache.remove(iPos);
                    depositionPosition[activeWaveID].add(iPos);
                    blocksToUpdate.add(iPos);
                    scannedCache.add(iPos);
                } else {
                    destroyedCache.add(iPos);
                }

            }
            else if (mat == Material.WATER || mat == Material.LAVA) {
                if (effectivePower > 5) {
                    destroyedCache.add(iPos);
                }
                else {
                    blocksToUpdate.add(iPos);
                }
                r = 10;
            }
            else {
                if (mat == Material.LAVA || mat == Material.WATER || block instanceof BlockLiquid || block instanceof IFluidBlock || block instanceof BlockFalling) {
                    blocksToUpdate.add(iPos);
                }
                scannedCache.add(iPos);
            }

            if (activeWave.reactions.containsKey(state)) {
                reactionPosition[activeWaveID].add(iPos);
                destroyedCache.remove(iPos);
            }

            if (activeWave.immunities.contains(state)) {
                destroyedCache.remove(iPos);
            }

            if (r > 1000) {
                r = 1000;
            }
        }
        else {
            scannedCache.add(iPos);
        }

        r = (r / radius) / travel;//?

        totalResist += r;
        power -= r;



        posVec.add(0, traceDir, 0);
        return trace(posVec, power, dist, traceDir, totalResist, travel);
    }


    @Shadow
    public abstract boolean isCalculationComplete();


    /**
     * @author Yodamax
     * @reason Overwrite detonation behaviour to introduce new behaviour.
     */
    @Overwrite
    public boolean detonate() {
        if (!isCalculationComplete() || detonated) {
            return false;
        }

        long l = System.currentTimeMillis();

        LogHelper.dev("Removing Blocks!");
        LogHelper.startTimer("Adding Blocks For Removal");

        ExplosionHelper removalHelper = new ExplosionHelper(world, origin.getPos(), shortPos);
        int i = 0;

        //process Reactions
        for (int wave = 0; wave < AlchemyRegistry.numWaves(); wave++) {
            BlastWave blastWave = AlchemyRegistry.waveTypes.get(wave);
            for (Integer pos : reactionPosition[wave]) {
                Transmutation transmutation = blastWave.reactions.get(world.getBlockState(shortPos.getActualPos(pos)));
                if (transmutation != null) {
                    world.setBlockState(shortPos.getActualPos(pos), transmutation.output);
                }
            }
        }

        removalHelper.setBlocksForRemoval(destroyedBlocks);

        LogHelper.stopTimer();
        LogHelper.startTimer("Adding Lava");

        //process depositions
        for (int wave = 0; wave < AlchemyRegistry.numWaves(); wave++) {
            for (Integer pos : depositionPosition[wave]) {
                world.setBlockState(shortPos.getActualPos(pos), AlchemyRegistry.waveTypes.get(wave).getDeposition(world.rand));
            }
        }

        LogHelper.stopTimer();
        LogHelper.startTimer("Adding update Blocks");
        removalHelper.addBlocksForUpdate(blocksToUpdate);
        LogHelper.dev("Blocks Removed: " + i);
        LogHelper.stopTimer();

        removalHelper.finish();

        isDead = true;
        detonated = true;

        final BlockPos pos = origin.getPos();
        PacketExplosionFX packet = new PacketExplosionFX(pos, radius, false);
        DraconicEvolution.network.sendToAllAround(packet, new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), radius * 4));

        new DelayedExecutor(30) {
            @Override
            public void execute(Object[] args) {
                List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(radius * 2.5, radius * 2.5, radius * 2.5));

                for (Entity e : list) {
                    double dist = e.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    float dmg = 10000F * (1F - (float) (dist / (radius * 1.2D)));
                    e.attackEntityFrom(fusionExplosion, dmg);
                }
            }
        }.run();

        LogHelper.dev("Total explosion time: " + (System.currentTimeMillis() - l) / 1000D + "s");
        return true;
    }

}

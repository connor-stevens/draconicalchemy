package com.cleanroommc.draconicalchemy.alchemy;

import java.util.ArrayList;
import java.util.List;

public class AlchemyRegistry {
    public static List<BlastWave> waveTypes = new ArrayList<>();
    public static List<Converter> converters = new ArrayList<>();
    public static List<Reaction> reactions = new ArrayList<>();
    public static List<Reflector> reflectors = new ArrayList<>();

    public final static BlastWave CHAOTICBLASTWAVE = new BlastWave("Chaotic", true);
    public static void registerBlastWave(BlastWave blastWave) {
        waveTypes.add(blastWave);
    }

    public static void registerConverter(Converter converter) {
        converters.add(converter);
    }

    public static void registerReaction(Reaction reaction) {
        reactions.add(reaction);
    }

    public static void registerReflector(Reflector reflector) {
        reflectors.add(reflector);
    }

    public static int numWaves() {
        return waveTypes.size();
    }

    public static int getWaveId(BlastWave blastWave) {
        return waveTypes.indexOf(blastWave);
    }
}

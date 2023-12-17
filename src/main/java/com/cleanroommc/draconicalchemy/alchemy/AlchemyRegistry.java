package com.cleanroommc.draconicalchemy.alchemy;

import com.cleanroommc.draconicalchemy.jei.polarisation.RecipeWrapperFactoryPolarisation;
import com.cleanroommc.draconicalchemy.jei.transmutation.RecipeWrapperFactoryTransmutation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlchemyRegistry {
    public static List<BlastWave> waveTypes = new ArrayList<>();
    public static List<Polarisation> polarisations = new ArrayList<>();
    public static List<Transmutation> transmutations = new ArrayList<>();
    public static List<Reflector> reflectors = new ArrayList<>();

    public final static BlastWave CHAOTICBLASTWAVE = new BlastWave("Chaotic", true);
    public static void registerBlastWave(BlastWave blastWave) {
        waveTypes.add(blastWave);
        blastWave.id = waveTypes.size()-1;
    }

    public static void registerPolariser(Polarisation polarisation) {
        polarisations.add(polarisation);
    }

    public static void registerTransmutation(Transmutation transmutation) {
        transmutations.add(transmutation);
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


    public static List<Transmutation> getUniqueTransmutations() {
        return AlchemyRegistry.transmutations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public static List<Polarisation> getUniquePolarisations() {
        return AlchemyRegistry.polarisations.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}

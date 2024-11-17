package pl.so5dz.aprj2.core.utils;

import java.util.Random;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    private static final Random RANDOM = new Random();

    public static boolean probability(double probability) {
        return RANDOM.nextDouble() < probability;
    }
}

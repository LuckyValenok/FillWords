package net.luckyvalenok.fillwords;

import java.util.Random;

public class RandomUtils {
    
    private static final Random RANDOM = new Random();
    
    public static int intRange(int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        return min + nextInt(max - min + 1);
    }
    
    @SafeVarargs
    public static <T> T of(T... args) {
        return args[nextInt(args.length)];
    }
    
    public static int nextInt(int i) {
        return getRandom().nextInt(i);
    }
    
    public static int nextInt() {
        return getRandom().nextInt();
    }
    
    public static Random getRandom() {
        return RANDOM;
    }
}

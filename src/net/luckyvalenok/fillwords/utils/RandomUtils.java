package net.luckyvalenok.fillwords.utils;

import java.util.List;
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
    
    @Deprecated
    @SafeVarargs
    public static <T> T of(List<T>... lists) {
        return flatOf(lists);
    }
    
    @SafeVarargs
    public static <T> T flatOf(List<T>... lists) {
        int var = 0;
        for (List<T> l : lists)
            var += l.size();
        var = nextInt(var);
        for (List<T> l : lists) {
            if (var >= l.size()) {
                var -= l.size();
            } else {
                return l.get(var);
            }
        }
        throw new IllegalArgumentException("Received lists is empty");
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

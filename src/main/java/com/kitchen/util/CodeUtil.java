package com.kitchen.util;

import java.util.Random;

/**
 * @author ding
 */
public class CodeUtil {
    private static Random random;

    static {
        random = new Random();
    }

    /**
     * Generate the unique 18-number code: nano_time(15) + 000
     */
    public static String getCode() {
        String timeStr = String.format("%015d", System.nanoTime());
        return String.format("%s%d", timeStr, random.nextInt(1000));
    }
}

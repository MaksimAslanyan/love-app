package com.example.datinguserapispring.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomNumberGeneratorUtil {

    private final static Random RANDOM = new Random();

    public static int getRandomNumberInt(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        return RANDOM.nextInt(max - min + 1) + min;
    }
}

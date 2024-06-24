package com.example.datinguserapispring.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class RandomDistanceGeneratorUtil {

    public static List<Double> generate(int radius, int numberOfUsers, boolean isPremiumUser) {
        double[][] distanceIntervals = {
                {0.1, 3.0, 0.05},
                {3.0, 5.0, 0.2},
                {5.0, 10.0, 0.4},
                {10.0, 15.0, 0.25},
                {15.0, 30.0, 0.1}
        };

        Random random = new Random();
        List<Double> distances = new ArrayList<>();

        for (int i = 0; i < numberOfUsers; i++) {
            double distance;
            double cumulativeProbability = 0.0;

            if (isPremiumUser) {
                // Generate distance without excluding any interval
                distance = random.nextDouble() * radius;
            } else {
                // Generate distance excluding the 1-3 km interval
                double minDistance = 3.0;
                double maxDistance = radius;
                distance = minDistance + random.nextDouble() * (maxDistance - minDistance);
            }

            for (double[] interval : distanceIntervals) {
                double start = interval[0];
                double end = interval[1];
                double probability = interval[2];

                if (distance >= start && distance <= end) {
                    cumulativeProbability += probability;
                    break;
                } else if (distance > end) {
                    cumulativeProbability += probability;
                }
            }

            distances.add(distance);
        }

        return distances;
    }
}

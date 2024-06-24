package com.example.datinguserapispring.util;



import com.example.datinguserapispring.dto.location.LatLonDTO;
import lombok.experimental.UtilityClass;

import static com.example.datinguserapispring.constants.Geo.EARTH_RADIUS;

@UtilityClass
public class CalculateDistanceUtil {

    //Haversine formula
    public static double calculateDistance(LatLonDTO myPlace, LatLonDTO opponentPlace) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(myPlace.getLat());
        double lon1Rad = Math.toRadians(myPlace.getLon());

        double lat2Rad = Math.toRadians(opponentPlace.getLat());
        double lon2Rad = Math.toRadians(opponentPlace.getLon());

        // Calculate the differences between the latitudes and longitudes
        double latDiff = lat2Rad - lat1Rad;
        double lonDiff = lon2Rad - lon1Rad;

        // Calculate the distance using the Haversine formula
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}



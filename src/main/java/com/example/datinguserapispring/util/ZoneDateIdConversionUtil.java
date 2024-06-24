package com.example.datinguserapispring.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@UtilityClass
public class ZoneDateIdConversionUtil {

    public static LocalDateTime performTimeConversion(LocalDateTime serverTime, String zoneDateId) {
        ZoneId userTimeZone = ZoneId.of(zoneDateId);

        ZonedDateTime serverZonedDateTime = serverTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime userZonedDateTime = serverZonedDateTime.withZoneSameInstant(userTimeZone);

        return userZonedDateTime.toLocalDateTime();
    }
}

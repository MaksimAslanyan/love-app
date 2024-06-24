package com.example.datinguserapispring.util;

import com.example.datinguserapispring.dto.bot.AgeRangeDTO;
import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ExtractorAgeFromUlrUtil {

    public static AgeRangeDTO extract(String input) {
        Pattern pattern = Pattern.compile("/(\\d+)_(\\d+)/");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            byte minAge = Byte.parseByte(matcher.group(1));
            byte maxAge = Byte.parseByte(matcher.group(2));
            return new AgeRangeDTO(minAge, maxAge);
        }
        return null;
    }
}

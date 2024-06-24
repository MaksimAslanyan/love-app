package com.example.datinguserapispring.util;


import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.Period;

@UtilityClass
public class AgeCalculatorFromDateUtil {
    public static int calculate(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        Period agePeriod = Period.between(dob, currentDate);
        return agePeriod.getYears();
    }
}

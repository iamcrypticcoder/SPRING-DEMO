package com.iamcrypticcoder.mongodbexample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class RandomDateTime {
    private final LocalDateTime minTime;
    private final LocalDateTime maxTime;
    private final Random random;

    public RandomDateTime() {
        this(
                LocalDateTime.of(2020, 1, 1, 00, 00, 00),
                LocalDateTime.of(2020, 3, 30, 23, 59, 59));
    }

    public RandomDateTime(LocalDateTime minDate, LocalDateTime maxDate) {
        this.minTime = minDate;
        this.maxTime = maxDate;
        this.random = new Random();
    }

    public LocalDateTime nextDateTime() {
        int minEpoch = (int) minTime.toEpochSecond(ZoneOffset.UTC);
        int maxEpoch = (int) maxTime.toEpochSecond(ZoneOffset.UTC);
        long randomTime = minEpoch + random.nextInt(maxEpoch - minEpoch);
        return LocalDateTime.ofEpochSecond(randomTime, 0, ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return "RandomDate{" +
                "maxDate=" + maxTime +
                ", minDate=" + minTime +
                '}';
    }
}
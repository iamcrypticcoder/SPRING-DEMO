package com.iamcrypticcoder.mongodbexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RandomCityName {

    private static String[] capital = {"Lisbon", "Madrid", "Paris", "Berlin", "Warsaw", "Kiev", "Moscow", "Prague", "Rome"};

    private Random random;

    public RandomCityName() {
        random = new Random(System.nanoTime());
    }

    public String nextCity() {
        return capital[random.nextInt(capital.length)];
    }
}

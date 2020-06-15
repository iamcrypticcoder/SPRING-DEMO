package com.iamcrypticcoder.mongodbexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RandomCountryName {

    private static String[] locales;
    private static List<String> countryName;

    private Random random;

    static {
        locales = Locale.getISOCountries();
        countryName = new ArrayList<>();

        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryName.add(obj.getDisplayCountry());
        }
    }

    public RandomCountryName() {
        random = new Random(System.nanoTime());
    }

    public String nextCountry() {
        return countryName.get(random.nextInt(countryName.size()));
    }
}

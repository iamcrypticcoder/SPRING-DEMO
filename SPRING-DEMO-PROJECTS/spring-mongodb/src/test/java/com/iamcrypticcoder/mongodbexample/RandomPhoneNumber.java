package com.iamcrypticcoder.mongodbexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPhoneNumber {

    private static String[] prefixes = {"017", "019", "015", "018", "013", "016"};

    private Random random;

    public RandomPhoneNumber() {
        random = new Random(System.nanoTime());
    }

    public String nextPhoneNumber() {
        StringBuilder sb = new StringBuilder(prefixes[random.nextInt(prefixes.length)]);
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public List<String> nextListPhoneNumberList() {
        List<String> ret = new ArrayList<>();
        int c = random.nextInt(5);
        for (int i = 0; i < c; i++) ret.add(nextPhoneNumber());
        return ret;
    }

    public String[] nextListPhoneNumberArray() {
        List<String> ret = nextListPhoneNumberList();
        return ret.toArray(new String[ret.size()]);
    }
}

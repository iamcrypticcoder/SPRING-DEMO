package com.iamcrypticcoder.mongodbexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLanguage {

    private static String[] langs = {
            "Mandarin", "English", "Hindustani", "Spanish", "Arabic", "Malay",
            "Russian", "Bengali", "Portuguese", "French"};

    private Random random;

    public RandomLanguage() {
        random = new Random(System.nanoTime());
    }

    public String nextLang() {
        return langs[random.nextInt(langs.length)];
    }

    public List<String> nextLangList() {
        List<String> ret = new ArrayList<>();
        int c = random.nextInt(5);
        for (int i = 0; i < c; i++) ret.add(nextLang());
        return ret;
    }

    public String[] nextLangArray() {
        List<String> ret = nextLangList();
        return ret.toArray(new String[ret.size()]);
    }
}

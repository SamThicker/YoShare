package com.yo.yoshare.common;

import java.util.Random;

public class CodeUtil {

    private static String numSource = "1234567890";

    public static String generateNumCode(int len) {
        char[] res = new char[len];
        Random r = new Random();
        for (int i = 0; i < len; i++) {
            res[i] = numSource.charAt(r.nextInt(numSource.length()));
        }
        return new String(res);
    }

    public static void main(String[] args) {

    }
}

package com.yo.yoshare.common.security;

import java.util.Random;

public class SecurityUtil {

    private static String numSource = "1234567890";

    public static String generateCheckcode(int len) {
        char[] res = new char[len];
        Random r = new Random();
        for (int i = 0; i < len; i++) {
            res[i] = numSource.charAt(r.nextInt(numSource.length()));
        }
        return new String(res);
    }
}

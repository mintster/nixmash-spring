package com.nixmash.springdata.jpa.utils;

import java.util.Random;

/**
 * Created by daveburke on 8/1/16.
 */
public class SharedUtils {

    public static Long randomNegativeId() {
        Random rand = new Random();
        return -1 * ((long) rand.nextInt(1000));
    }

}

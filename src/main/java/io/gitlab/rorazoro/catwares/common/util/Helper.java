package io.gitlab.rorazoro.catwares.common.util;

public class Helper {
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}

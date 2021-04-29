package com.hobby.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class StreamUtil {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    public static String readAsString(InputStream inputStream) {
        //java 7 resource - auto closeable feature
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().collect(Collectors.joining(LINE_SEPARATOR));
        } catch (Throwable ex) {
            return "";
        }
    }
}

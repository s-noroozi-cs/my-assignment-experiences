package com.hobby.crawler.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScriptUtil {
    public static String getJavaScriptLibraryName(String jsSourceAddress) {
        return String.valueOf(jsSourceAddress)
                .replace('\\','/')
                .replaceAll("^.*[\\/]", "");
    }

    public static boolean acceptStaticScriptAddress(String jsFileName) {
        return jsFileName.contains(".js")
                && !jsFileName.matches(".*-[a-fA-F0-9]+.*");
    }

    public static String normalizeName(String jsFileName) {
        String normalized = Stream.of(String.valueOf(jsFileName))
                .map(p -> p.replaceAll("ie-", ""))
                .map(p -> p.split("\\.js")[0])
                .map(p -> p.split("\\."))
                .flatMap(Stream::of)
                .flatMap(p -> Stream.of(p.split("-")))
                .filter(p -> !p.matches("[0-9]+"))
                .filter(p -> !p.matches("main"))
                .filter(p -> !p.matches("min"))
                .filter(p -> !p.matches("runtime"))
                .filter(p -> !p.matches("api"))
                .filter(p -> !p.matches("debug"))
                .filter(p -> !p.matches("package"))
                .filter(p -> !p.matches("[a-f|-|0-9]{8,}"))
                .collect(Collectors.joining("-"));
        //System.out.println(String.format("input: %s output: %s", jsFileName, normalized));
        return normalized;
    }
}

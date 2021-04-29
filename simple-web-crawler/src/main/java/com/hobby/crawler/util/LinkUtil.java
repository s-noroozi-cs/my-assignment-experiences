package com.hobby.crawler.util;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LinkUtil {
    public static List<String> normalizeSearchLinkResult(List<String> links){
        Predicate<String> notRelativeAddress = link -> String.valueOf(link).matches("(?i)(http|https)://.+");
        Predicate<String> webCache = link -> String.valueOf(link).matches("(?i)(http|https)://.+(http|https)://.+");
        Predicate<String> googleService = link -> String.valueOf(link).matches("(?i)(http|https)://.*.google.com.+");

        return links
                .parallelStream()
                .filter(notRelativeAddress)
                .filter(webCache.negate())
                .filter(googleService.negate())
                .collect(Collectors.toList());
    }


}

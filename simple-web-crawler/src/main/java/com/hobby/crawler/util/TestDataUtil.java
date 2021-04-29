package com.hobby.crawler.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TestDataUtil {
    private static final String[] GOOGLE_SEARCH_TEST_RESULT = {
            "angular.html",
            "extjs.html",
            "frontend_programming",
            "gwt.html",
            "hackertank.html",
            "jquery.html",
            "polymer_js.html",
            "React.html",
            "scala.html",
            "UX_vs_UI.html"
    };


    public static String getSampleGoogleSearchResultPage() throws IOException {
        int index = (int) (Math.random() * GOOGLE_SEARCH_TEST_RESULT.length);
        String path = "test_data/google/search/result/" + GOOGLE_SEARCH_TEST_RESULT[index];
        return getResource(path);
    }

    public static List<String> getSampleHtmlPagePaths() {
        return IntStream.rangeClosed(1,3)
                .mapToObj(i -> "test_data/web/page/sample_" + i + ".html")
                .collect(Collectors.toList());
    }

    private static String getResource(String path) throws IOException {
        return StreamUtil.readAsString(ResourceUtil.getClassPathResourceAsStream(path));
    }
}

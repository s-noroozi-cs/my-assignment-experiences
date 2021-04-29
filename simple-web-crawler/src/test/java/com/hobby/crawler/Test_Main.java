package com.hobby.crawler;

import com.hobby.crawler.util.ResourceUtil;
import com.hobby.crawler.util.ScriptUtil;
import com.hobby.crawler.util.StreamUtil;
import com.hobby.crawler.util.TestDataUtil;
import com.hobby.crawler.web.html.HtmlParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test_Main {

    @Test
    public void test_main_sample_page() {
        Optional<Map.Entry<String, Long>> ajaxEntry =
                TestDataUtil
                        .getSampleHtmlPagePaths()
                        .stream()
                        .map(ResourceUtil::getClassPathResourceAsStream)
                        .map(StreamUtil::readAsString)
                        .map(HtmlParser.getRegExpHtmlParser()::extractScriptSources)
                        .flatMap(List::stream)
                        .map(ScriptUtil::getJavaScriptLibraryName)
                        .filter(ScriptUtil::acceptStaticScriptAddress)
                        .map(ScriptUtil::normalizeName)
                        .filter(p -> p != null && p.trim().length() > 0)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .limit(5)
                        .filter(entry -> entry.getKey().equalsIgnoreCase("Ajax"))
                        .findFirst();
        Assert.assertTrue(ajaxEntry.isPresent());
        Assert.assertTrue(ajaxEntry.get().getValue().longValue()== 3L);
    }

    @Test
    public void test_main_dev_mode() {
        String page_1 = "<script src=\"jquery.debug.js\"> </script>";
        String page_2 = "<script src=\"jquery.min.js\"> </script>";
        String page_3 = "<script src=\"jquery.js\"> </script>";

        Optional<Map.Entry<String, Long>> jquery =
                Stream.of(page_1,page_2,page_3)
                        .map(HtmlParser.getRegExpHtmlParser()::extractScriptSources)
                        .flatMap(List::stream)
                        .map(ScriptUtil::getJavaScriptLibraryName)
                        .filter(ScriptUtil::acceptStaticScriptAddress)
                        .map(ScriptUtil::normalizeName)
                        .filter(p -> p != null && p.trim().length() > 0)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .findFirst();
        Assert.assertTrue(jquery.isPresent());
        Assert.assertTrue(jquery.get().getKey().equalsIgnoreCase("jquery"));
        Assert.assertTrue(jquery.get().getValue().longValue()== 3L);
    }

    @Test
    public void test_main_version_mode() {
        String page_1 = "<script src=\"angular.1.2.3.js\"></script>";
        String page_2 = "<script src=\"angular.4.5.6.js\"></script>";
        String page_3 = "<script src=\"angular.7.8.9.js\"></script>";

        Optional<Map.Entry<String, Long>> angular =
                Stream.of(page_1,page_2,page_3)
                        .map(HtmlParser.getRegExpHtmlParser()::extractScriptSources)
                        .flatMap(List::stream)
                        .map(ScriptUtil::getJavaScriptLibraryName)
                        .filter(ScriptUtil::acceptStaticScriptAddress)
                        .map(ScriptUtil::normalizeName)
                        .filter(p -> p != null && p.trim().length() > 0)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .findFirst();
        Assert.assertTrue(angular.isPresent());
        Assert.assertTrue(angular.get().getKey().equalsIgnoreCase("angular"));
        Assert.assertTrue(angular.get().getValue().longValue()== 3L);
    }
}

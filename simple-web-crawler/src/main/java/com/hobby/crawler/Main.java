package com.hobby.crawler;

import com.hobby.crawler.search.SearchEngine;
import com.hobby.crawler.util.LinkUtil;
import com.hobby.crawler.util.ResourceUtil;
import com.hobby.crawler.util.ScriptUtil;
import com.hobby.crawler.web.html.HtmlParser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception{
        //check command line arguments
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Please pass search term as parameter to program.");

        //Maybe user send search terms with space,
        //following lines provide two way to join all parameters and keep it in search term
        //java 8 provide stream api and some api on it to reduce, join or ....
        String searchTerm =
                //Arrays.stream(args).reduce("", (a, b) -> a + " " + b);
                Arrays.stream(args).collect(Collectors.joining(" "));

        solutionA(searchTerm);
    }


    private static void solutionA(String searchTerm) {
        //java provide executor service to execute task in different way, runnable, callable and etc.
        //There are some type of thread pool, fixed thread pool, cached thread pool and etc,
        //Each of them describe with 3 properties, init number of thread, maximum number of threads, and idle time
        // In Fixed thread pool,init thread equal with maximum thread and idle time is zero
        //In Cached thread pool,init thread is zero , maximum thread is int_max_value (no limitation,
        // and when need , new thread created
        final ExecutorService executorService = Executors.newFixedThreadPool(100);

        //Define Search engine to provide some typeof search, now google search engine and at future other search engines
        SearchEngine searchEngine = SearchEngine.getGoogleSearchEngine();

        //There are some different ways to extract element or attributes from html content.
        //I define Html parser (interface) and define regular expression parser that only use java builtin api (RegEx)
        //Another solutions are Document parser (SAX-parser), JAX-B  (javaEE) and etc.
        //Due to the test assignment tips (use minimum third party), try to parse html using RegEx
        //But Able add another html parser without any other changes in execution scenario
        HtmlParser htmlParser = HtmlParser.getRegExpHtmlParser();

        String htmlSearchPageResult = searchEngine.search(searchTerm);

        // when search result page is html , We need main result
        // result located in heper link (html a tag)
        //At first try to extract link (href attribute)
        List<String> links = htmlParser.
                        extractLinks(htmlSearchPageResult);

        //in google page result, exist huge links , but not all of them related to main result
        //for example some of them related to next page , suggestion , web cache and etc
        // normalized search link , remove web cache , in page (html link -  #)
        // ,and some other links that does not belong to main search result
        links = LinkUtil.normalizeSearchLinkResult(links);

        links
                // java 8 provide stream to work with each item separately as same as functional programing language (haskel, erlang)
                // if all of operations that we want to apply on data ara thread safe,
                // we can improve overall throughput using parallel stream instead of steam

                //.stream()
                .parallelStream()


                //in computing world task divided to two parts: IO bound and cpu bound,
                // io bound is task that has more latency, such as disk read/write operation, network transmission and etc
                //cpu bound task that has minimum latency, such as sorting array in memory, calculating equation and etc
                //Each time based on type of task we can decide to improve overall throughput base on total thread factor.
                // total thread factor say when task is cpu bound , best throughput reach total thread equal processor cores,
                // also when task type is io bound , best throughput each total thread 2 or 3 times of processor cores

                //however, two solution exist to download resource from internet
                //download directly (sync.) and download indirectly (async.)
                //in directly mode, current thread wait on result and in other new thread create to do download and current continue its work
                //Some time we need result of task and need to improve overall throughput. java provide callable interface for this scenario.
                //When submit task (implement callable interface),executor service return future object.
                //any time we need result,call get method on future object
                // and at those time if result does not available , current thread wait to result made available.

                //.map(ResourceUtil::downloadResourceByLink)
                .map(link -> executorService.submit(ResourceUtil.downloadAsyncMode(link)))

                //fetching result is function to control download wait based on time unit
                //this controlling is good, because
                // maybe some resource does not available right now or some resource prevent direct download (need authenticate) and etc
                .map(ResourceUtil::getFutureResult)

                //after downloading resource try to extract source property of script tags in result html page
                .map(htmlParser::extractScriptSources)

                // result of previous command is list and we would like to process each item separately
                //for converting list to steam of items in stream flow use flat map operation
                .flatMap(List::stream)

                //source attribute of script tag is path of script file,
                // then we need to extract file name from path (as same as base command in linux operation system)
                .map(ScriptUtil::getJavaScriptLibraryName)

                //some script file name is not valid (due to the combine or merge operations) and file name contain hex expressions
                //some other script name is not static (refer to dynamic address php, jsp , servlet and any other things)
                //ignore above items and send other items to next section
                .filter(ScriptUtil::acceptStaticScriptAddress)

                // normalize script library , this operation try to remove version, debug, min and other things,
                // to find pure library name.
                .map(ScriptUtil::normalizeName)

                //maybe some library after normalization has not valid name part, we should ignore them
                .filter(p -> p != null && p.trim().length() > 0)

                // after all above operation, now we have stream of java script library names,
                //We should be counting them. java 8 provide collecting operation on stream.
                // some predefined operation such as identity and counting
                // help to collect all stream result in map with library name as key and total usage rate(count) as value.
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))

                //to operate on map in streaming manner,need to streaming them bt entry set
                //now streaming item instead of parallel streaming,
                // because I use limit operation and this operation does not work properly on sorted stream in parallel mode.
                .entrySet().stream()

                //sort map items by values(total count / usage rate).
                // following inline implementation of Comparator interface sort items in descending order.
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))

                //when stream sorted in descending order and then limit to first 5 items, that equal to fetch 5 top most.
                .limit(5)

                //after limiting stream to 5 top most, then writing them to system console.
                .map(entry -> String.format("%d times, library: %s", entry.getValue(), entry.getKey()))
                .forEach(System.out::println);

        //after completing operation , We must shutdown executor service to program terminated correctly.
        executorService.shutdownNow();
    }

}

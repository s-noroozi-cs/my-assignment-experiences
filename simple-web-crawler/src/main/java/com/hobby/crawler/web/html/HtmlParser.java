package com.hobby.crawler.web.html;

import java.util.List;

public interface HtmlParser {
    List<String> extractLinks(String htmlPageContent);
    List<String> extractScriptSources(String htmlPageContent);


    static HtmlParser getRegExpHtmlParser() {
        return new HtmlRegExpPageParser();
    }
}

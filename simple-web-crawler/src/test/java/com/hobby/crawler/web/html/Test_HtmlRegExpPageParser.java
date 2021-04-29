package com.hobby.crawler.web.html;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Test_HtmlRegExpPageParser {


    @Test
    public void test_extractScriptSources(){
        String html = "<html><script src=\"a/b/c/d.js\"></script></html>";
        List<String> scripts = HtmlParser.getRegExpHtmlParser().extractScriptSources(html);
        Assert.assertEquals("a/b/c/d.js",scripts.get(0));

        html = "<html><script src='a/b/c/d.js'></script></html>";
        scripts = HtmlParser.getRegExpHtmlParser().extractScriptSources(html);
        Assert.assertEquals("a/b/c/d.js",scripts.get(0));

        html = "<html><script src=\"abc.js\"></script></html>";
        scripts = HtmlParser.getRegExpHtmlParser().extractScriptSources(html);
        Assert.assertEquals("abc.js",scripts.get(0));

        html = "<html><script src=\"abc.js\"></html>";
        scripts = HtmlParser.getRegExpHtmlParser().extractScriptSources(html);
        Assert.assertEquals("abc.js",scripts.get(0));

        html = "<html><script src=\"abc.js\"></html>";
        scripts = HtmlParser.getRegExpHtmlParser().extractScriptSources(html);
        Assert.assertEquals("abc.js",scripts.get(0));

    }

    @Test
    public void test_extractLinks(){
        String html = "<html><a href='abcd'></a></html>";
        List<String> links = HtmlParser.getRegExpHtmlParser().extractLinks(html);
        Assert.assertEquals("abcd",links.get(0));

        html = "<html><a href='abcdefg'>123456</a></html>";
        links = HtmlParser.getRegExpHtmlParser().extractLinks(html);
        Assert.assertEquals("abcdefg",links.get(0));
    }
}

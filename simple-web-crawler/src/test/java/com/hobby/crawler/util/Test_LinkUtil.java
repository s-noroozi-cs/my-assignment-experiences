package com.hobby.crawler.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class Test_LinkUtil {

    @Test
    public void test_normalizeSearchLinkResult() {
        List<String> links = Arrays.asList(
                "/search?q=1234",
                "https://www.google.com?q=abc",
                "https://www.webcach.xyz.com?http://abc.def",
                "#",
                ""
        );
        Assert.assertEquals(0,LinkUtil.normalizeSearchLinkResult(links).size());

        links = Arrays.asList(
                "https://abc.com?xyz",
                "HTTP://www.amazon.com?q=abc",
                "https://WIKIPEDIA.com?abcd"
        );
        Assert.assertEquals(3,LinkUtil.normalizeSearchLinkResult(links).size());



    }
}

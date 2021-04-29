package com.hobby.crawler.util;

import org.junit.Assert;
import org.junit.Test;


public class Test_ScriptUtil {

    @Test
    public void test_getJavaScriptLibraryName(){
        Assert.assertEquals(
                "jquery.3.2.1.min.js",
                ScriptUtil.getJavaScriptLibraryName("/a/b/c/jquery.3.2.1.min.js")
                );

        Assert.assertEquals("jquery.3.2.1.min.js",
                ScriptUtil.getJavaScriptLibraryName("\\a\\b\\c\\jquery.3.2.1.min.js")
                );

        Assert.assertEquals(
                "jquery.3.2.1.min.js",
                ScriptUtil.getJavaScriptLibraryName("/a/b/c/jquery.3.2.1.min.js")
        );

        Assert.assertEquals("jquery.3.2.1.min.js",
                ScriptUtil.getJavaScriptLibraryName("../jquery.3.2.1.min.js")
        );

        Assert.assertEquals("jquery.3.2.1.min.js",
                ScriptUtil.getJavaScriptLibraryName("..\\jquery.3.2.1.min.js")
        );
    }

    @Test
    public void test_normalizeName(){
        Assert.assertEquals("jquery",
                ScriptUtil.normalizeName("jquery.3.2.1.min.js")
        );
        Assert.assertEquals("jquery",
                ScriptUtil.normalizeName("jquery.3.2.1.min.js?ver=12345")
        );
        Assert.assertEquals("jquery",
                ScriptUtil.normalizeName("debug.jquery.3.2.1.min.js?ver=12345")
        );
        Assert.assertEquals("jquery",
                ScriptUtil.normalizeName("api.jquery.3.2.1.min.js?ver=12345")
        );
    }
}

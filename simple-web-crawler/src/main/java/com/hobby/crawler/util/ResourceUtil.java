package com.hobby.crawler.util;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ResourceUtil {
    public static String getFutureResult(Future<String> future) {
        try {
            return future.get(2, TimeUnit.SECONDS);
        } catch (Throwable ex) {
            return "";
        }
    }

    public static Callable<String> downloadAsyncMode(final String linkAddress) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                return downloadResourceByLink(linkAddress);
            }
        };

    }


    public static InputStream getClassPathResourceAsStream(String resourcePath) {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resourcePath);

        if (is == null) {
            is = ResourceUtil.class.getClass().getResourceAsStream(resourcePath);
        }

        return is;
    }

    public static String downloadResourceByLink(String linkAddress) {
        try (InputStream is = new URL(linkAddress).openStream()) {
            return StreamUtil.readAsString(is);
        } catch (Throwable ex) {
            return "";
        }
    }
}

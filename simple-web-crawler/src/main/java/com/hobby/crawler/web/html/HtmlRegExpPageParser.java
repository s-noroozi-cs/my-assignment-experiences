package com.hobby.crawler.web.html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlRegExpPageParser implements HtmlParser {



    @Override
    public List<String> extractScriptSources(String htmlPageContent) {
        List<String> scriptSources = new ArrayList<>();

        Pattern r = Pattern.compile("(?i)<script?\\w+(?:\\s+(?:(?i)src=\"([^\"]*)\")|[^\\s>]+|\\s+)*>");
        Matcher m = r.matcher(htmlPageContent.replaceAll("'", "\""));

        while (m.find()) {
            String srcAttribute = m.group(1);
            if (srcAttribute != null && !srcAttribute.isEmpty())
                scriptSources.add(srcAttribute);
        }
        return scriptSources;
    }

    @Override
    public List<String> extractLinks(String htmlPageContent) {

        List<String> links = new ArrayList<>();

        Matcher mTag, mLink;
        Pattern pTag, pLink;

        final String HTML_TAG_PATTERN = "(?i)<a([^>]+)>(.*?)</a>";
        final String HTML_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

        pTag = Pattern.compile(HTML_TAG_PATTERN);
        pLink = Pattern.compile(HTML_HREF_TAG_PATTERN);

        mTag = pTag.matcher(htmlPageContent);
        while (mTag.find()) {
            String href = mTag.group(1);
            mLink = pLink.matcher(href);

            while (mLink.find()) {
                links.add(mLink.group(1)
                        .replaceAll("\"", "")
                        .replaceAll("'", "")
                );
            }

        }

        return links;
    }
}

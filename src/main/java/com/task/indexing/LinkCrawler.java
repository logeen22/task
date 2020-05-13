package com.task.indexing;

import com.task.tools.UrlTester;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//perhaps it was necessary to make a component, but I was not sure
public class LinkCrawler {
    private Set<String> set = new HashSet<>();

    public Set<String> crawLink(String link, int depth) throws IOException {
        set.add(link);
        readAllLink(link, depth);
        return set;
    }

    private void readAllLink(String link, int depth) throws IOException {
        depth--;
        if (depth < 0) {
            return;
        }
        org.jsoup.nodes.Document document = Jsoup.connect(link).get();
        Elements a = document.getElementsByTag("a");
        for (Element element : a) {
            String href = element.attr("href");
            if (!UrlTester.isLinkCorrect(href)) {
                continue;
            }
            set.add(href);
            readAllLink(href, depth);
        }
    }
}

package com.task.indexing;

import com.task.tools.UrlTester;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LinkCrawler {
    private Set<String> set = new HashSet<>();

    public Set<String> getSetOfUrl(String url, int depth) throws IOException {
        readAllLink(url, depth);
        return set;
    }

    private void readAllLink(String url, int depth) throws IOException {
        depth--;
        if (depth < 0) {
            return;
        }
        org.jsoup.nodes.Document document = Jsoup.connect(url).get();
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

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }
}

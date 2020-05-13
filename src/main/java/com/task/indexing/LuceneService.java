package com.task.indexing;

import com.task.tools.Sorting;
import com.task.tools.UrlTester;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class LuceneService {
    private final LuceneReader luceneReader;
    private final LuceneWriter luceneWriter;
    private static final int MAX_DEPTH = 2;

    public LuceneService(LuceneReader luceneReader, LuceneWriter luceneWriter) {
        this.luceneReader = luceneReader;
        this.luceneWriter = luceneWriter;
    }

    public void findAndStoreLinksIntoLuceneIndex(String url, int depth) throws IOException {
        if (!UrlTester.isLinkCorrect(url)) {
            return;
        }

        if (depth > MAX_DEPTH) {
            depth = MAX_DEPTH;
        }
        LinkCrawler linkCrawler = new LinkCrawler();
        Set<String> links = linkCrawler.crawLink(url, depth);
        luceneWriter.writeSetOfLinksIntoLuceneIndex(links);
    }

    public List<Site> getListOfSite(String searchQuery, String sort) throws IOException, ParseException {
        if (sort.equalsIgnoreCase(Sorting.ASCENDING.toString())) {
            return getSorterListOfSite(searchQuery);
        }
        return luceneReader.findDataByString(searchQuery);
    }

    public List<Site> getSorterListOfSite(String searchQuery) throws IOException, ParseException {
        List<Site> read = luceneReader.findDataByString(searchQuery);
        Collections.sort(read);
        return read;
    }
}

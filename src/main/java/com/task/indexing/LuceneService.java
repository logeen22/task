package com.task.indexing;

import com.task.tools.Sorting;
import com.task.tools.UrlTester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class LuceneService {
    private final ExecutorService executorService;
    private final LuceneReader luceneReader;
    private final LuceneWriter luceneWriter;
    private final int MAX_DEPTH = 2;

    public LuceneService(ExecutorService executorService, LuceneReader luceneReader, LuceneWriter luceneWriter) {
        this.executorService = executorService;
        this.luceneReader = luceneReader;
        this.luceneWriter = luceneWriter;
    }

    public void findAndStoreLinksIntoLuceneIndex(String url, int depth) {
        if (!UrlTester.isLinkCorrect(url)) {
            return;
        }

        if (depth > MAX_DEPTH) {
            depth = MAX_DEPTH;
        }

        luceneWriter.addUrlQueryToQueue(new UrlQuery(url, depth));
        if (!luceneWriter.isActive()) {
            executorService.execute(luceneWriter);
        }
    }

    public List<Site> getListOfSite(String searchQuery, String sort) throws Exception {
        if (sort.equals(Sorting.ASCENDING.toString().toLowerCase())) {
            return getSorterListOfSite(searchQuery);
        }
        return luceneReader.findDataByString(searchQuery);
    }

    public List<Site> getSorterListOfSite(String searchQuery) throws Exception {
        List<Site> read = luceneReader.findDataByString(searchQuery);
        Collections.sort(read);
        return read;
    }
}

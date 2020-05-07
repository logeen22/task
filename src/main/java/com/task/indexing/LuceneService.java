package com.task.indexing;

import com.task.tools.Sorting;
import com.task.tools.UrlTester;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class LuceneService {
    private final String path = System.getProperty("user.dir") + File.separator + "indexedFiles";
    private final ExecutorService executorService;
    private final LuceneReader luceneReader;
    private LuceneWriter luceneWriter;

    public LuceneService(ExecutorService executorService, IndexingService indexingService, LuceneReader luceneReader) {
        this.executorService = executorService;
        this.luceneWriter = new LuceneWriter(indexingService, path);
        this.luceneReader = luceneReader;
    }

    public void addUrl(String url, int depth) {
        if (!UrlTester.checkLinkToExistence(url)) {
            return;
        }

        if (depth > 2) {
            depth = 2;
        }

        luceneWriter.queryQueue.add(new UrlQuery(url, depth));
        if (!luceneWriter.isActive) {
            executorService.execute(luceneWriter);
        }
    }

    public List<Site> getListOfSite(String q, String sort) throws Exception {
        if (sort.equals(Sorting.ABC.toString().toLowerCase())) {
            return getSorterListOfSite(q);
        }
        return luceneReader.read(q, path);
    }

    public List<Site> getSorterListOfSite(String q) throws Exception {
        List<Site> read = luceneReader.read(q, path);
        Collections.sort(read);
        return read;
    }
}

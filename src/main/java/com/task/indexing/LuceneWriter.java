package com.task.indexing;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LuceneWriter implements Runnable {
    private final String path;
    private final IndexingService indexingService;
    private Queue<UrlQuery> queryQueue = new LinkedList<>();
    private boolean isActive = false;

    public LuceneWriter(IndexingService indexingService, @Value("${dir.path}") String path) {
        this.indexingService = indexingService;
        this.path = path;
    }

    @Override
    public void run() {
        isActive = true;
        while (true) {
            if (queryQueue.isEmpty()) {
                break;
            }
            UrlQuery poll = queryQueue.poll();
            try {
                write(poll.getUrl(), poll.getDepth());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isActive = false;
    }

    private void write(String url, int depth) throws IOException {
        save(url);
        LinkCrawler linkCrawler = new LinkCrawler();
        Set<String> setOfUrl = linkCrawler.getSetOfUrl(url, depth);
        for (String s : setOfUrl) {
            save(s);
        }
    }


    private void save(String url) throws IOException {
        if (!indexingService.saveLinkToDatabase(url)) {
            return;
        }
        try (FSDirectory dir = FSDirectory.open(Paths.get(path))) {
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            try (IndexWriter writer = new IndexWriter(dir, config)) {
                writer.addDocument(createDocument(url));
                writer.commit();
            }
        }
    }

    private Document createDocument(String url) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.connect(url).get();
        Document doc = new Document();
        doc.add(new TextField("url", url, Field.Store.YES));
        doc.add(new TextField("text", document.text(), Field.Store.YES));
        doc.add(new TextField("title", document.title(), Field.Store.YES));
        return doc;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void addUrlQueryToQueue(UrlQuery urlQuery) {
        queryQueue.add(urlQuery);
    }

    public void setQueryQueue(Queue<UrlQuery> queryQueue) {
        this.queryQueue = queryQueue;
    }
}

package com.task.indexing;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import com.task.tools.UrlTester;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LuceneWriter implements Runnable {
    private String path;
    private final IndexingService indexingService;
    public Queue<UrlQuery> queryQueue = new LinkedList<>();
    public boolean isActive = false;
    private Set<String> set = new HashSet<>();

    public LuceneWriter(IndexingService indexingService, String path) {
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

    private void write(String url, int depth) throws Exception {
        save(url);
        saveAllUrl(url, depth);
        for (String s : set) {
            save(s);
        }
        set.clear();
    }

    private void saveAllUrl(String url, int depth) throws IOException {
        depth--;
        if (depth < 0) {
            return;
        }
        org.jsoup.nodes.Document document = Jsoup.connect(url).get();
        Elements a = document.getElementsByTag("a");
        for (Element element : a) {
            String href = element.attr("href");
            if (!UrlTester.checkLinkToExistence(href)) {
                continue;
            }
            set.add(href);
            saveAllUrl(href, depth);
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
}

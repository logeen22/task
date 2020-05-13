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
public class LuceneWriter {
    private final String path;
    private final IndexingService indexingService;

    public LuceneWriter(IndexingService indexingService, @Value("${dir.path}") String path) {
        this.indexingService = indexingService;
        this.path = path;
    }

    public synchronized void writeSetOfLinksIntoLuceneIndex(Set<String> setOfLinks) throws IOException {
        for (String s : setOfLinks) {
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
}

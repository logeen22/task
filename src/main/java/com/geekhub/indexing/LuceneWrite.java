package com.geekhub.indexing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;


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

public class LuceneWrite implements Runnable {
    private IndexingRepository indexingRepository;
    private String url;
    private int depth;

    public LuceneWrite(IndexingRepository indexingRepository, String url, int depth) {
        this.indexingRepository = indexingRepository;
        this.url = url;
        this.depth = depth;
    }

    @Override
    public void run() {
        //just for debug
        System.out.println("start");
        try {
            write(url, depth);
        } catch (Exception e) {
        }
        //just for debug
        System.out.println("finish");
    }

    public void write(String url, int depth) throws Exception {
        Set<String> set = new HashSet<>();
        getAllUrl(url, depth, set);
        for (String s : set) {
            save(s);
        }
        indexingRepository.save(set);
    }

    private void getAllUrl(String url, int depth, Set<String> set) throws IOException {
        depth--;
        if (depth < 0) {
            return;
        }
        org.jsoup.nodes.Document document = Jsoup.connect(url).get();
        Elements a = document.getElementsByTag("a");
        for (Element element : a) {
            String href = element.attr("href");
            try {
                URL sites = new URL(href);
                set.add(href);
                getAllUrl(href, depth, set);
            } catch (MalformedURLException e) {
            }
        }
    }

    private void save(String url) throws IOException {
        IndexWriter writer = createWriter();
        writer.addDocument(createDocument(url));
        writer.commit();
        writer.close();
    }

    private Document createDocument(String url) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.connect(url).get();
        Document doc = new Document();
        doc.add(new TextField("url", url, Field.Store.YES));
        doc.add(new TextField("text", document.text(), Field.Store.YES));
        doc.add(new TextField("title", document.title(), Field.Store.YES));
        return doc;
    }

    private IndexWriter createWriter() throws IOException {
        String INDEX_DIR = "c:/test";
        FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }
}

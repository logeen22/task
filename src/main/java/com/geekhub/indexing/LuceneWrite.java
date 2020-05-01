package com.geekhub.indexing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LuceneWrite implements Runnable {
    public static Queue<UrlQuery> queryQueue = new LinkedList<>();
    public static boolean isActive = false;
    private final IndexingRepository indexingRepository;

    public LuceneWrite(IndexingRepository indexingRepository) {
        this.indexingRepository = indexingRepository;
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

    public void write(String url, int depth) throws Exception {
        Set<String> set = new HashSet<>();
        if (depth == 0) {
            set.add(url);
        } else {
            getAllUrl(url, depth, set);
        }
        for (String s : set) {
            if (indexingRepository.save(s)) {
                save(s);
            }
        }
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
                new URL(href);
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

package com.task.indexing;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

@Component
public class LuceneReader {
    public List<Site> read(String string, String path) throws Exception {
        try (Directory dir = FSDirectory.open(Paths.get(path)); IndexReader reader = DirectoryReader.open(dir)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs foundDocs = searchByString(string, searcher);

            List<Site> list = new ArrayList<>();

            for (ScoreDoc sd : foundDocs.scoreDocs) {
                Document d = searcher.doc(sd.doc);
                Site site = new Site();
                site.setUrl(d.get("url"));
                site.setTitle(d.get("title"));
                list.add(site);
            }
            return list;
        }
    }

    private TopDocs searchByString(String string, IndexSearcher searcher) throws Exception {
        QueryParser qp = new QueryParser("text", new StandardAnalyzer());
        Query textQuery = qp.parse(string);
        return searcher.search(textQuery, 100);
    }
}

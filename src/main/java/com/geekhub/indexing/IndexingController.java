package com.geekhub.indexing;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

@Controller
public class IndexingController {
    private final IndexingRepository indexingRepository;
    private final ExecutorService executorService;

    public IndexingController(IndexingRepository indexingRepository, ExecutorService executorService) {
        this.indexingRepository = indexingRepository;
        this.executorService = executorService;
    }

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

    @PostMapping("/index")
    public String addUrl(@RequestParam String q, int depth) {
        if (!checkUrl(q)) {
            return "redirect:/index";
        }
        if (depth > 2) {
            depth = 2;
        }
        LuceneWrite.queryQueue.add(new UrlQuery(q, depth));
        if (!LuceneWrite.isActive) {
            executorService.execute(new LuceneWrite(indexingRepository));
        }
        return "redirect:/index";
    }

    public boolean checkUrl(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }
}

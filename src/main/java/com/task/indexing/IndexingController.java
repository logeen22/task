package com.task.indexing;

import com.task.tools.UrlTester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        if (!UrlTester.testUrl(q)) {
            return "redirect:/index";
        }
        if (depth > 2) {
            depth = 2;
        }
        LuceneWriter.queryQueue.add(new UrlQuery(q, depth));
        if (!LuceneWriter.isActive) {
            executorService.execute(new LuceneWriter(indexingRepository));
        }
        return "redirect:/index";
    }
}

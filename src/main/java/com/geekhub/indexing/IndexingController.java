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
    public String addUrl(@RequestParam String q) throws Exception {
        try {
            URL url = new URL(q);
            executorService.execute(new LuceneWrite(indexingRepository, q, 1));
        }
        catch(MalformedURLException e) {
            return "redirect:/index";
        }
        return "redirect:/index";
    }
}

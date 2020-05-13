package com.task.indexing;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class IndexingController {
    private final LuceneService luceneService;

    public IndexingController(LuceneService luceneService) {
        this.luceneService = luceneService;
    }

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

    @PostMapping("/index")
    public String addUrl(@RequestParam String searchQuery, int depth) throws IOException {
        luceneService.findAndStoreLinksIntoLuceneIndex(searchQuery, depth);
        return "redirect:/index";
    }
}

package com.geekhub;

import com.geekhub.indexing.LuceneReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    private final LuceneReader luceneReader;

    public SearchController(LuceneReader luceneReader) {
        this.luceneReader = luceneReader;
    }

    @GetMapping("/")
    public String getPage() {
        return "mainPage";
    }

    @GetMapping("/search")
    public String getSearchPage(@RequestParam String q, Model model) throws Exception {
        model.addAttribute("sites", luceneReader.read(q));
        return "search";
    }
}

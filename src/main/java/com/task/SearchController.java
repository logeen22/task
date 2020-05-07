package com.task;

import com.task.indexing.Site;
import com.task.tools.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class SearchController {
    private final LuceneService luceneService;

    public SearchController(LuceneService luceneService) {
        this.luceneService = luceneService;
    }

    @GetMapping("/")
    public String getPage() {
        return "mainPage";
    }

    @GetMapping("/search")
    public String getSearchPage(@RequestParam String q, @RequestParam int p, @RequestParam String sort, Model model) throws Exception {
        List<Site> list = luceneService.getListOfSite(q, sort);
        model.addAttribute("sites", Pagination.getListOfLinksOnCurrentPage(list, p));
        model.addAttribute("pages", Pagination.getListOfIntegersThatRepresentPageNumbers(list));
        model.addAttribute("q", q);
        model.addAttribute("p", p);
        model.addAttribute("sort", sort);
        return "search";
    }
}

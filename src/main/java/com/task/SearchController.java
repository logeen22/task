package com.task;

import com.task.indexing.LuceneService;
import com.task.indexing.Site;
import com.task.tools.CalculatorForPagination;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getSearchPage(@RequestParam String searchQuery, @RequestParam int page, @RequestParam String sort, Model model) throws Exception {
        List<Site> list = luceneService.getListOfSite(searchQuery, sort);
        model.addAttribute("sites", CalculatorForPagination.getListOfLinksOnCurrentPage(list, page));
        model.addAttribute("pages", CalculatorForPagination.getListOfIntegersThatRepresentPageNumbers(list));
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("page", page);
        model.addAttribute("sort", sort);
        return "search";
    }
}

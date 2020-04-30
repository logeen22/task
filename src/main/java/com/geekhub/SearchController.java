package com.geekhub;

import com.geekhub.indexing.LuceneReader;
import com.geekhub.indexing.Site;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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
    public String getSearchPage(@RequestParam String q, @RequestParam int p, Model model) throws Exception {
        List<Site> read = luceneReader.read(q);
        List<Site> list;
        System.out.println("read : " + read.size());
        if (read.size() < 10) {
            list = read;
        } else {
            list = read.subList(p*10 - 10, p*10);
        }
        model.addAttribute("sites", list);

        System.out.println("Length: " + read.size() + " " + read.size()/10);
        int[] mass = new int[read.size()/10];
        for (int i = 1; i < read.size()/10 + 1; i++) {
            mass[i-1] = i;
        }
        System.out.println(mass.length);
        model.addAttribute("pages", mass);
        model.addAttribute("q", q);

        return "search";
    }
}

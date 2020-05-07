package com.task.tools;

import com.task.indexing.Site;

import java.util.ArrayList;
import java.util.List;

public class Pagination {
    private static final int countOfLinks = 10;

    public static List<Integer> getListOfIntegersThatRepresentPageNumbers(List<Site> list) {
        List<Integer> integerList = new ArrayList<>();
        int countOfPages = list.size() % countOfLinks == 0 ? list.size() / countOfLinks : list.size() / countOfLinks + 1;
        for (int i = 0; i < countOfPages; i++) {
            integerList.add(i + 1);
        }
        return integerList;
    }

    public static List<Site> getListOfLinksOnCurrentPage(List<Site> list, int p) {
        if (list.size() < p * countOfLinks) {
            return list.subList(p * countOfLinks - countOfLinks, list.size());
        } else {
            return list.subList(p * countOfLinks - countOfLinks, p * countOfLinks);
        }
    }
}

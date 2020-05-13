package com.task.tools;

import com.task.indexing.Site;

import java.util.ArrayList;
import java.util.List;

public class CalculatorForPagination {
    private static final int COUNT_OF_LINKS = 10;

    public static List<Integer> getListOfIntegersThatRepresentPageNumbers(List<Site> list) {
        List<Integer> integerList = new ArrayList<>();
        int countOfPages = list.size() % COUNT_OF_LINKS == 0 ? list.size() / COUNT_OF_LINKS : list.size() / COUNT_OF_LINKS + 1;
        for (int i = 0; i < countOfPages; i++) {
            integerList.add(i + 1);
        }
        return integerList;
    }

    public static List<Site> getListOfLinksOnCurrentPage(List<Site> list, int p) {
        if (list.size() < p * COUNT_OF_LINKS) {
            return list.subList(p * COUNT_OF_LINKS - COUNT_OF_LINKS, list.size());
        } else {
            return list.subList(p * COUNT_OF_LINKS - COUNT_OF_LINKS, p * COUNT_OF_LINKS);
        }
    }
}

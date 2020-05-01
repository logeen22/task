package com.task.tools;

import com.task.indexing.Site;

import java.util.ArrayList;
import java.util.List;

public class Pagination {

    public static List<Integer> getListOfPages(List<Site> list) {
        List<Integer> integerList = new ArrayList<>();
        if (list.size()%10 == 0) {
            for (int i = 0; i < list.size() / 10; i++) {
                integerList.add(i + 1);
            }
        } else {
            for (int i = 0; i < list.size() / 10 + 1; i++) {
                integerList.add(i + 1);
            }
        }
        return integerList;
    }

    public static List<Site> getSiteOnPage(List<Site> list, int p) {
        if (list.size() < p * 10) {
            return list.subList(p * 10 - 10, list.size());
        } else {
            return list.subList(p * 10 - 10, p * 10);
        }
    }
}

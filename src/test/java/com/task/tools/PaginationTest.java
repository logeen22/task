package com.task.tools;

import com.task.indexing.Site;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginationTest {

    @Test
    void getListOfPages() {
        List<Site> list1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list1.add(new Site());
        }
        List<Integer> listOfPages1 = Pagination.getListOfPages(list1);

        List<Site> list2 = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            list2.add(new Site());
        }
        List<Integer> listOfPages2 = Pagination.getListOfPages(list2);

        List<Site> list3 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list3.add(new Site());
        }
        List<Integer> listOfPages3 = Pagination.getListOfPages(list3);

        Assert.assertEquals(2, listOfPages1.size());
        Assert.assertEquals(3, listOfPages2.size());
        Assert.assertEquals(1, listOfPages3.size());
    }

    @Test
    void getSiteOnPage() {
        List<Site> list = new ArrayList();
        for (int i = 0; i < 25; i++) {
            list.add(new Site());
        }
        List<Site> siteOnPage1 = Pagination.getSiteOnPage(list, 3);
        List<Site> siteOnPage2 = Pagination.getSiteOnPage(list, 2);
        Assert.assertEquals(siteOnPage1.size(), 5);
        Assert.assertEquals(siteOnPage2.size(), 10);
    }
}
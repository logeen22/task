package com.task.tools;

import com.task.indexing.Site;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PaginationTest {

    @Test
    void testMethodGetListOfPagesWhenEvenNumberOfPages() {
        List<Site> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new Site());
        }

        List<Integer> listOfPages = CalculatorForPagination.getListOfIntegersThatRepresentPageNumbers(list);
        Assert.assertEquals(2, listOfPages.size());
    }

    @Test
    void testMethodGetListOfPagesWhenOddNumberOfPages() {
        List<Site> list = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            list.add(new Site());
        }

        List<Integer> listOfPages = CalculatorForPagination.getListOfIntegersThatRepresentPageNumbers(list);
        Assert.assertEquals(3, listOfPages.size());
    }

    @Test
    void testMethodGetListOfPagesWhenFewerPages() {
        List<Site> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new Site());
        }

        List<Integer> listOfPages = CalculatorForPagination.getListOfIntegersThatRepresentPageNumbers(list);
        Assert.assertEquals(1, listOfPages.size());
    }

    @Test
    void testMethodGetSiteOnPageWhenPageInMiddleOfList() {
        List<Site> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add(new Site());
        }

        List<Site> siteOnPage = CalculatorForPagination.getListOfLinksOnCurrentPage(list, 2);
        Assert.assertEquals(siteOnPage.size(), 10);
    }

    @Test
    void testMethodGetSiteOnPageWhenPageInBeginOfList() {
        List<Site> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add(new Site());
        }

        List<Site> siteOnPage = CalculatorForPagination.getListOfLinksOnCurrentPage(list, 1);
        Assert.assertEquals(siteOnPage.size(), 10);
    }

    @Test
    void testMethodGetSiteOnPageWhenPageInEndOfList() {
        List<Site> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add(new Site());
        }

        List<Site> siteOnPage1 = CalculatorForPagination.getListOfLinksOnCurrentPage(list, 3);
        Assert.assertEquals(siteOnPage1.size(), 5);
    }
}
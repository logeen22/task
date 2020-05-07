package com.task.tools;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class UrlTesterTest {

    @Test
    void testMethodCheckLinkToExistenceWithCorrectUrl() {
        boolean b = UrlTester.checkLinkToExistence("https://www.google.com/");
        Assert.assertTrue(b);
    }

    @Test
    void testMethodCheckLinkToExistenceWithIncorrectUrl() {
        boolean a = UrlTester.checkLinkToExistence("google.com");
        Assert.assertFalse(a);
    }
}
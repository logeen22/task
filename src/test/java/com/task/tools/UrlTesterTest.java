package com.task.tools;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class UrlTesterTest {

    @Test
    void testMethodCheckLinkToExistenceWithCorrectUrl() {
        boolean b = UrlTester.isLinkCorrect("https://www.google.com/");
        Assert.assertTrue(b);
    }

    @Test
    void testMethodCheckLinkToExistenceWithIncorrectUrl() {
        boolean a = UrlTester.isLinkCorrect("google.com");
        Assert.assertFalse(a);
    }
}
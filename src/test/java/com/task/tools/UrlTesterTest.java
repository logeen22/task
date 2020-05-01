package com.task.tools;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlTesterTest {

    @Test
    void testUrl() {
        boolean a = UrlTester.testUrl("google.com");
        Assert.assertFalse(a);
        boolean b = UrlTester.testUrl("https://www.google.com/");
        Assert.assertTrue(b);
    }
}
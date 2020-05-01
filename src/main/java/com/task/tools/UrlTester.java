package com.task.tools;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlTester {
    public static boolean testUrl(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }
}

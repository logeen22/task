package com.task.indexing;

import java.util.Objects;

public class Site implements Comparable<Site>{
    private int id;
    private String url;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return id == site.id &&
                Objects.equals(url, site.url) &&
                Objects.equals(title, site.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, title);
    }

    @Override
    public int compareTo(Site o) {
        return this.title.compareTo(o.title);
    }
}

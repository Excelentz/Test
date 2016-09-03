package com.example.admin.myapplication;


public class RssItem {

    private final String title;
    private final String link;
    private final String comments;

    public RssItem(String title, String link, String comments) {
        this.title = title;
        this.link = link;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getComments() {
        return comments;
    }
}
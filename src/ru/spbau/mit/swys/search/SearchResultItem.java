package ru.spbau.mit.swys.search;

public class SearchResultItem {
    private String title;
    private String pictureUrl;
    private String url;

    public SearchResultItem(String title, String pictureUrl, String url) {
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getUrl() {
        return url;
    }
}

package ru.spbau.mit.swys.search;

public abstract class SearchService {
    public SearchResult search(Image im) {
        SearchResult result = buildEmptyResult(im);
        fillSearchResult(im, result);

        return result;
    }

    protected abstract void fillSearchResult(Image im, SearchResult result);

    protected SearchResult buildEmptyResult(Image im) {
        return new SearchResult(im);
    }
}


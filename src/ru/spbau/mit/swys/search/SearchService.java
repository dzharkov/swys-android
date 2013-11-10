package ru.spbau.mit.swys.search;

public abstract class SearchService {
    public SearchResult search(Image im) throws SearchQueryException {
        SearchResult result = buildEmptyResult(im);
        fillSearchResult(im, result);

        return result;
    }

    protected abstract void fillSearchResult(Image im, SearchResult result) throws SearchQueryException;

    protected SearchResult buildEmptyResult(Image im) {
        return new SearchResult(im);
    }
}


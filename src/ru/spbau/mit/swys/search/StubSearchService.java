package ru.spbau.mit.swys.search;

public class StubSearchService extends SearchService {
    @Override
    protected void fillSearchResult(Image im, SearchResult result) {
        result.addItem(new SearchResultItem(
                "Хутор в Малороссии (1884)",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/5/51/KonstantinKryzhitsky_HutorVMalorossii_1884.jpg/640px-KonstantinKryzhitsky_HutorVMalorossii_1884.jpg?uselang=ru",
                "http://ru.wikipedia.org/wiki/%D0%9A%D1%80%D1%8B%D0%B6%D0%B8%D1%86%D0%BA%D0%B8%D0%B9,_%D0%9A%D0%BE%D0%BD%D1%81%D1%82%D0%B0%D0%BD%D1%82%D0%B8%D0%BD_%D0%AF%D0%BA%D0%BE%D0%B2%D0%BB%D0%B5%D0%B2%D0%B8%D1%87"
        ));
        result.addItem(new SearchResultItem(
                "Данте Габриэль Россетти",
                "http://i.allday.ru/uploads/posts/2009-01/1232719137_3.jpg",
                "http://allday2.com/engine/print.php?newsid=81490"
        ));
    }
}


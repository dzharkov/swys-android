package ru.spbau.mit.swys.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchResult implements Parcelable {
    private ArrayList< SearchResultItem > items = new ArrayList<SearchResultItem>();

    public SearchResult(Image image) {
    }

    public void addItem(SearchResultItem item) {
        items.add(item);
    }

    public SearchResultItem[] getItems() {
        return items.toArray(new SearchResultItem[items.size()]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(items.size());

        for (SearchResultItem item : items) {
            parcel.writeString(item.getTitle());
            parcel.writeString(item.getPictureUrl());
            parcel.writeString(item.getUrl());
        }
    }

    public static final Parcelable.Creator<SearchResult> CREATOR = new Parcelable.Creator<SearchResult>() {
        // распаковываем объект из Parcel
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    // конструктор, считывающий данные из Parcel
    private SearchResult(Parcel parcel) {
        int size = parcel.readInt();
        for (int i = 0; i < size; i++) {
            SearchResultItem item = new SearchResultItem(parcel.readString(), parcel.readString(), parcel.readString());
            items.add(item);
        }
    }
}


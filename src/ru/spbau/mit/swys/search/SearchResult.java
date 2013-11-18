package ru.spbau.mit.swys.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SearchResult implements Parcelable {
    private ArrayList<SearchResultItem> items = new ArrayList<SearchResultItem>();

    public SearchResult(Image image) {
    }

    public void addItem(SearchResultItem item) {
        items.add(item);
    }

    public SearchResultItem[] getItems() {
        return items.toArray(new SearchResultItem[items.size()]);
    }

    public boolean isSuccessful() {
        return items.size() > 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(items.toArray());
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

    // Constructor by Parcel
    private SearchResult(Parcel parcel) {
        items = parcel.readArrayList(SearchResultItem.class.getClassLoader());
    }
}


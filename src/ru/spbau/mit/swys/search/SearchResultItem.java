package ru.spbau.mit.swys.search;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchResultItem implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTitle());
        parcel.writeString(getPictureUrl());
        parcel.writeString(getUrl());
    }

    public static final Parcelable.Creator<SearchResultItem> CREATOR = new Parcelable.Creator<SearchResultItem>() {
        public SearchResultItem createFromParcel(Parcel in) {
            return new SearchResultItem(
                    in.readString(),
                    in.readString(),
                    in.readString()
            );
        }

        public SearchResultItem[] newArray(int size) {
            return new SearchResultItem[size];
        }
    };
}


package com.example.komal.booklisting;

import android.os.Parcelable;

/**
 * Created by Komal on 01-04-2018.
 */
import android.os.Parcel;


public class BookArray implements Parcelable {
    private String mAuthor;
    private String mTitle;

    public BookArray(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public static final Creator<BookArray> CREATOR = new Creator<BookArray>() {

        @Override
        public BookArray createFromParcel(Parcel in) {
            return new BookArray(in);
        }

        @Override
        public BookArray[] newArray(int size) {
            return new BookArray[size];
        }
    };

    public String getAuthor() {
        return mAuthor;
    }

    protected BookArray(Parcel inv) {
        mAuthor = inv.readString();
        mTitle = inv.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);

    }
}

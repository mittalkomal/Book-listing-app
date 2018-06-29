package com.example.komal.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Komal on 01-04-2018.
 */

public class BookLoader extends AsyncTaskLoader<List<BookArray>> {
    private static final String LOG_TAG = BookLoader.class.getName();

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BookArray> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<BookArray> books = Utils.fetchBookData(mUrl);
        return books;
    }
}

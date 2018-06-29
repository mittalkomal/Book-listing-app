package com.example.komal.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookArray>> {

    final String Url = "https://www.googleapis.com/books/v1/volumes?q=search+";
    private TextView noAnswer;
    private BookAdapter mAdapter;
    private Button search;
    private EditText userInput;
    private ListView bookListView;
    static final String RESULTS = "RequiredBooksSearchResults";
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noAnswer = findViewById(R.id.noAnswer);
        userInput = findViewById(R.id.inputText);
        bookListView = findViewById(R.id.List);
        search = findViewById(R.id.search);
        noAnswer.setVisibility(View.GONE);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    LoaderManager loader = getLoaderManager();
                    loader.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

                } else {

                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    mAdapter.clear();
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                }
            }


        });
        mAdapter = new BookAdapter(this, new ArrayList<BookArray>());

        bookListView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            BookArray[] books = (BookArray[]) savedInstanceState.getParcelableArray(RESULTS);
            mAdapter.addAll(books);
        }
    }

    @Override
    public Loader<List<BookArray>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, getUrl());
    }


    @Override
    public void onLoadFinished(Loader<List<BookArray>> loader, List<BookArray> books) {
        mEmptyStateTextView.setVisibility(View.GONE);
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            noAnswer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookArray>> loader) {

        mAdapter.clear();
    }

    @Override
    public void onSaveInstanceState(Bundle State) {
        super.onSaveInstanceState(State);
        BookArray[] books = new BookArray[mAdapter.getCount()];
        for (int i = 0; i < books.length; i++) {
            books[i] = mAdapter.getItem(i);
        }
        State.putParcelableArray(RESULTS, books);
    }

    private String getUrl() {
        String UserInput = userInput.getText().toString().trim().replaceAll("\\s+", "+");
        String finalurl = Url + UserInput;
        return finalurl;
    }

}

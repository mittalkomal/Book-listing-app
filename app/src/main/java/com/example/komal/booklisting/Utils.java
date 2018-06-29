package com.example.komal.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Komal on 01-04-2018.
 */

public final class Utils {
    public static final String LOG_TAG = Utils.class.getName();


    private Utils() {
    }

    public static List<BookArray> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<BookArray> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    private static URL createUrl(String Url) {
        URL url = null;
        try {
            url = new URL(Url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<BookArray> extractFeatureFromJson(String bookJSON) {

        List<BookArray> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(bookJSON);
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);
                JSONObject bookDetail = bookObject.getJSONObject("volumeInfo");
                JSONArray authorsArray = bookDetail.getJSONArray("authors");
                String authors = AuthorsList(authorsArray);
                String title = bookDetail.getString("title");
                BookArray book = new BookArray(title, authors);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static String AuthorsList(JSONArray authorsList) throws JSONException {

        String ListInString = null;

        for (int i = 0; i < authorsList.length(); i++) {
            if (i == 0) {
                ListInString = authorsList.getString(0);
            } else {
                ListInString = ListInString + ", " + authorsList.getString(i);
            }
        }

        return ListInString;
    }
}

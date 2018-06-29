package com.example.komal.booklisting;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Komal on 01-04-2018.
 */

public class BookAdapter extends ArrayAdapter<BookArray> {
    public BookAdapter(Activity context, List<BookArray> booklist) {
        super(context, 0, booklist);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
        }
        BookArray currentList = getItem(position);

        TextView auhtorTextView =  listItemView.findViewById(R.id.author);
        auhtorTextView.setText(currentList.getAuthor());

        TextView titleTextView =  listItemView.findViewById(R.id.title);
        titleTextView.setText(currentList.getTitle());


        return listItemView;
    }
}

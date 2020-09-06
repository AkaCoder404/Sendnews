package com.example.news.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.models.Pagination;

import java.util.List;

public class PaginationAdapter extends ArrayAdapter<Pagination> {

        private Context context;
        private List<Pagination> values;

        public PaginationAdapter(Context context, List<Pagination> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = inflater.inflate(R.layout.list_item_pagination, parent, false);
            }

//            TextView textView = (TextView) row.findViewById(R.id.lsit_item_pagination_text);
//
//            Pagination item = values.get(position);
//            String message = item.getMessage();
//            textView.setText(message);
//            return row;
            return row;
        }
}

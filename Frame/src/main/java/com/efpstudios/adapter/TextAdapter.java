package com.efpstudios.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.efpstudios.R;
import com.efpstudios.activity.MainActivity;

public class TextAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] namaFont;
    private Typeface[] typefaces;

    public TextAdapter(Context context, int layoutXML, String[] namaFont, Typeface[] typefaces) {
        super(context, layoutXML, namaFont);

        this.context = context;
        this.namaFont = namaFont;
        this.typefaces = typefaces;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        MainActivity mainActivity = (MainActivity)context;
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_row, parent, false);

        TextView label = (TextView) row.findViewById(R.id.textView1);
        label.setText(namaFont[position]);

        label.setTypeface(typefaces[position]);

        return row;
    }

}

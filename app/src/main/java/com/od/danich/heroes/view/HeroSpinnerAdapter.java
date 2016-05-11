package com.od.danich.heroes.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.od.danich.heroes.R;

import java.util.List;

/**
 * Created by sanches on 1/26/16.
 */
public class HeroSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> items;

    public HeroSpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.context=context;
        this.items=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View mySpinner = inflater.inflate(R.layout.spinner_header, parent, false);
        TextView main_text = (TextView) mySpinner.findViewById(R.id.text_spinner);
        main_text.setText(items.get(position));
        return mySpinner;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View mySpinnerDropdown = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        TextView main_text = (TextView) mySpinnerDropdown.findViewById(R.id.text_spinner_dropdown);
        main_text.setText(items.get(position));
        return mySpinnerDropdown;
    }
}
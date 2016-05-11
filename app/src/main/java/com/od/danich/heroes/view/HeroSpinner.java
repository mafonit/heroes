package com.od.danich.heroes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class HeroSpinner extends Spinner {
    OnItemSelectedListener listener;
    private AdapterView<?> lastParent;
    private View lastView;
    private long lastId;

    public HeroSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        initlistner();
    }

    @Override
    public void setSelection(int position) {
        if (position == getSelectedItemPosition() && listener != null) {
            listener.onItemSelected(lastParent, lastView, position, lastId);
        } else {
            super.setSelection(position);
        }

    }

    private void initlistner() {
        super.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                lastParent = parent;
                lastView = view;
                lastId = id;
                if (listener != null) {
                    listener.onItemSelected(parent, view, position, id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                if (listener != null) {
                    listener.onNothingSelected(parent);
                }
            }
        });

    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }

}
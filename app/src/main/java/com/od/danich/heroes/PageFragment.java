package com.od.danich.heroes;


import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.od.danich.heroes.helpers.TimeConverter;
import com.od.danich.heroes.helpers.ImageDownloader;
import com.od.danich.heroes.view.ItemHero;

public class PageFragment extends Fragment {


    private String name, img, desc;
    private long time;

    static PageFragment newInstance(ItemHero itemHero) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putString("name", itemHero.getName());

        arguments.putString("img", itemHero.getImage());
        arguments.putLong("time", itemHero.getTime());
        arguments.putString("desc", itemHero.getDescription());
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        img = getArguments().getString("img");
        time = getArguments().getLong("time");
        desc = getArguments().getString("desc");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_full_desc_, null);


        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        tvName.setText(name);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvTime.setText(TimeConverter.convert(time));
        TextView tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        tvDesc.setText(desc);


        ImageView imageView= (ImageView) view.findViewById(R.id.img);

            try {
                imageView.setImageBitmap(new ImageDownloader().execute(img).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        return view;
    }


}
package com.od.danich.heroes.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.od.danich.heroes.R;
import com.od.danich.heroes.helpers.ImageDownloader;
import com.od.danich.heroes.helpers.TimeConverter;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private  List<ItemHero> itemHeroList;
    private ItemHero item;
    private static ListItemClickListener itemListener;

    public interface ListItemClickListener {
        void ItemClicked(ItemHero item);

    }

    public ItemAdapter( ListItemClickListener listener, List<ItemHero> items){
        itemHeroList =items;
        this.itemListener = listener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView tvName, tvTime;
        public ImageView iconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            iconImageView = (ImageView) itemView.findViewById(R.id.img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.ItemClicked(itemHeroList.get(getLayoutPosition()));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View channelView = inflater.inflate(R.layout.item_hero, parent, false);
        ViewHolder viewHolder= new ViewHolder( channelView);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
        item = itemHeroList.get(position);

        TextView textViewName = holder.tvName;
        textViewName.setText(item.getName());

        TextView textViewNumber = holder.tvTime;
        textViewNumber.setText(TimeConverter.convert(item.getTime()));

        ImageView iconImageView = holder.iconImageView;

        if (item.getImage().isEmpty()) {
            iconImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            try {
                iconImageView.setImageBitmap(new ImageDownloader().execute(item.getImage()).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public int getItemCount() {
        return itemHeroList.size();
    }

    public void swap(List<ItemHero> datas){

        itemHeroList=datas;
        notifyDataSetChanged();
    }


}



package com.od.danich.heroes.helpers;

import android.content.Context;

import com.od.danich.heroes.network.HttpRequest;
import com.od.danich.heroes.view.ItemHero;

import java.util.ArrayList;
import java.util.List;


public class AttractData {
    private static AttractData instance;
    private List<ItemHero> itemHeroArrayList;
    private Context context;

    private AttractData(Context context) {
        itemHeroArrayList = new ArrayList<ItemHero>();
        this.context=context;
        HttpRequest httpRequest=new HttpRequest(context);
        itemHeroArrayList = httpRequest.getHeroList();
    }



    public static AttractData getInstance(Context context) {
        if (instance == null) {instance = new AttractData(context);
            }
        return instance;
    }

    public List getHeroItemList() {
        return itemHeroArrayList;
    }


}

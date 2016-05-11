package com.od.danich.heroes;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.od.danich.heroes.dialogs.ErrorDialog;
import com.od.danich.heroes.network.HttpRequest;
import com.od.danich.heroes.network.HttpResponseListener;
import com.od.danich.heroes.view.ItemHero;

import java.util.List;

public class SlideItemsActivity extends AppCompatActivity implements HttpResponseListener {

    private HttpRequest httpRequest;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int heroListSize ;
    private ListView mDrawerList;
    private PagerAdapter pagerAdapter;
    private String itemName;
    private List<ItemHero> heroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_items);
        toolbar = (Toolbar) findViewById(R.id.toolbar_slide);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        httpRequest = new HttpRequest(this);
        httpRequest.setResponseListener(this);

        Intent intent = getIntent();
        itemName = intent.getStringExtra("name");

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupItemList();
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,R.layout.navigation_item, getResources().getStringArray(R.array.nav_drawer_items)));

    }

    public void setupItemList() {
        heroList = httpRequest.getHeroList();
        if(heroList !=null) {
            heroListSize = heroList.size();
            pagerAdapter = new HeroPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            for(int i=0;i< heroList.size();i++){
                if(heroList.get(i).getName().equals(itemName)){
                    viewPager.setCurrentItem(i);
                    break;
                }
            }
        }
        else{
            showDialogWarning(getString(R.string.list_item_error));
        }
    }


    public void showDialogWarning(String message) {
        ErrorDialog errorDialog = ErrorDialog.newInstance(message);
        errorDialog.show(getSupportFragmentManager(), "errorDialog");
    }

    @Override
    public void handleError(String messageError) {
        showDialogWarning(messageError);

    }


    private class HeroPagerAdapter extends FragmentPagerAdapter {

        public HeroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(heroList.get(position));
        }

        @Override
        public int getCount() {
            return heroListSize;
        }

    }



}

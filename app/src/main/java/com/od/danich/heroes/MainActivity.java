package com.od.danich.heroes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.od.danich.heroes.helpers.AttractData;
import com.od.danich.heroes.dialogs.ErrorDialog;
import com.od.danich.heroes.network.HttpRequest;
import com.od.danich.heroes.network.HttpResponseListener;
import com.od.danich.heroes.view.HeroSpinner;
import com.od.danich.heroes.view.HeroSpinnerAdapter;
import com.od.danich.heroes.view.DividerItemDecoration;
import com.od.danich.heroes.view.ItemAdapter;
import com.od.danich.heroes.view.ItemHero;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HttpResponseListener {

    private HttpRequest httpRequest;
    private RecyclerView rvItems;
    private LinearLayoutManager layoutManager;
    private ItemAdapter itemAdapter;

    private ListView lvNavigationMenu;
    private Toolbar toolbar;
    private HeroSpinnerAdapter spinnerAdapter;
    public ArrayList<String> spinnerList = new ArrayList<>();
    private HeroSpinner customSpinner;
    private AdapterView.OnItemSelectedListener listener;
    private List<ItemHero> heroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        httpRequest = new HttpRequest(this);
        httpRequest.setResponseListener(this);

        customSpinner = (HeroSpinner) findViewById(R.id.sp_names);
        lvNavigationMenu = (ListView) findViewById(R.id.left_drawer);
        rvItems = (RecyclerView)findViewById(R.id.rvItems);

        lvNavigationMenu.setAdapter(new ArrayAdapter<>(this,
                R.layout.navigation_item, getResources().getStringArray(R.array.nav_drawer_items)));
        setupUi();
    }

    private void setupUi(){
        heroList = httpRequest.getHeroList();
        if(heroList!=null) {
            setupSpinner(heroList);
            setupHeroList(heroList);
        }
    }

    private void setupSpinner(List<ItemHero> heroList) {
        for (ItemHero item : heroList) {
            spinnerList.add(item.getName());
        }
        spinnerAdapter = new HeroSpinnerAdapter(this, R.layout.spinner_header, spinnerList);
        spinnerAdapter.insert(getString(R.string.all_names), 0);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(spinnerAdapter);
        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                sortByName(spinnerList.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
        customSpinner.setOnItemSelectedEvenIfUnchangedListener(listener);
    }

    private void setupHeroList(List<ItemHero> heroList) {

        rvItems.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        itemAdapter = new ItemAdapter( new ItemAdapter.ListItemClickListener() {
            @Override
            public void ItemClicked(ItemHero item) {
                Intent intent= new Intent(MainActivity.this, SlideItemsActivity.class);
                intent.putExtra("name",item.getName());
                startActivity(intent);
            }
        }, heroList);
        rvItems.setAdapter(itemAdapter);
        layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        layoutManager.scrollToPosition(0);
    }

   private void sortByName(String selectedItem) {
       List<ItemHero> sortedList = new ArrayList<>();
        List<ItemHero> listItems = AttractData.getInstance(this).getHeroItemList();
       if(selectedItem.equals(getResources().getString(R.string.all_names))){
           sortedList.addAll(listItems);
           itemAdapter.swap(sortedList);
       }else{
           for (int i=0;i< listItems.size();i++) {
                if(selectedItem.equals(listItems.get(i).getName())){
                   sortedList.add(listItems.get(i));
               }
           }
           itemAdapter.swap(sortedList);
       }
   }


    @Override
    public void handleError(String messageError) {
        showDialogWarning(messageError);
    }
    public void showDialogWarning(String message) {
        ErrorDialog errorDialog = ErrorDialog.newInstance(message);
        errorDialog.show(getSupportFragmentManager(), "errorDialog");
    }


}
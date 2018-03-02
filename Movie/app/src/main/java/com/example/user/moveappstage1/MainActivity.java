package com.example.user.moveappstage1;

import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.moveappstage1.Adapter.ImageAdapter;
import com.example.user.moveappstage1.Adapter.Scroller;
import com.example.user.moveappstage1.Background.AsyncTaskCompleteListener;
import com.example.user.moveappstage1.Background.NetworkAsyanTask;
import com.example.user.moveappstage1.JsonParse.JsonAnalysis;
import com.example.user.moveappstage1.Networking.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListner,AsyncTaskCompleteListener {

    /////Viewsss
    private Toolbar toolbar;
    GridLayoutManager gridLayoutManager;
    private ImageAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar probar_Loading_for_Data;
    private Menu optionsMenu;
    private int Selected_item = 0;  // to checked any Item has been Checked  // this will contain the id of item
    // APis Links
    private URL APi_Link_Most_Pooular;
    private URL APi_Link_Top_Rated;
    private URL Joker_URl;
    ////////////////////////////////////////////////////////////////////////////////////////
    private Scroller scrollListener;      /// this to get throw pages in Api
    /////////////////  For Knowing Most_Popular || Top_Rated is Selected
    private int Selecteditem = 0; // this will refer to any item has selected for compare
   // final private int Item_Will_Selected = 2;
    //////////////////             on Save Instance State String Keys         //////////////////////////////////////
    private final  String Movies_Data_SK="Get_Movies_From_onSaveInstant" ;
    private final String Poster_Data_SK = "Get_Poster_From_onSaveInstant";
    private final String Page_States_SK="Pages_State" ;
    private final String Selected_Item_S = "Selected_item";
    private final String Selected_Item_Value = "Selected_item_Value";
    //////////////////    Data Comming from Internet //////////
    private ArrayList<Movies> InComingData_Movies=new ArrayList<>();
    private ArrayList<String> InComingData_Poster=new ArrayList<>();
    private PagesState InComingData_PageState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews(); // for initait views
        MakeURLS();  //Set up Links
        // Contains Key for all Valuses one Valuse not found means that all values not found
        if (savedInstanceState == null || !savedInstanceState.containsKey(Selected_Item_Value)) {
            CheckInternetConnetion(false); // Check internet connection

        } else {
            //
            if(CheckInternetConnetion(true)) {
                // get values from onSave Instance State
                Selected_item = savedInstanceState.getInt(Selected_Item_S); // to get selected item form onSave Instance State
                Selecteditem = savedInstanceState.getInt(Selected_Item_Value);
                ArrayList<Movies> movies = savedInstanceState.getParcelableArrayList(Movies_Data_SK);
                ArrayList<String> Posters = savedInstanceState.getStringArrayList(Poster_Data_SK);
                InComingData_Movies = movies;
                InComingData_Poster = Posters;
                InComingData_PageState = savedInstanceState.getParcelable(Page_States_SK);
            }
        }
    }

    private boolean CheckInternetConnetion(boolean fromrotating) {
        if (Network.TestNetwork(this)) {
            // Define Values
                InComingData_Movies = new ArrayList<>();
                InComingData_Poster = new ArrayList<>();
                InComingData_PageState = new PagesState();
           new NetworkAsyanTask(this,this,probar_Loading_for_Data,false).execute(Joker_URl);
           return true;
        } else {
            if(fromrotating)
            {

            }else {
                Toast.makeText(this, getResources().getString(R.string.No_Internet), Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    private void initviews() {
        //Views
        toolbar=findViewById(R.id.toolbar_mainactivity);
        probar_Loading_for_Data = findViewById(R.id.probar_loading);
        recyclerView = findViewById(R.id.tv_item);
        //set up toolbar
        setSupportActionBar(toolbar);
        //Set up LayOutmanager
         gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        /// for refhrehing
        scrollListener = new Scroller(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                LoadPageFromLinks(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapter=new ImageAdapter(MainActivity.this,this);
        recyclerView.setAdapter(adapter);
    }

    private void LoadPageFromLinks(int page) {
        //PagesState state;
        PagesState pagesState=InComingData_PageState;
        if(page>Integer.valueOf( pagesState.getTotal_pages())){
            Toast.makeText(this, getResources().getString(R.string.NoMoreFilms), Toast.LENGTH_SHORT).show();

        }else {
            URL url = Network.MakeUrlForPages(Network.ApiLinks[Selecteditem], String.valueOf(page));
            Log.i("Pagess",url.toString());
            new NetworkAsyanTask(this,this,probar_Loading_for_Data,true).execute(url);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);

        // Selected_item is a int resourec of item has been selected before destroying
        // on this coded we checked the ites as checked
        // optionsMenu we will use this value to check whick item i selected on CheckWhichtemSelected() Function
        optionsMenu = menu;
        if (Selected_item != 0) {
            menu.findItem(Selected_item).setChecked(true);
            menu.performIdentifierAction(Selected_item, 0);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_Most_Popular:
                item.setChecked(true);
                Selecteditem = 0;
                SendUrl(APi_Link_Most_Pooular);
                MakeAdapter(InComingData_Poster); // this Method to put the ArrayList in the adapter
                scrollListener.remakeLayout();
                return true;
            case R.id.menu_item_Top_Rated:
                item.setChecked(true);                     // Mark item as checked
                Selecteditem = 1;
                SendUrl(APi_Link_Top_Rated);
                MakeAdapter(InComingData_Poster); // this Method to put the ArrayList in the adapter
                scrollListener.remakeLayout();
                return true;
            case R.id.menu_Refersh:
                CheckInternetConnetion(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void MakeURLS() {
        // Set up the Url
        // and put it into All_Links[] to execute in background

        Network.MakeUrlWithApikey();// for connect with Api Keys
        APi_Link_Most_Pooular = Network.BuildURL(Network.API_LINK_POPULAR);
        APi_Link_Top_Rated = Network.BuildURL(Network.API_LINK_Top_Related);
        Joker_URl=APi_Link_Most_Pooular;
    }
    private void SendUrl(URL url){
        Joker_URl=url;
        new NetworkAsyanTask(this,this,probar_Loading_for_Data,false).execute(Joker_URl);
    }


    public void PraparePosterList() {
        ArrayList<String> strings = new ArrayList<>();
        for (Movies movie : InComingData_Movies) {
            strings.add(movie.getMovie_Poster());
        }
        InComingData_Poster = strings;
        ClickOnItemsHasChecked();
    }

    // this for make Poster List from Movies objects
    public  ArrayList<String> PraparePosterList(ArrayList<Movies> movies){
         ArrayList<String> Retuner=new ArrayList<>();
        for(Movies movie :movies ){
            Retuner.add(movie.getMovie_Poster());
        }
        return Retuner;
    }

    @Override
    public void onTaskComplete(Object Movies, Object PageState, Object addPages) {
        if((Boolean) addPages){
            AddItemsToIncomingdataFroScrolling((ArrayList<Movies>) Movies);
        }else {
            InComingData_Movies = (ArrayList<Movies>) Movies;
            InComingData_PageState = (PagesState) PageState;
            PraparePosterList(); /// To Make Posters
        }
    }


    // this AsyncTask For get Data ON Other Pages
    class GetDataPagesOnOtherThread extends AsyncTask<URL,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            probar_Loading_for_Data.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            try {
                String retunerformHtml=Network.GetDataFromHtml(url);
                return retunerformHtml;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // stop loading
            probar_Loading_for_Data.setVisibility(View.INVISIBLE);
            ArrayList<Movies> movies=new ArrayList<>();
            if(!s.equals("")&&s!=null){
                ////Analysis Json
                movies=JsonAnalysis.parseMoviesJson(s);
                AddItemsToIncomingdataFroScrolling(movies);
            }
        }
    }

    // when Get Data From Scolling we need To add this to Main Data In App
    private void AddItemsToIncomingdataFroScrolling(ArrayList<Movies> movies) {
        ArrayList<String>Posters=new ArrayList<>();
            for (int i = 0; i < movies.size(); i++) {
                InComingData_Movies.add(movies.get(i));
                Posters = PraparePosterList(movies);
                InComingData_Poster.add(Posters.get(i));
            }
        MakeAdapter(InComingData_Poster);
    }

    //This to Put Values On Adapter
    private void MakeAdapter(List<String> Links) {

        adapter.setLinks(Links);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Movies_Data_SK,  InComingData_Movies);
        outState.putStringArrayList(Poster_Data_SK, InComingData_Poster);
        outState.putParcelable(Page_States_SK,InComingData_PageState); //save PageState
     // to Get Which Item Has Selected
        Selected_item=CheckWhichtemSelected();
        outState.putInt(Selected_Item_S,Selected_item);
        outState.putInt(Selected_Item_Value,Selecteditem);
        super.onSaveInstanceState(outState);
    }

    // this function return the Resource Id Of Item Has Checked
    public int CheckWhichtemSelected(){
        //get item from OptionsMenu that has refer to Menu
        MenuItem item1=optionsMenu.findItem(R.id.menu_item_Most_Popular);
        if(item1.isChecked()){
            return R.id.menu_item_Most_Popular;
        }
        else{
            return R.id.menu_item_Top_Rated;
        }
    }

    // for Click On Items
    public void ClickOnItemsHasChecked(){
         // for handel Clcikes
        MakeAdapter(InComingData_Poster);
    }

    @Override
    public void onItemClicked(int i) {
        //// On Items Click
        Intent intent =new Intent(this,DetailActivity.class);
        intent.putExtra(DetailActivity.Poster_Image_K,InComingData_Movies.get(i).getMovie_Poster());
        intent.putExtra(DetailActivity.Title_K,InComingData_Movies.get(i).getTitle());
        intent.putExtra(DetailActivity.Plot_Synopsis_K,InComingData_Movies.get(i).getPlot_synopsis());
        intent.putExtra(DetailActivity.Release_Date_K,InComingData_Movies.get(i).getRelase_Date());
        intent.putExtra(DetailActivity.Vote_Average_K,InComingData_Movies.get(i).getVote_average());
        startActivity(intent);
    }
}

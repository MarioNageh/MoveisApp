package com.example.user.moveappstage1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity {
android.support.v7.widget.Toolbar toolbar;
ImageView Poster_tv;
TextView Title_txt,vote_avarage_txt,relase_data_txt,over_View_txt;
    RatingBar ratingBar;

    public static final String Poster_Image_K="Poster_Image";
    public static final String Title_K="Title";
    public static final String Release_Date_K="Release_Date";
    public static final String Vote_Average_K="Vote_Average";
    public static final String Plot_Synopsis_K="Plot_Synopsis";


     private static String Poster_Image_V,Title_V,Release_Date_V,Vote_Average_V,Plot_Synopsis_V;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initviews();
        iniToolbar();
        checkForIntentcoming();
    }

    private void checkForIntentcoming() {
        Intent intent =getIntent();
        if(intent!=null){
            Poster_Image_V=intent.getStringExtra(Poster_Image_K);
            Title_V=intent.getStringExtra(Title_K);
            Release_Date_V=intent.getStringExtra(Release_Date_K);
            Vote_Average_V=intent.getStringExtra(Vote_Average_K);
            Plot_Synopsis_V=intent.getStringExtra(Plot_Synopsis_K);
            PutValuesInViews();
        }
    }

    private void PutValuesInViews() {
        toolbar.setTitle(Title_V);
        Picasso.with(this).load(Poster_Image_V).into(Poster_tv);
        Title_txt.setText(Title_V);
        vote_avarage_txt.setText(Vote_Average_V);
        if(Float.valueOf(Vote_Average_V)>10)
            ratingBar.setRating(10);
        else{
            float value=Float.valueOf(Vote_Average_V)*.5f;
            ratingBar.setRating(value);
        }
        relase_data_txt.setText(FormattingDate(Release_Date_V));
        over_View_txt.setText(Plot_Synopsis_V);
    }

    private void iniToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initviews() {
        toolbar=findViewById(R.id.toolbar);
        Poster_tv=findViewById(R.id.Poster_Tv);
        Title_txt=findViewById(R.id.tv_title_txt);
        vote_avarage_txt=findViewById(R.id.rate_tv_txt);
        ratingBar=findViewById(R.id.ratingd_bar);
        relase_data_txt=findViewById(R.id.tv_Relase_Date);
        over_View_txt=findViewById(R.id.tv_over_view);
    }
    private String FormattingDate(String Date){
        StringBuilder string_date=new StringBuilder();
        String[] splitsarray=Date.split("-");
        switch (splitsarray[1]){
            case "01":
                string_date.append("Jan");
                break;
            case "02":
                string_date.append("Feb");
                break;
            case "03":
                string_date.append("Mar");
                break;
            case "04":
                string_date.append("Apr");
                break;
            case "05":
                string_date.append("May");
                break;
            case "06":
                string_date.append("Jun");
                break;
            case "07":
                string_date.append("Jul");
                break;
            case "08":
                string_date.append("Aug");
                break;
            case "09":
                string_date.append("Sep");
                break;
            case "10":
                string_date.append("Oct");
                break;
            case "11":
                string_date.append("Nov");
                break;
            case "12":
                string_date.append("Dec");
                break;
                default:
                    break;
        }
        string_date.append(" ");
        string_date.append(splitsarray[2]);
        string_date.append(",");
        string_date.append(splitsarray[0]);

    return string_date.toString();
    }

    private Target MakeTarget(final View view){
        //                   important here

        //in this method i intened to get the background of toolbar from json
        //but i get probelm that i want to display home button and title on this
        //the probelm is that the background have many colors
        //this willn't display the homebutton and title when background is the same color
        //soo i make fixed photo
        //if you have any idea tell me in review thanks

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
               // Bitmap b = Bitmap.createScaledBitmap(bitmap,50,50,false);
                BitmapDrawable icon = new BitmapDrawable(view.getResources(), bitmap);
                toolbar.setBackground(icon);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        return target;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

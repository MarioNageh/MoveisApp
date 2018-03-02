package com.example.user.moveappstage1.Adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by user on 28/02/2018.
 */

public abstract class Scroller extends RecyclerView.OnScrollListener {
    // before loading more.
    private int visibleThreshold = 7;
// Cuuren Page we also in page 1 because fist we download data from api from page 1
    private int currentPage = 1;
//Total item Counr To save total item
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // if there no data or no internet connection to loadata  this is converd because the asyntask get page 1 all the time
    private int startingPageIndex = 0;
    RecyclerView.LayoutManager mLayoutManager;

    public Scroller(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        // befor load any data
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();

            // to assign valuse to compare beneath
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // stop loading where totalitems in layout > the last tolal item
            // thats mean we can scroll down again
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }
            // on stop loading and there is some viwes to scroll
            // send the abstract with page number
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                if(currentPage==0 && (lastVisibleItemPosition + visibleThreshold)>0 ){
                    currentPage =(totalItemCount/20)+1; //when press top_rated$$Most_Poular
                }else {
                    currentPage++;
                }
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }
    }
    public void remakeLayout(){
        //to remove all layour views
        this.mLayoutManager.removeAllViews();
    }
    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}
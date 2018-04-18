package com.cloverinfosoft.fooddelivery.dashboard;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Admin on 15-04-2018.
 */

class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private boolean isLoading;
    private boolean hasMorePages;
    private int pageNumber = 0;
    private RefreshList refreshList;
    private boolean isRefreshing;
    private int pastVisibleItems;

    EndlessScrollListener(RefreshList refreshList) {
        this.isLoading = false;
        this.hasMorePages = true;
        this.refreshList = refreshList;
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        GridLayoutManager manager =
                (GridLayoutManager) recyclerView.getLayoutManager();

        int visibleItemCount = manager.getChildCount();
        int totalItemCount = manager.getItemCount();
        int firstVisibleItems = manager.findFirstVisibleItemPosition();
       // if (firstVisibleItems != null && firstVisibleItems.length > 0) {
            pastVisibleItems = firstVisibleItems;
        //}

        if (visibleItemCount + pastVisibleItems >= totalItemCount && !isLoading) {
            isLoading = true;
            if (hasMorePages && !isRefreshing) {
                isRefreshing = true;
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        refreshList.onRefresh(pageNumber);
                    }
                }, 200);
            }
        } else {
            isLoading = false;
        }
    }

    public void noMorePages() {
        this.hasMorePages = false;
    }

    void notifyMorePages() {
        isRefreshing = false;
        pageNumber = pageNumber + 1;
    }

    interface RefreshList {
        void onRefresh(int pageNumber);
    }  }


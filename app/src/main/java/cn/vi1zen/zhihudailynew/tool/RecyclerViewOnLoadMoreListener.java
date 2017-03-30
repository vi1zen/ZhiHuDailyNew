package cn.vi1zen.zhihudailynew.tool;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import cn.vi1zen.zhihudailynew.ui.daily.DailyRecycleViewAdapter;

/**
 * Created by Destiny on 2017/3/15.
 */
public abstract class RecyclerViewOnLoadMoreListener extends RecyclerView.OnScrollListener {

    private int lastVisibleItemPosition;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isLoading;
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        visibleItemCount = layoutManager.getChildCount();
        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        totalItemCount = layoutManager.getItemCount();
        //当
        //1.有数据
        //2.滚动是闲置状态
        //3.滚到最后一个Item
        //4.不处于加载更多的状态
        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItemPosition >= totalItemCount - 1 && !isLoading) {
            onLoadMore();
            Log.i("LOADMORE","LOAD MORE......");
            isLoading = true;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        visibleItemCount = layoutManager.getChildCount();
//        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//        totalItemCount = layoutManager.getItemCount();


    }

    public abstract void onLoadMore();

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean getLoading() {
        return isLoading;
    }

}

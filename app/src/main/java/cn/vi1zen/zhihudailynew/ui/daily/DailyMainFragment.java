package cn.vi1zen.zhihudailynew.ui.daily;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.jude.rollviewpager.hintview.TextHintView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.DailiesJson;
import cn.vi1zen.zhihudailynew.model.TopStory;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.net.UrlConstants;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.tool.DividerItemDecoration;
import cn.vi1zen.zhihudailynew.tool.RecyclerViewOnLoadMoreListener;
import cn.vi1zen.zhihudailynew.tool.RollPagerAdapter;
import cn.vi1zen.zhihudailynew.tool.StateTool;
import cn.vi1zen.zhihudailynew.ui.MainFragment;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Destiny on 2017/3/14.
 */

public class DailyMainFragment extends MainFragment implements  DailyRecycleViewAdapter.LoadMoreDataListener,SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LinearLayoutManager linearLayoutManager;
    private DailyRecycleViewAdapter dailyRecycleViewAdapter;

    private String endDate;//当前App里有的最新日报的时间

    private String startDate;//当前App下拉加载的最早时间

    private Subscriber subscriber;

    private StateTool stateTool;

    private RollPagerView rollPagerView;//轮播图

    private RollPagerAdapter rollPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_main,container,false);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        stateTool = new StateTool(linearLayout);
        stateTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate = null;
                stateTool.showProgressView();
                getMore(null);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));//设置分割线
        dailyRecycleViewAdapter = new DailyRecycleViewAdapter(this,recyclerView);

        //轮播图
        View linearView = LayoutInflater.from(getActivity()).inflate(R.layout.header_carousel_image,container,false);
        rollPagerView = (RollPagerView) linearView.findViewById(R.id.rollPagerView);
        rollPagerView.setAdapter(rollPagerAdapter = new RollPagerAdapter(rollPagerView));
        dailyRecycleViewAdapter.setHeaderView(rollPagerView);

        dailyRecycleViewAdapter.setOnMoreDataLoadListener(this);
        recyclerView.setAdapter(dailyRecycleViewAdapter);

        stateTool.showProgressView();
        getMore(null);
        return view;
    }

    private void getMore(final String targetDate) {

        subscriber = new Subscriber<DailiesJson>() {
            @Override
            public void onCompleted() {
                stateTool.closeRefresh(swipeRefreshLayout);
                dailyRecycleViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
                if (targetDate == null) {
                    stateTool.showErrorView();
                }
            }

            @Override
            public void onNext(final DailiesJson dailiesJson) {
                if (dailiesJson.getStories().size() == 0) {
                    stateTool.showEmptyView();
                    return;
                }
                if (TextUtils.isEmpty(endDate)) { //如果是首次加载这个界面
                    startDate = dailiesJson.getDate();
                    endDate = dailiesJson.getDate();
//                    dailyRecycleViewAdapter.addList(dailiesJson.getStories());
                    //增加轮播图数据
                    dailyRecycleViewAdapter.addListToHeader(dailiesJson.getStories(),dailiesJson.getTop_stories());
                    stateTool.showContentView();
                } else if (targetDate == null) { //表示下拉刷新
                    if (endDate.equals(dailiesJson.getDate())) { //App的最晚时间 等于 下拉新获取的时间
                        swipeRefreshLayout.setRefreshing(false);
                    } else { ////App的最晚时间 不等于 下拉新获取的时间
                        endDate = dailiesJson.getDate();
//                        dailyRecycleViewAdapter.addList(dailiesJson.getStories());
                        //增加轮播图数据
                        dailyRecycleViewAdapter.addListToHeader(dailiesJson.getStories(),dailiesJson.getTop_stories());
                       stateTool.showContentView();
                    }
                } else { //表示上拉加载
                    startDate = dailiesJson.getDate();
//                    dailyRecycleViewAdapter.addList(dailiesJson.getStories());
                    dailyRecycleViewAdapter.addListToHeader(dailiesJson.getStories(),dailiesJson.getTop_stories());
                    dailyRecycleViewAdapter.setLoading(false);
                    dailyRecycleViewAdapter.notifyDataSetChanged();
                    stateTool.showContentView();
                }
            }
        };
        if (targetDate == null) {
            ZhiHuHttp.getZhiHuHttp().getDailies(subscriber);
        } else {
            ZhiHuHttp.getZhiHuHttp().getDailiesBefore(subscriber, targetDate);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //如果没有这个置空，当没有设置fragment缓存时,会执行destroyView方法.但是成员变量并不会摧毁,依然有值
        // 下次再进来时,会得出endDate不是空的情况,从而跳过"首次加载这个界面"这个逻辑
        endDate = null;
    }

    /**
     * @param tartgetDate targetDate为空表示首次刷新或者下拉刷新, 获取最新数据
     *                    不为空表示加载更多, 获取指定日期历史数据
     *                    此为纯RxJava
     */

    private void getMoreOld(final String tartgetDate) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Response response = null;
                    if (tartgetDate == null) {
                        response = OkHttpSync.get(UrlConstants.DAILIES);
                    } else {
                        response = OkHttpSync.get(String.format(UrlConstants.DAILIES_BEFORE, tartgetDate));
                    }
                    if (response.isSuccessful()) {
                        DailiesJson dailiesJson = new Gson().fromJson(response.body().string(), DailiesJson.class);

                        startDate = dailiesJson.getDate();
//                            dailyRecycleViewAdapter.addList(dailiesJson.getStories());

                        /**
                         * 当我使用传两个参数到adapter时，上拉加载需要拖动两次才能正确触发，试了各种方法都不行，还望大神指导下，谢谢
                         */
                        dailyRecycleViewAdapter.addListToHeader(dailiesJson.getStories(),dailiesJson.getTop_stories());

                        //增加轮播图数据
//                      rollPagerAdapter.addTopData(dailiesJson.getTop_stories());
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(new Exception("error"));
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onCompleted() {
                        dailyRecycleViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(Boolean isRefreshing) {
                        swipeRefreshLayout.setRefreshing(isRefreshing);
                    }
                });
    }

    @Override
    public void loadMoreData() {
        dailyRecycleViewAdapter.notifyDataSetChanged();
        getMoreOld(startDate);
    }

    @Override
    public void onRefresh() {
        getMore(null);
    }
}

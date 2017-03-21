package cn.vi1zen.zhihudailynew.ui.special;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.SectionJson;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.net.UrlConstants;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.tool.RecyclerViewOnLoadMoreListener;
import cn.vi1zen.zhihudailynew.ui.BaseActivity;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Destiny on 2017/3/20.
 */

public class SpecialActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvStories)
    RecyclerView rvStories;

    private SectionJson specialJson;
    private int id;
    private String name;
    private SpecialStoryAdapter specialStoryAdapter;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerViewOnLoadMoreListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");

        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        toolbar.setTitle(name);//设置主标题
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        specialStoryAdapter = new SpecialStoryAdapter(this);
        rvStories.setAdapter(specialStoryAdapter);
        rvStories.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
//        rvStories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvStories.addOnScrollListener(listener = new RecyclerViewOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getSpecialStories(specialJson.getTimestamp());
            }
        });

        getSpecialStories(-1);
    }

    private void getSpecialStories(final long timestamp) {
        Subscriber subscriber = new Subscriber<SectionJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(SectionJson specialJson) {
                SpecialActivity.this.specialJson = specialJson;
                specialStoryAdapter.addList(specialJson.getStories());
                specialStoryAdapter.notifyDataSetChanged();
                if (timestamp > 0) {
                    listener.setLoading(false);
                }
            }
        };
        if (timestamp == -1) {
            ZhiHuHttp.getZhiHuHttp().getSpecial(subscriber, String.valueOf(id));
        } else if (timestamp == 0) {
        } else {
            ZhiHuHttp.getZhiHuHttp().getSpecialBefore(subscriber, String.valueOf(id), String.valueOf(timestamp));
        }

    }

    /**
     * @param timestamp targetDate为-1表示首次刷新或者下拉刷新, 获取最新数据
     *                  为0表示没有更多数据
     *                  其他值为加载更多数据
     */
    @Deprecated
    private void getSpecialStoriesOld(final long timestamp) {
        Observable.create(new Observable.OnSubscribe<SectionJson>() {

            private Response response;

            @Override
            public void call(Subscriber<? super SectionJson> subscriber) {
                try {
                    if (timestamp == -1) {
                        response = OkHttpSync.get(String.format(UrlConstants.SECTION, String.valueOf(id)));
                    } else if (timestamp == 0) {
                    } else {
                        response = OkHttpSync.get(String.format(UrlConstants.SECTION_BEFORE, String.valueOf(id), String.valueOf(timestamp)));
                    }
                    if (response == null) {
                    } else if (response.isSuccessful()) {
                        specialJson = new Gson().fromJson(response.body().string(), SectionJson.class);
                        specialStoryAdapter.addList(specialJson.getStories());
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
                .subscribe(new Subscriber<SectionJson>() {

                    @Override
                    public void onCompleted() {
                        specialStoryAdapter.notifyDataSetChanged();
                        if (timestamp > 0) {
                            listener.setLoading(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(SectionJson specialJson) {
                    }
                });
    }
}

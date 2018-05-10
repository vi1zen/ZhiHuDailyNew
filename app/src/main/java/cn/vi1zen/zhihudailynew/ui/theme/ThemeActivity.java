package cn.vi1zen.zhihudailynew.ui.theme;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.ThemeJson;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.net.UrlConstants;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.tool.DividerItemDecoration;
import cn.vi1zen.zhihudailynew.ui.BaseActivity;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vi1zen on 2017/3/20.
 */

public class ThemeActivity extends BaseActivity{
    @BindView(R.id.ivBackground)
    ImageView ivBackground;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.rvStories)
    RecyclerView rvStories;

    private ThemeJson themeJson;
    private int id;
    private ThemeStoryAdapter themeStoryAdapter;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra("ID", 0);
        //已经在xml中设置
//        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
//        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        themeStoryAdapter = new ThemeStoryAdapter(this);
        rvStories.setAdapter(themeStoryAdapter);
        rvStories.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
//        rvStories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        getThemeData();
    }

    private void getThemeData() {
        Subscriber subscriber = new Subscriber<ThemeJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(ThemeJson themeJson) {
                themeStoryAdapter.addList(themeJson.getStories());
                themeStoryAdapter.notifyDataSetChanged();
                Glide.with(ThemeActivity.this).load(themeJson.getBackground()).into(ivBackground);
                collapsingToolbarLayout.setTitle(themeJson.getName());
                tvDescription.setText(themeJson.getDescription());
            }
        };

        ZhiHuHttp.getZhiHuHttp().getTheme(subscriber, String.valueOf(id));
    }

    @Deprecated
    private void getThemeDataOld() {
        Observable.create(new Observable.OnSubscribe<ThemeJson>() {

            @Override
            public void call(Subscriber<? super ThemeJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.THEME, String.valueOf(id)));
                    if (response.isSuccessful()) {
                        themeJson = new Gson().fromJson(response.body().string(), ThemeJson.class);
                        themeStoryAdapter.addList(themeJson.getStories());
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
                .subscribe(new Subscriber<ThemeJson>() {

                    @Override
                    public void onCompleted() {
                        themeStoryAdapter.notifyDataSetChanged();
                        Glide.with(ThemeActivity.this).load(themeJson.getBackground()).into(ivBackground);
                        collapsingToolbarLayout.setTitle(themeJson.getName());
                        tvDescription.setText(themeJson.getDescription());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(ThemeJson themeJson) {
                    }
                });
    }
}

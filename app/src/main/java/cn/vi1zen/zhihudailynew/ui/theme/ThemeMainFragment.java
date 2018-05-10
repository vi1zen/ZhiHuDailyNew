package cn.vi1zen.zhihudailynew.ui.theme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.ThemesJson;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.net.UrlConstants;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.tool.GridItemDecoration;
import cn.vi1zen.zhihudailynew.ui.MainFragment;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vi1zen on 2017/3/14.
 */

public class ThemeMainFragment extends MainFragment {
    @BindView(R.id.theme_recyclerView)
    RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ThemeAdapter themeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.theme_main,container,false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(gridLayoutManager = new GridLayoutManager(getActivity(),3));
//        recyclerView.addItemDecoration(new GridItemDecoration(10, 3));
        recyclerView.setAdapter(themeAdapter = new ThemeAdapter(this));

        getThemes();

        return view;
    }

    private void getThemes() {
        Subscriber subscriber = new Subscriber<ThemesJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(ThemesJson themesJson) {
                themeAdapter.addList(themesJson.getOthers());
                themeAdapter.notifyDataSetChanged();
            }
        };

        ZhiHuHttp.getZhiHuHttp().getThemes(subscriber);
    }

    @Deprecated
    private void getThemesOld() {
        Observable.create(new Observable.OnSubscribe<ThemesJson>() {

            @Override
            public void call(Subscriber<? super ThemesJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.THEMES);
                    if (response.isSuccessful()) {
                        ThemesJson themesJson = new Gson().fromJson(response.body().string(), ThemesJson.class);
                        themeAdapter.addList(themesJson.getOthers());
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
                .subscribe(new Subscriber<ThemesJson>() {

                    @Override
                    public void onCompleted() {
                        themeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(ThemesJson themesJson) {
                    }
                });
    }
}

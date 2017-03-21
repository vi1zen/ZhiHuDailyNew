package cn.vi1zen.zhihudailynew.ui.hot;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.Hot;
import cn.vi1zen.zhihudailynew.model.HotJson;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.net.UrlConstants;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.ui.MainFragment;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Destiny on 2017/3/14.
 */

public class HotMainFragment extends MainFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private HotAdapter hotAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_main,container,false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(hotAdapter = new HotAdapter(this));

        getHot();
        return view;
    }

    private void getHot() {
        Subscriber subscriber = new Subscriber<HotJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(HotJson hotJson) {
                ArrayList<Hot> hots = hotJson.getHots();
                hotAdapter.addList(hots);
                hotAdapter.notifyDataSetChanged();
            }
        };

        ZhiHuHttp.getZhiHuHttp().getHot(subscriber);
    }

    private void getHotOld() {
        Observable.create(new Observable.OnSubscribe<Response>() {

            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.HOT);
                    if (response.isSuccessful()) {
                        Type listType = new TypeToken<ArrayList<Hot>>() {
                        }.getType();
                        String responseString = response.body().string();
                        responseString = responseString.substring(10, responseString.length() - 1);
                        ArrayList<Hot> hots = new Gson().fromJson(responseString, listType);
                        hotAdapter.addList(hots);
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
                .subscribe(new Subscriber<Response>() {

                    @Override
                    public void onCompleted() {
                        hotAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(Response response) {
                    }
                });
    }
}

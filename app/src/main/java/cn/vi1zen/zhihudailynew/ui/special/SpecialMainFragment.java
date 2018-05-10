package cn.vi1zen.zhihudailynew.ui.special;


import android.os.Bundle;
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
import cn.vi1zen.zhihudailynew.model.Section;
import cn.vi1zen.zhihudailynew.model.SectionsJson;
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
 * Created by vi1zen on 2017/3/14.
 */

public class SpecialMainFragment extends MainFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RecyclerView rvSections;

    private SpecialAdapter specialAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_main, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(specialAdapter = new SpecialAdapter(this));

        getSpecials();

        return view;
    }

    private void getSpecials() {
        Subscriber subscriber = new Subscriber<SectionsJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(SectionsJson sectionsJson) {
                ArrayList<Section> sections = sectionsJson.getSections();
                specialAdapter.addList(sections);
                specialAdapter.notifyDataSetChanged();
            }
        };

        ZhiHuHttp.getZhiHuHttp().getSpecials(subscriber);
    }


    private void getSectionsOld() {
        Observable.create(new Observable.OnSubscribe<SectionsJson>() {

            @Override
            public void call(Subscriber<? super SectionsJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(UrlConstants.SECTIONS);
                    if (response.isSuccessful()) {
                        Type listType = new TypeToken<ArrayList<Section>>(){}.getType();
                        String responseString = response.body().string();
                        responseString = responseString.substring(8, responseString.length()-1);
                        ArrayList<Section> sections = new Gson().fromJson(responseString, listType);
                        specialAdapter.addList(sections);
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
                .subscribe(new Subscriber<SectionsJson>() {

                    @Override
                    public void onCompleted() {
                        specialAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(SectionsJson sectionsJson) {
                    }
                });
    }

}

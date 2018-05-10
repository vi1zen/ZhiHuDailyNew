package cn.vi1zen.zhihudailynew.net;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import cn.vi1zen.zhihudailynew.model.DailiesJson;
import cn.vi1zen.zhihudailynew.model.DailyJson;
import cn.vi1zen.zhihudailynew.model.HotJson;
import cn.vi1zen.zhihudailynew.model.SectionJson;
import cn.vi1zen.zhihudailynew.model.SectionsJson;
import cn.vi1zen.zhihudailynew.model.StartImageJson;
import cn.vi1zen.zhihudailynew.model.StoryExtra;
import cn.vi1zen.zhihudailynew.model.ThemeJson;
import cn.vi1zen.zhihudailynew.model.ThemesJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vi1zen on 2017/3/13.
 */

public class ZhiHuHttp {
    private static final String ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/";

    private static final ZhiHuHttp zhiHuHttp = new ZhiHuHttp();

    private OkHttpClient okHttpClient;

    private Retrofit retrofit;

    private ZhiHuApi zhiHuApi;

    private ZhiHuHttp(){
        if(zhiHuHttp == null){
            synchronized (ZhiHuHttp.this){
                okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30,TimeUnit.SECONDS)
                        .readTimeout(30,TimeUnit.SECONDS).build();
                retrofit = new Retrofit.Builder()
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl(ZHIHU_BASE_URL)
                        .build();
                zhiHuApi = retrofit.create(ZhiHuApi.class);
            }
        }
    }

    public static ZhiHuHttp getZhiHuHttp(){
        return zhiHuHttp;
    }


    public void getDailies(Subscriber<DailiesJson> subscriber) {
        Observable observable = zhiHuApi.getDailies();
        toSubscribe(observable, subscriber);
    }

    public void getNews(Subscriber<DailyJson> subscriber, String id) {
        Observable observable = zhiHuApi.getNews(id);
        toSubscribe(observable, subscriber);
    }

    public void getStoryExtra(Subscriber<StoryExtra> subscriber, String id) {
        Observable observable = zhiHuApi.getStoryExtra(id);
        toSubscribe(observable, subscriber);
    }

    public void getDailiesBefore(Subscriber<DailiesJson> subscriber, String date) {
        Observable observable = zhiHuApi.getDailiesBefore(date);
        toSubscribe(observable, subscriber);
    }

    public void getHot(Subscriber<HotJson> subscriber) {
        Observable observable = zhiHuApi.getHot();
        toSubscribe(observable, subscriber);
    }

    public void getThemes(Subscriber<ThemesJson> subscriber) {
        Observable observable = zhiHuApi.getThemes();
        toSubscribe(observable, subscriber);
    }

    public void getTheme(Subscriber<ThemeJson> subscriber, String id) {
        Observable observable = zhiHuApi.getTheme(id);
        toSubscribe(observable, subscriber);
    }

    public void getSpecials(Subscriber<SectionsJson> subscriber) {
        Observable observable = zhiHuApi.getSections();
        toSubscribe(observable, subscriber);
    }

    public void getSpecial(Subscriber<SectionJson> subscriber, String id) {
        Observable observable = zhiHuApi.getSection(id);
        toSubscribe(observable, subscriber);
    }
    public void getSpecialBefore(Subscriber<SectionJson> subscriber, String id, String timestamp) {
        Observable observable = zhiHuApi.getSectionBefore(id, timestamp);
        toSubscribe(observable, subscriber);
    }

    public void getStartImage(Subscriber<StartImageJson> subscriber){
        Observable observable  =zhiHuApi.getStartImage();
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    private void toSubscribe(Observable o, Subscriber s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}

package cn.vi1zen.zhihudailynew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.DailyJson;
import cn.vi1zen.zhihudailynew.model.StoryExtra;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.net.UrlConstants;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.util.HtmlUtil;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Destiny on 2017/3/17.
 */

public class NewsDetailActivity extends BaseActivity {
    private StoryExtra storyExtra;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSource)
    TextView tvSource;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.view_bg)
    View viewBg;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.webView)
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_detail_main);
        ButterKnife.bind(this);

        final int id = getIntent().getIntExtra("ID", 0);

        //在展开的时候标题文字的外观
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedDisappearAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        toolbar.inflateMenu(R.menu.new_detail_menu);//设置右上角的填充菜单
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemShare:
                        Toast.makeText(NewsDetailActivity.this, "点击分享", Toast.LENGTH_SHORT).show();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "发现一篇好文章分享给你，地址:" + String.format(UrlConstants.STORY_SHARE, id));
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case R.id.itemComment:
                        Toast.makeText(NewsDetailActivity.this, "点击评论", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DailyDetailActivity.this, CommentsActivity.class);
//                        intent.putExtra("id", id);
//                        intent.putExtra("storyExtra", storyExtra);
//                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        getDetail(id);
        getStoryExtra(id);
    }

    private void getDetail(final int id) {
        Subscriber subscriber = new Subscriber<DailyJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(DailyJson dailyJson) {
                webView.loadData(HtmlUtil.createHtmlData(dailyJson), HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                tvTitle.setText(dailyJson.getTitle());
                tvSource.setText(dailyJson.getImageSource());
                if (dailyJson.getRecommenders() == null) {
                    collapsingToolbarLayout.setTitle("并没有推荐者");
                } else {
                    collapsingToolbarLayout.setTitle(dailyJson.getRecommenders().size() + "个推荐者");
                    toolbar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(NewsDetailActivity.this, "点击ToolBar", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(DailyDetailActivity.this, RecommendersActivity.class);
//                            intent.putExtra("id", id);
//                            startActivity(intent);
                        }
                    });
                }

                Glide.with(NewsDetailActivity.this).load(dailyJson.getImage()).placeholder(R.mipmap.liukanshan).into(ivImage);
//                if(!TextUtils.isEmpty(dailyJson.getImage())) {
//                    Glide.with(NewsDetailActivity.this).load(dailyJson.getImage()).placeholder(R.mipmap.liukanshan).into(ivImage);
//                }else{
//                    // TODO
//                    ivImage.setVisibility(View.GONE);
//                    viewBg.setVisibility(View.GONE);
//                }
            }
        };

        ZhiHuHttp.getZhiHuHttp().getNews(subscriber, String.valueOf(id));
    }

    private void getStoryExtra(final int id) {
        Subscriber subscriber = new Subscriber<StoryExtra>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "Subscriber onError()");
            }

            @Override
            public void onNext(StoryExtra storyExtra) {
                NewsDetailActivity.this.storyExtra = storyExtra;
            }
        };

        ZhiHuHttp.getZhiHuHttp().getStoryExtra(subscriber, String.valueOf(id));
    }

    /**
     * 获取新闻
     *
     * @param id
     */
    @Deprecated
    private void getNewsOld(final int id) {
        //获取文章内容
        Observable.create(new Observable.OnSubscribe<DailyJson>() {

            @Override
            public void call(Subscriber<? super DailyJson> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.NEWS, id));
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        DailyJson dailyJson = new Gson().fromJson(json, DailyJson.class);
                        subscriber.onNext(dailyJson);
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
                .subscribe(new Subscriber<DailyJson>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(DailyJson dailyJson) {
                        webView.loadData(HtmlUtil.createHtmlData(dailyJson), HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                        tvTitle.setText(dailyJson.getTitle());
                        tvSource.setText(dailyJson.getImageSource());
                        if (dailyJson.getRecommenders() == null) {
                            collapsingToolbarLayout.setTitle("并没有推荐者");
                        } else {
                            collapsingToolbarLayout.setTitle(dailyJson.getRecommenders().size() + "个推荐者");
                            toolbar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Toast.makeText(NewsDetailActivity.this, "点击ToolBar", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(DailyDetailActivity.this, RecommendersActivity.class);
//                                    intent.putExtra("id", id);
//                                    startActivity(intent);
                                }
                            });
                        }
                        Glide.with(NewsDetailActivity.this).load(dailyJson.getImage()).placeholder(R.mipmap.liukanshan).into(ivImage);
                    }
                });
    }

    /**
     * 获取长评论数,点赞总数,短评论数,评论总数
     *
     * @param id
     */
    @Deprecated
    private void getStoryExtraOld(final int id) {
        Observable.create(new Observable.OnSubscribe<StoryExtra>() {

            @Override
            public void call(Subscriber<? super StoryExtra> subscriber) {
                try {
                    Response response = OkHttpSync.get(String.format(UrlConstants.STORY_EXTRA, id));
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        storyExtra = new Gson().fromJson(json, StoryExtra.class);
                        subscriber.onNext(storyExtra);
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
                .subscribe(new Subscriber<StoryExtra>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "Subscriber onError()");
                    }

                    @Override
                    public void onNext(StoryExtra storyExtra) {

                    }
                });
    }
}

package cn.vi1zen.zhihudailynew.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.StartImageJson;
import cn.vi1zen.zhihudailynew.net.OkHttpAsync;
import cn.vi1zen.zhihudailynew.net.ZhiHuApi;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.tool.Constants;
import cn.vi1zen.zhihudailynew.util.ResUtil;
import cn.vi1zen.zhihudailynew.util.SP;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by Destiny on 2017/3/10.
 */

public class SplashActivity extends Activity {

    private static final String START_IMAGE_JSON_URL = "http://news-at.zhihu.com/api/7/prefetch-launch-images/1080*1920";
    private String img_url = "";
    private String img_id = "";
    private static final String START_IMAGE_FILE = "startImage";
    private MyAsyncTask myAsyncTask;
    private ImageView imageView;
    private ProgressBar progressBar;

    private static final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!SP.getBoolean(SP.SPLASH, true)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.iv_start);
//        imageView.setBackgroundResource(R.mipmap.star);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        getStartImage();
        setAnimation();
    }

    private void setAnimation() {
        imageView.setPivotX(imageView.getWidth()*0.5f);
        imageView.setPivotY(imageView.getHeight()*0.5f);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageView,"scaleX",1,1.25f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageView,"scaleY",1,1.25f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000).setStartDelay(1000);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.playTogether(objectAnimatorX,objectAnimatorY);
        set.start();
    }

    private void getStartImage() {
        Subscriber subscriber = new Subscriber<StartImageJson>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e,"Subscriber onError()");
            }

            @Override
            public void onNext(final StartImageJson startImageJson) {
                //如果本地存的图片就是最新的图片,那么不用下载更新
                /*OkHttpAsync.get(startImageJson.getUrl(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Logger.e(e,"连接错误！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        OkHttpAsync.saveFile(response, Constants.STORAGE_DIR, START_IMAGE_FILE);
                    }
                });*/

//                Log.i("IMAGE","startImageJson.getCreatives().get(0).getUrl() = " + startImageJson.getCreatives().get(0).getUrl());
                img_url = startImageJson.getCreatives().get(0).getUrl();
                img_id = startImageJson.getCreatives().get(0).getId();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(Constants.STORAGE_DIR,img_id + ".jpg");

                        if(file.exists()){
                            Glide.with(SplashActivity.this)
                                    .load(file)
                                    .centerCrop()
                                    .into(imageView);
                        }else{
                            Log.i("IMAGE","下载图片");
                            Glide.with(SplashActivity.this)
                                    .load(img_url)
                                    .centerCrop()
                                    .into(new GlideDrawableImageViewTarget(imageView) {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                            super.onResourceReady(resource, animation);
                                            Bitmap bmp = ResUtil.drawableToBitmap(resource);
                                            ResUtil.saveImage(bmp, img_id + ".jpg");
                                        }
                                    });
                        }


                    }
                });
            }
        };

        //这个的返回不能在UI线程中执行,应该是IO线程
        ZhiHuHttp.getZhiHuHttp().getStartImage(subscriber);
    }

    class MyAsyncTask extends AsyncTask<String,Integer,Bitmap>{

        @Override
            protected Bitmap doInBackground(String... strings) {
            Log.i("IMAGE","进入doInBackground...");
            Bitmap bitmap = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                System.out.println("AsyncTask传入的参数为："+strings[0]);
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // 设置文件类型
                conn.setRequestProperty("Content-Type", "application/octet-stream");
//                conn.setRequestProperty("Charset","UTF-8");
                conn.setConnectTimeout(10000);
                conn.setDoInput(true);
                conn.connect();
                long sum = conn.getContentLength();
                Log.i("IMAGE","文件总长度："+sum);
                if(conn.getResponseCode() == 200){
                    Log.i("IMAGE","连接成功...");
                    is = conn.getInputStream();
//                    int sum = is.available();//不能使用此方法，会报错
                    Log.i("IMAGE","连接成功..."+is);
                    byte[] buf = new byte[1024];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int len = 0;
                    long writeLen = 0;
                    while((len = is.read(buf))!=-1){
                        writeLen+=len;
                        Log.i("IMAGE","已下载："+writeLen);
                        Integer writePercent =(int) ((writeLen/((double)sum))*100);
                        Log.i("IMAGE","下载进度为："+writePercent);
                        //更新进度条
                        publishProgress(writePercent);
                        baos.write(buf, 0 ,len);
                    }



                    byte[] bytes = baos.toByteArray();
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
                    Log.i("IMAGE","连接成功..bitmap."+bitmap);
                }else{
                    Log.i("IMAGE",conn.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null){
                    conn.disconnect();
                }
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            imageView.setBackgroundResource(R.mipmap.star);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i("IMAGES",values[0].toString());
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);
            setAnimation();
        }

    }
}

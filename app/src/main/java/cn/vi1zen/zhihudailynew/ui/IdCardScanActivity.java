package cn.vi1zen.zhihudailynew.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.net.OkHttpAsync;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.net.ZhiHuHttp;
import cn.vi1zen.zhihudailynew.tool.Constants;
import cn.vi1zen.zhihudailynew.util.ImageUtils;
import cn.vi1zen.zhihudailynew.view.DrawImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vi1zen on 2017/6/15.
 */

public class IdCardScanActivity extends Activity{
    private static final String TAG = "IdCardScanActivity";
    private DrawImageView mDrawIV;
    private SurfaceView surfaceview;
    private Camera camera;
    private Bitmap mBitmap;
    private boolean isPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//把屏幕设置成横屏
        setContentView(R.layout.activity_id_card_scan);
        surfaceview = (SurfaceView) findViewById(R.id.previewSV);
        SurfaceHolder holder = surfaceview.getHolder();
        holder.setKeepScreenOn(true);// 屏幕常亮
        holder.addCallback(new MySurfaceCallback());
        holder.lockCanvas();
        //绘制矩形的ImageView
        mDrawIV = (DrawImageView) findViewById(R.id.drawIV);
        ImageButton takePhoto = (ImageButton) findViewById(R.id.photoImgBtn);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, myPictureCallback);
            }
        });

        surfaceview.setOnTouchListener(new View.OnTouchListener() {
            //点击 surfaceview 聚焦
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                camera.autoFocus(myAutoFocusCallback);
                return false;
            }
        });


    }



    private Camera.PictureCallback myPictureCallback = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //保存图片
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            if(null != data){
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                camera.stopPreview();
                isPreview = false;
            }
            //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。图片竟然不能旋转了，故这里要旋转下
            Matrix matrix = new Matrix();
//            matrix.postRotate((float)90.0);
            Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);

            //旋转后rotaBitmap是960×1280.预览surfaview的大小是540×800
            //将960×1280缩放到540×800
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            Bitmap sizeBitmap = Bitmap.createScaledBitmap(rotaBitmap, width, height, true);
//            Bitmap rectBitmap = Bitmap.createBitmap(sizeBitmap, 100, 200, 300, 300);//截取


            //保存图片到sdcard
            if(null != sizeBitmap)
            {
                saveJpeg(sizeBitmap);
                finish();
            }

            //再次进入预览
//            camera.startPreview();
//            isPreview = true;
        }
    };

    private Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //聚焦后的操作
        }
    };

    private final class MySurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //当surface的格式或大小发生改变，这个方法就被调用
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {   // surfaceview创建之后，就去打开相机
                camera = Camera.open();
                if(camera == null){
                    Log.e(TAG, "surfaceCreated: camera is null");
                    return;
                }
                Camera.Parameters params = camera.getParameters();
//                camera.setDisplayOrientation(90);//相机旋转90度
                camera.setPreviewDisplay(surfaceview.getHolder());
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    }

    /*给定一个Bitmap，进行保存*/
    public void saveJpeg(Bitmap bm){
        File saveFile = Environment.getExternalStorageDirectory();
        long dataTake = System.currentTimeMillis();
        String jpegName = dataTake +".jpg";
        Log.i("ZEN_TEST", "saveJpeg:jpegName--" + jpegName);
        File jpegFile = new File(saveFile,jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegFile);

            //          //如果需要改变大小(默认的是宽960×高1280),如改成宽600×高800
            //          Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            fout.flush();
            fout.close();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("app_id",Constants.TENCENT_YOUTU_APPID);
//                File file = new File(savePath,jpegName);
                jsonObject.put("image",ImageUtils.GetImageStr(jpegFile));
                jsonObject.put("card_type","0");//身份证图片类型，0-正面，1-反面
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody formBody = FormBody.create(MediaType.parse("text/json"),jsonObject.toString());
            OkHttpSync.postParams(Constants.YOUTU_URL, formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG,"信息上传错误");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    IdCardScanActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"识别结果为 == " + str,Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.d(TAG, "onResponse: 。。。。。。。。。。。。。" + str);
                }
            });

            Log.i(TAG, "saveJpeg：存储完毕！");
        } catch (IOException e) {
            Log.i(TAG, "saveJpeg:存储失败！");
            e.printStackTrace();
        }
    }
}

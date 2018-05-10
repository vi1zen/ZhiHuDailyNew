package cn.vi1zen.zhihudailynew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.util.ArrayList;

import cn.vi1zen.zhihudailynew.R;

public class GalleryActivity extends TakePhotoActivity {

    private ImageView imageView;
    private TakePhoto takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        imageView = (ImageView) findViewById(R.id.iv_showPhoto);
        //获取takePhoto实例
        takePhoto = getTakePhoto();
        //takePhoto配置选项
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        //获取photo
        takePhoto.onPickFromGallery();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
//        Intent intent=new Intent(this,ResultActivity.class);
//        intent.putExtra("images",images);
//        startActivity(intent);
        for (int i = 0,j = images.size(); i < j; i++) {
            Glide.with(GalleryActivity.this).load(new File(images.get(i).getCompressPath())).into(imageView);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    private void configTakePhotoOption(TakePhoto takePhoto){
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);//使用自带相册
        builder.setCorrectImage(true);//使用当前相册
        takePhoto.setTakePhotoOptions(builder.create());
    }


    private void configCompress(TakePhoto takePhoto){
//        takePhoto.onEnableCompress(null,false);
        int maxSize= 102400;//单位B
        int width= 500;//单位px
        int height= 500;//单位px
        boolean showProgressBar=true;//显示压缩进度条
        boolean enableRawFile = true;//拍照压缩后是否保存原图
        CompressConfig config;
//        if(){//自带压缩
//            config=new CompressConfig.Builder()
//                    .setMaxSize(maxSize)
//                    .setMaxPixel(width>=height? width:height)
//                    .enableReserveRaw(enableRawFile)
//                    .create();
//        }else {//鲁班压缩
            LubanOptions option=new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config=CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
//        }
        takePhoto.onEnableCompress(config,showProgressBar);


    }
    private CropOptions getCropOptions(){
        int height= 800;
        int width= 800;
        boolean withWonCrop=true;//自带裁剪工具

        CropOptions.Builder builder=new CropOptions.Builder();

//        builder.setAspectX(width).setAspectY(height);//
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }
}

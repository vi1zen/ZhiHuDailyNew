package cn.vi1zen.zhihudailynew.ui;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youtu.Youtu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.tool.Constants;
import cn.vi1zen.zhihudailynew.util.ImageUtils;
import cn.vi1zen.zhihudailynew.util.ResUtil;
import cn.vi1zen.zhihudailynew.util.YoutuUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vi1zen on 2017/6/16.
 * 注意控制图片大小
 */

public class IdCardOcrActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "IdCardOcrActivity";
    private ImageView imageView;
    private TextView tv;
    private Uri imageUri;
    private static final int CHOOSE_PHOTO = 1;
    private FormBody.Builder builder;
    private FormBody formBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_ocr);
        ((Button)findViewById(R.id.btn_choose_image)).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.iv_showImg);
        tv = (TextView) findViewById(R.id.tv_id_card_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_choose_image:
                //file用于存放照片
                File imageFile = new File(Environment.getExternalStorageDirectory(), "outputImage.jpg");
                if (imageFile.exists()) {
                    imageFile.delete();
                }
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
                //转换成Uri
                imageUri = Uri.fromFile(imageFile);
                //开启选择界面
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                //设置可以缩放
                intent.putExtra("scale", true);
                //设置可以裁剪
                intent.putExtra("crop", true);
                intent.setType("image/*");
                //设置输出位置
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //开始选择
                startActivityForResult(intent, CHOOSE_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*// 从拍照界面返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 设置intent为启动裁剪程序
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    // 设置Data为刚才的imageUri和Type为图片类型
                    intent.setDataAndType(imageUri, "image*//*");
                    // 设置可缩放
                    intent.putExtra("scale", true);
                    // 设置输出地址为imageUri
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    // 开启裁剪,设置requestCode为CROP_PHOTO
                    startActivityForResult(intent, CROP_PHOTO);
                }

                break;
            // 从裁剪界面返回
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap;
                    try {
                        //通过BitmapFactory将imageUri转化成bitmap
                        bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        //设置显示
                        mPhotoImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;*/
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    handleImageOnKitkat(data);
                }
                break;
            default:
                break;
        }
    }

    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果不是document类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        displayImage(imagePath); // 根据图片路径显示图片
        System.err.println(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            imageView.setImageBitmap(bitmap);
            startLoading();
            File file = new File(imagePath);
            if(!file.exists()){
                Log.i("IdCard","找不到图片文件");
            }else {
                Log.i("IdCard","imgPath = " + imagePath );
            }
                final String imgPath = imagePath;

                final Youtu youtu = new Youtu(Constants.TENCENT_YOUTU_APPID,Constants.TENCENT_YOUTU_SECRETID,
                    Constants.TENCENT_YOUTU_SECRETKEY,YoutuUtils.API_YOUTU_END_POINT);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObject = youtu.IdCardOcr(imgPath,0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("IdCard","jsonObject = "+ jsonObject.toString());
                                    try {
                                        String resultStr = "姓名:" + jsonObject.getString("name") + "\n"
                                                + "性别:" + jsonObject.getString("sex") + "\n"
                                                + "民族:" + jsonObject.getString("nation") + "\n"
                                                + "出生日期:" + jsonObject.getString("birth") + "\n"
                                                + "地址:" + jsonObject.getString("address") + "\n"
                                                + "身份证号:" + jsonObject.getString("id") + "\n";
                                        tv.setText(resultStr);
//                                        stopLoading();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (KeyManagementException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}

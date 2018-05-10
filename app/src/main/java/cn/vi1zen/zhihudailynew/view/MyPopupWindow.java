package cn.vi1zen.zhihudailynew.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.tool.Constants;
import cn.vi1zen.zhihudailynew.util.ResUtil;

/**
 * Created by vi1zen on 2017/4/8.
 */

public class MyPopupWindow extends PopupWindow implements View.OnClickListener{

    private Activity context;
    private ImageView share_img;
    private Bitmap bitmap;
    private String imgPath;
    private static final String SCREENSHOT_NAME = "screenShot_img.jpg";
    public MyPopupWindow(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.share_popup_window,null);
        share_img = (ImageView) view.findViewById(R.id.iv_share_img);
        //设置view
        setContentView(view);
        //设置宽度
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置高度
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置窗体可点击
        setFocusable(true);
        setOutsideTouchable(true);
        //刷新状态
        update();
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000000000);
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        //点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(R.anim.popup_in);
        //设置点击事件
        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.iv_weixin).setOnClickListener(this);
        view.findViewById(R.id.iv_qq).setOnClickListener(this);
        view.findViewById(R.id.iv_weibo).setOnClickListener(this);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            // 以下拉方式显示popupwindow
//            showAsDropDown(parent, 50, 20);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                dismiss();
                break;
            case R.id.iv_weixin:
                Toast.makeText(context,"正在分享到微信...",Toast.LENGTH_SHORT).show();
                String imgUrl = ResUtil.insertImageToSystem(context,imgPath);//微信需要将图片添加到系统图库才能分享
                Intent intent = new Intent(Intent.ACTION_SEND);
                ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                intent.setComponent(comp);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgUrl));
                context.startActivity(intent);
                dismiss();
                break;
            case R.id.iv_qq:
                Toast.makeText(context,"正在分享到QQ...",Toast.LENGTH_SHORT).show();
                Intent qqIntent = new Intent(Intent.ACTION_SEND);
                ComponentName compt = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                qqIntent.setComponent(compt);
                qqIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgPath));
                qqIntent.setType("image/*");
                context.startActivity(qqIntent);
                dismiss();
                break;
            case R.id.iv_weibo:
                Toast.makeText(context,"正在分享到朋友圈...",Toast.LENGTH_SHORT).show();
                String imgUrl1 = ResUtil.insertImageToSystem(context,imgPath);//微信需要将图片添加到系统图库才能分享
                Intent weiboIntent = new Intent(Intent.ACTION_SEND);
                ComponentName compt1 = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                weiboIntent.setComponent(compt1);
                weiboIntent.putExtra("Kdescription", "来自知新日报");
                weiboIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgUrl1));
                weiboIntent.setType("image/*");
                context.startActivity(weiboIntent);
                dismiss();
                break;
        }
    }

    public void addImage(Bitmap bitmap,String imgPath){
        this.bitmap = bitmap;
        this.imgPath = imgPath;
        share_img.setImageBitmap(bitmap);
        update();
    }
}

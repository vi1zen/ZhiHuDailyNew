package cn.vi1zen.zhihudailynew.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.widget.TextView;

import cn.vi1zen.zhihudailynew.R;

/**
 * Created by vi1zen on 2017/6/16.
 */

public class LoadingDialog extends Dialog{

    private RotateLoading rotateLoading;
    private TextView tv_loading_desc;

    public LoadingDialog(@NonNull Context context) {
        this(context,R.style.Dialog_Fullscreen);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.dialog_loading);
        tv_loading_desc = (TextView) findViewById(R.id.tv_loading_desc);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateLoading);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return
     */
    public LoadingDialog setMessage(String message) {
        tv_loading_desc.setText(message);
        return this;
    }

    @Override
    public void show() {
        super.show();
        if(!rotateLoading.isStart()){
            rotateLoading.start();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if(rotateLoading.isStart()){
            rotateLoading.stop();
        }
    }
}

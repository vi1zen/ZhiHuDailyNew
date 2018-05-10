package cn.vi1zen.zhihudailynew.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.vi1zen.zhihudailynew.view.LoadingDialog;

import static anetwork.channel.http.NetworkSdkSetting.context;

/**
 * Created by vi1zen on 2017/3/10.
 */

public class BaseActivity extends Activity {
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(context).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void startLoading(){
        loadingDialog = new LoadingDialog(this).setMessage("正在加载...");
        loadingDialog.show();
    }

    public void startLoading(String message){
        loadingDialog = new LoadingDialog(this).setMessage(message);
        loadingDialog.show();
    }

    public void stopLoading(){
        if(loadingDialog !=null){
            loadingDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog !=null){
            loadingDialog.dismiss();
        }
    }
}

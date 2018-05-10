package cn.vi1zen.zhihudailynew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;

/**
 * Created by vi1zen on 2017/3/30.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.btn_sina)
    Button btn_sina;
    @BindView(R.id.btn_tencent)
    Button btn_tencent;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        btn_sina.setOnClickListener(this);
        btn_tencent.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_sina:
                Toast.makeText(this,"功能暂未开发！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_tencent:
                Toast.makeText(this,"功能暂未开发！",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

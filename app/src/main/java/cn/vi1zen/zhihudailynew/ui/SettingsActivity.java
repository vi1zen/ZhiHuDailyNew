package cn.vi1zen.zhihudailynew.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.tool.Constants;
import cn.vi1zen.zhihudailynew.util.FileUtil;
import cn.vi1zen.zhihudailynew.util.SP;
import cn.vi1zen.zhihudailynew.util.T;
import cn.vi1zen.zhihudailynew.view.SettingSwitchCompat;



/**
 * Created by vi1zen on 2017/3/21.
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.settingSplash)
    SettingSwitchCompat settingSwitchCompat;
    @BindView(R.id.tvClearCache)
    TextView tvClearCache;
    @BindView(R.id.tvCacheSize)
    TextView tvCacheSize;
    @BindView(R.id.rlClearCache)
    RelativeLayout rlClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        settingSwitchCompat.setChecked(SP.getBoolean(SP.SPLASH, true));
        settingSwitchCompat.setOnClickListener(this);
        tvCacheSize.setText(FileUtil.formetFileSize(FileUtil.getFileSize(new File(Constants.STORAGE_DIR))));
        rlClearCache.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingSplash:
                SP.put(SP.SPLASH, settingSwitchCompat.isChecked());
                break;
            case R.id.rlClearCache:
                FileUtil.delete(new File(Constants.STORAGE_DIR));
                T.s(rlClearCache, "缓存清理完成!");
                tvCacheSize.setText("0B");
                break;
            default:
                break;
        }
    }
}

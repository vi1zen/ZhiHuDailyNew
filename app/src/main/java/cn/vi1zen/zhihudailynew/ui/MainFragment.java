package cn.vi1zen.zhihudailynew.ui;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.umeng.analytics.MobclickAgent;

import cn.vi1zen.zhihudailynew.R;

/**
 * Created by vi1zen on 2017/3/15.
 */
public abstract class MainFragment extends Fragment {

    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        Log.i("ZEN", "MainFragment.java - onPause() ---------- Fragment:" + this.getClass().getName() );
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        Log.i("ZEN", "MainFragment.java - onResume() ---------- Fragment:" + this.getClass().getName() );
    }

    /**
     * Refresh
     * 如果是当前页面=点击页面, 则平滑移动到头部
     */
    public void refresh() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * Called when a fragment will be displayed
     * 将会显示的淡入动画
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     * 将会隐藏的淡出动画
     */
    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }

}

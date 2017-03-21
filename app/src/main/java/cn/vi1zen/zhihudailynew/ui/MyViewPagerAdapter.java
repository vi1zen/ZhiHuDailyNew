package cn.vi1zen.zhihudailynew.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.vi1zen.zhihudailynew.tool.FragmentPagerAdapter;
import cn.vi1zen.zhihudailynew.ui.MainFragment;

/**
 * Created by Destiny on 2017/3/14.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<MainFragment> mFragments = new ArrayList<MainFragment>();

    private MainFragment currentFragment;

    public MyViewPagerAdapter(FragmentManager fm, ArrayList<MainFragment> fragments) {
        super(fm);
        mFragments.addAll(fragments);
    }

    @Override
    public MainFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if(getCurrentFragment() != object){
            currentFragment = (MainFragment) object;
        }
    }

    public MainFragment getCurrentFragment(){
        return currentFragment;
    }
}

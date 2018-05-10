package cn.vi1zen.zhihudailynew.tool;

import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.vi1zen.zhihudailynew.model.Daily;

/**
 * Created by vi1zen on 2017/3/24.
 */

public class CarouselImageAdapter extends PagerAdapter {
    private ArrayList<Daily> daily;//轮播图信息
    private Fragment fragment;
    private List<ImageView> imageViews;

    public CarouselImageAdapter(ArrayList<Daily> daily) {
        this.daily = daily;
    }

    @Override
    public int getCount() {
        return daily.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        ViewParent vp =  imageView.getParent();
        if(vp != null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(imageView);
        }
        //上面这些语句必须加上，如果不加的话，就会产生则当用户滑到第四个的时候就会触发这个异常
        //原因是我们试图把一个有父组件的View添加到另一个组件。
        //这里我刚才写东西的时候想到一点，也遇到这个问题，就是这个函数他会对viewPager的每一个页面进
        //绘制的时候就会被调用，当第一次滑动完毕之后，当在此调用此方法就会在此添加view可是我们之前已经
        //添加过了,这时在添加，就会重复，所以才会有需要移除异常的报告
        container.addView(imageView);
        return imageView;
    }

    public void addList(ArrayList<Daily> daily){
        this.daily = daily;
    }

    public void initData(){
        imageViews = new ArrayList<ImageView>();

    }

}

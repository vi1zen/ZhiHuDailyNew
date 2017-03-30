package cn.vi1zen.zhihudailynew.tool;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.TopStory;

/**
 * Created by Destiny on 2017/3/29.
 */

public class RollPagerAdapter extends LoopPagerAdapter{
    private ArrayList<TopStory> top_stories = new ArrayList<>();

//    private int[] imgs = {
//                    R.mipmap.avater,
//                    R.mipmap.avater,
//                    R.mipmap.avater,
//                    R.mipmap.avater
//        };
    public RollPagerAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        String imgUrl = top_stories.get(position).getImage();//图片的url
        Log.i("IMG","imgUrl-------------------->"+imgUrl);

//        ImageView view = new ImageView(container.getContext());
//        Glide.with(container.getContext()).load(imgUrl).placeholder(R.mipmap.liukanshan).centerCrop().into(view);

        View viewGroup = LayoutInflater.from(container.getContext()).inflate(R.layout.header_viewpager,container,false);
        ImageView iv = (ImageView) viewGroup.findViewById(R.id.iv);
        TextView tv = (TextView) viewGroup.findViewById(R.id.tv);
        tv.setText(top_stories.get(position).getTitle());
        Glide.with(container.getContext()).load(imgUrl).placeholder(R.mipmap.liukanshan).centerCrop().into(iv);
        Log.i("IMG","top_stories.get(position).getTitle()----------->"+top_stories.get(position).getTitle());
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewGroup;
    }

    @Override
    public int getRealCount() {
//            return imgs.length;
        return top_stories.size();
    }

    public void addTopData(ArrayList<TopStory> top_stories){
        this.top_stories.addAll(top_stories);
        Log.i("IMG","top_stories---------------->"+top_stories.size());
    }

}

package cn.vi1zen.zhihudailynew.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.Daily;
import cn.vi1zen.zhihudailynew.model.TopStory;

public class RollViewPager extends ViewPager {
    private Context context;
    private int currentItem;
    private List<Daily> topStories;
    private ArrayList<View> dots;
    private int[] resImageIds;
    private int dot_focus_resId;
    private int dot_normal_resId;
    private OnPagerClickCallback onPagerClickCallback;
    private boolean isShowResImage = false;
    MyOnTouchListener myOnTouchListener;
    ViewPagerTask viewPagerTask;
    private PagerAdapter adapter;

    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();
    private int abc = 1;
    private float mLastMotionX;
    private float mLastMotionY;

    private float firstDownX;
    private float firstDownY;
    private boolean flag = false;

    private long start = 0;

    public class MyOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            curP.x = event.getX();
            curP.y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    start = System.currentTimeMillis();
                    handler.removeCallbacksAndMessages(null);
                    // 记录按下时候的坐标
                    // 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
                    downP.x = event.getX();
                    downP.y = event.getY();
                    // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
                    // getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    handler.removeCallbacks(viewPagerTask);
                    Log.i("d", (curP.x - downP.x) + "----" + (curP.y - downP.y));
                    // if (Math.abs(curP.x - downP.x) > Math.abs(curP.y - downP.y)
                    // && (getCurrentItem() == 0 || getCurrentItem() == getAdapter()
                    // .getCount() - 1)) {
                    // getParent().requestDisallowInterceptTouchEvent(false);
                    // } else {
                    // getParent().requestDisallowInterceptTouchEvent(false);
                    // }
                    // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
                    break;
                case MotionEvent.ACTION_CANCEL:
                    // getParent().requestDisallowInterceptTouchEvent(false);
                    startRoll();
                    break;
                case MotionEvent.ACTION_UP:
                    downP.x = event.getX();
                    downP.y = event.getY();
                    long duration = System.currentTimeMillis() - start;
                    if (duration <= 500 && downP.x == curP.x) {
                        onPagerClickCallback.onPagerClick(currentItem);
                    } else {
                    }
                    startRoll();
                    break;
            }
            return true;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                abc = 1;
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (abc == 1) {
                    if (Math.abs(x - mLastMotionX) < Math.abs(y - mLastMotionY)) {
                        abc = 0;
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            Log.e("rollViewPager", "run:" + currentItem);
            currentItem = (currentItem + 1)
                    % (isShowResImage ? resImageIds.length : topStories.size());
            if (handler != null)
                handler.obtainMessage().sendToTarget();
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<RollViewPager> mWeakReference;

        public MyHandler(RollViewPager rollViewPager) {
            mWeakReference = new WeakReference<>(rollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_STOP) {
                removeCallbacksAndMessages(null);
                return;
            }
            RollViewPager rollViewPager = mWeakReference.get();
            Log.e("rollViewPager", "handleMessage：" + rollViewPager.currentItem);
            rollViewPager.setCurrentItem(rollViewPager.currentItem);
            rollViewPager.startRoll();

        }
    }

    private Handler handler = new MyHandler(this);


    public RollViewPager(Context context, ArrayList<View> dots,
                         int dot_focus_resId, int dot_normal_resId,
                         OnPagerClickCallback onPagerClickCallback) {
        super(context);
        this.context = context;
        this.dots = dots;
        this.dot_focus_resId = dot_focus_resId;
        this.dot_normal_resId = dot_normal_resId;
        this.onPagerClickCallback = onPagerClickCallback;
        viewPagerTask = new ViewPagerTask();
        myOnTouchListener = new MyOnTouchListener();
    }


    public List<Daily> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<Daily> topStories) {
        isShowResImage = false;
        this.topStories = topStories;
    }

    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    public ArrayList<View> getDots() {
        return dots;
    }

    public void setDots(ArrayList<View> dots) {
        this.dots = dots;
    }

    public void setResImageIds(int[] resImageIds) {
        isShowResImage = true;
        this.resImageIds = resImageIds;
    }

    private boolean hasSetAdapter = false;

    /**
     *
     */

    public void startRoll() {
        if (!hasSetAdapter) {
            hasSetAdapter = true;
            this.addOnPageChangeListener(new MyOnPageChangeListener());
            adapter = new ViewPagerAdapter();
            this.setAdapter(adapter);
        }
        Log.e("rollViewPager", "startRoll：" + currentItem);
        handler.postDelayed(viewPagerTask, 3000);
    }

    private static final int CODE_STOP = -1;

    public void stopRoll() {
        handler.sendEmptyMessage(CODE_STOP);
    }

    class MyOnPageChangeListener implements OnPageChangeListener {
        int oldPosition = 0;

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            if (dots != null && dots.size() > 0) {
                dots.get(position).setBackgroundResource(dot_focus_resId);
                dots.get(oldPosition).setBackgroundResource(dot_normal_resId);
            }
            oldPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return isShowResImage ? resImageIds.length : topStories.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(context, R.layout.viewpager_item, null);
            view.setOnTouchListener(myOnTouchListener);
            ImageView ivTop = (ImageView) view.findViewById(R.id.iv_top);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_top_title);
            tvTitle.setText(topStories.get(position).getTitle());
            if (isShowResImage) {
                ivTop.setImageResource(resImageIds[position]);
            } else {
                Glide.with(context).load(topStories.get(position).getImage()).into(ivTop);
//                ImageLoader.getInstance().display(context, ivTop, topStories.get(position).getImage());
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    public interface OnPagerClickCallback {
        void onPagerClick(int position);
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent arg0) {
    // // 每次进行onTouch事件都记录当前的按下的坐标
    // curP.x = arg0.getX();
    // curP.y = arg0.getY();
    //
    // if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
    // // 记录按下时候的坐标
    // // 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
    // downP.x = arg0.getX();
    // downP.y = arg0.getY();
    // // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
    // getParent().requestDisallowInterceptTouchEvent(true);
    // }
    //
    // if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
    // // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
    // getParent().requestDisallowInterceptTouchEvent(true);
    // }
    //
    // if (arg0.getAction() == MotionEvent.ACTION_UP) {
    // // 在up时判断是否按下和松手的坐标为一个点
    // // 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
    // if (downP.x == curP.x && downP.y == curP.y) {
    // // onSingleTouch();
    // return true;
    // }
    // }
    //
    // return super.onTouchEvent(arg0);
    // }

}

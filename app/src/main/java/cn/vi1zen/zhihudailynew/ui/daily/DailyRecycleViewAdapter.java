package cn.vi1zen.zhihudailynew.ui.daily;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.jude.rollviewpager.hintview.TextHintView;

import java.util.ArrayList;
import java.util.List;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.DailiesJson;
import cn.vi1zen.zhihudailynew.model.Daily;
import cn.vi1zen.zhihudailynew.model.TopStory;
import cn.vi1zen.zhihudailynew.tool.OnItemClickListener;
import cn.vi1zen.zhihudailynew.tool.RollPagerAdapter;
import cn.vi1zen.zhihudailynew.ui.NewsDetailActivity;

/**
 * Created by vi1zen on 2017/3/15.
 */

public class DailyRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Daily> stories = new ArrayList<>();
    private ArrayList<TopStory> topStories = new ArrayList<>();
    private Fragment fragment;
    private final RecyclerView mRecyclerView;
    private boolean isLoading = false;
    private int lastVisibleItemPosition;
    private int visibleItemCount;
    private int totalItemCount;
    private int footViewCount = 1;
    private int HeaderViewCount = 1;
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_FOOT = 1;
    private static final int VIEW_TITLE = 2;
    private static final int VIEW_HEADER = 3;
    private View headerView;
    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 2;
    private LoadMoreDataListener mMoreDataListener;

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Daily daily = stories.get(position);
            Intent intent = new Intent(fragment.getActivity(),NewsDetailActivity.class);
            intent.putExtra("ID",daily.getId());
            fragment.getActivity().startActivity(intent);

        }
    };

    public DailyRecycleViewAdapter(final Fragment fragment, RecyclerView recyclerView) {
        this.fragment = fragment;
        this.mRecyclerView = recyclerView;
        if(recyclerView.getLayoutManager() instanceof RecyclerView.LayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //mRecyclerView添加滑动事件监听
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    /*visibleItemCount = linearLayoutManager.getChildCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    totalItemCount = linearLayoutManager.getItemCount();
                    Log.i("LOADMORE", "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition +
                            "isLoad = " + isLoading);
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        //此时是刷新状态
                        if (mMoreDataListener != null)
                            mMoreDataListener.loadMoreData();
                        isLoading = true;
                    }*/
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
//                    visibleItemCount = linearLayoutManager.getChildCount();
//                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    Log.i("LOADMORE", "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition +
//                            "isLoad = " + isLoading);
//                    //!isLoading &&
//                    if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1) {
//                        if (mMoreDataListener != null) {
//                            mMoreDataListener.loadMoreData();
//                        }
//                        isLoading = true;
//
//                    }
                   // RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
                   // RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                   if(!mRecyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE ){
                       Log.i("LOADMORE","已经滑动到底部了。。。"+ (mMoreDataListener == null));
                       if (mMoreDataListener != null) {
                            mMoreDataListener.loadMoreData();
                        }
                   }
                }
            });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_ITEM){
            return new TitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_item,parent,false));
        }else if(viewType == VIEW_HEADER){
            return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_carousel_image,parent,false));
        }else{
            return new FootHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        position = getRealPosition(holder);
        Log.i("IMG","position-------->"+position);
        if(holder instanceof TitleHolder){
            Daily daily = stories.get(position);
            if(daily.getImages().size() != 0){
                Glide.with(fragment).load(daily.getImages().get(0)).into(((TitleHolder)holder).imageView);
            }
            ((TitleHolder)holder).textView.setText(daily.getTitle());
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(listener);
        }else if(holder instanceof HeaderHolder){
            RollPagerView rollPagerView = ((HeaderHolder)holder).rollPagerView;
            rollPagerView.setVisibility(View.VISIBLE);
            RollPagerAdapter rollPagerAdapter = new RollPagerAdapter(((HeaderHolder)holder).rollPagerView);
            rollPagerView.setHintView(new IconHintView(fragment.getActivity(),R.drawable.dot_focus,R.drawable.dot_blur));
            rollPagerView.setHintView(new ColorPointHintView(fragment.getActivity(),Color.WHITE, Color.GRAY));
            rollPagerAdapter.addTopData(topStories);
            rollPagerView.setAdapter(rollPagerAdapter);
            rollPagerView.setOnItemClickListener(new com.jude.rollviewpager.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    TopStory topStory = topStories.get(position);
                    Intent intent = new Intent(fragment.getActivity(),NewsDetailActivity.class);
                    intent.putExtra("ID",topStory.getId());
                    fragment.getActivity().startActivity(intent);
                }
            });
        }else{
            if(((FootHolder)holder).progressBar != null){
                ((FootHolder)holder).progressBar.setIndeterminate(true);
            }
        }

    }

    public void addList(ArrayList<Daily> stories) {
        this.stories.addAll(stories);
    }
    public void addListToHeader(final ArrayList<Daily> stories, ArrayList<TopStory> topStories) {
        this.stories.addAll(stories);
        if(topStories != null){
            this.topStories.addAll(0,topStories);
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(getItemCount());
                notifyItemRangeInserted(getItemCount(),stories.size());
                Log.i("LOADMORE", "addListToHeader: ....");
            }
        });
    }
    public void addData(ArrayList<TopStory> topStories){
        this.topStories.addAll(0,topStories);
    }
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }
    public boolean getLoading() {
        return isLoading;
    }

    //因为添加了一个headerView，其他的Item的position都往后移了一位，所以要进行动态计算
    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headerView == null ? position:position - HeaderViewCount;
    }
    // headerView的setter
    public void setHeaderView(RollPagerView headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return stories.size() + footViewCount + HeaderViewCount;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return VIEW_HEADER;
        }else if(position == stories.size() + HeaderViewCount){
            return VIEW_FOOT;
        }else{
            return VIEW_ITEM;
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;
        public TitleHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_title);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        private RollPagerView rollPagerView;
        public HeaderHolder(View itemView) {
            super(itemView);
            rollPagerView = (RollPagerView) itemView.findViewById(R.id.rollPagerView);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder{

        private ProgressBar progressBar;
        public FootHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public interface LoadMoreDataListener {
        void loadMoreData();
    }

    //加载更多监听方法
    public void setOnMoreDataLoadListener(LoadMoreDataListener onMoreDataLoadListener) {
        mMoreDataListener = onMoreDataLoadListener;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        isLoading = false;
    }
}

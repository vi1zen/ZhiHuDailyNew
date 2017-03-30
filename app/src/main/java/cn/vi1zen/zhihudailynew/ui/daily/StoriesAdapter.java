package cn.vi1zen.zhihudailynew.ui.daily;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.AdapterBean;
import cn.vi1zen.zhihudailynew.model.Daily;
import cn.vi1zen.zhihudailynew.model.Story;
import cn.vi1zen.zhihudailynew.ui.BaseRVAdapter;
import cn.vi1zen.zhihudailynew.ui.NewsDetailActivity;
import cn.vi1zen.zhihudailynew.util.CommonUtil;
import cn.vi1zen.zhihudailynew.view.RollViewPager;

/**
 * @author Destiny
 * 2017-3-26 14:59:50
 */
public class StoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected View mHeaderView;

    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_TITLE = 1;
    protected static final int TYPE_FOOTER = 2;
    protected static final int TYPE_HEADER = 3;
    protected static final int TYPE_NO_IMG_ITEM = 4;
    protected static final int TYPE_EMPTY_VIEW = 5;

    private Fragment mFragment;

    private List<Daily> stories = new ArrayList<Daily>();

    private List<Daily> top_stories = new ArrayList<Daily>();

    public StoriesAdapter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
//        mData.clear();
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    /**
     * 清空数据
     */
    public void clearData() {
        stories.clear();
        mHeaderView = null;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_item, parent, false);
            return new StoryViewHolder(itemView);
        } else if (viewType == TYPE_TITLE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item, parent, false);
            return new TitleViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item, parent, false);
            return new FooterViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Daily daily = stories.get(position);
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.tvTitleItem.setText(daily.getTime());
        } else if (holder instanceof StoryViewHolder) {
            StoryViewHolder storyViewHolder = (StoryViewHolder) holder;
            // 改变已读状态
            storyViewHolder.textView.setTextColor(
                    storyViewHolder.textView.getResources().getColor(
                            daily.getIsRead() ? R.color.textReader : android.R.color.black));

            storyViewHolder.textView.setText(daily.getTitle());
            Glide.with(mFragment).load(daily.getImages().get(0)).into(storyViewHolder.imageView);
//            ImageLoader.getInstance().display(mFragment, storyViewHolder.ivThumbnailImage, story.getImages().get(0));
            // 显示多图标志
//            ActivityUtils.setVisible(story.isMultipic(), storyViewHolder.ivMultiPic);

        }
    }

    @Override
    public int getItemCount() {
        return stories.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        }else if(position == 1){
            return TYPE_TITLE;
        }else{
            return TYPE_ITEM;
        }
    }

    public void addList(ArrayList<Daily> stories) {
        this.stories.addAll(stories);
    }

    public void addListToHeader(ArrayList<Daily> stories) {
        this.top_stories.addAll(0, stories);
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title_item)
        TextView tvTitleItem;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_title)
        ImageView imageView;
        @BindView(R.id.tv_title)
        TextView textView;

        public StoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_loading)
        TextView tvLoadingItem;
        @BindView(R.id.progressBar)
        ContentLoadingProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NoImgStoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView textView;

        public NoImgStoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

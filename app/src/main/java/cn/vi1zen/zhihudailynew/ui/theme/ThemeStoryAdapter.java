package cn.vi1zen.zhihudailynew.ui.theme;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.ThemeStory;
import cn.vi1zen.zhihudailynew.tool.OnItemClickListener;
import cn.vi1zen.zhihudailynew.ui.NewsDetailActivity;

/**
 * Created by Destiny on 2017/3/20.
 */

public class ThemeStoryAdapter extends RecyclerView.Adapter<ThemeStoryAdapter.StoryViewHolder>{
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_TEXT_IMAGE = 2;
    private Activity activity;
    private ArrayList<ThemeStory> stories = new ArrayList<ThemeStory>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            ThemeStory themeStory = stories.get(pos);
            Intent intent = new Intent(activity, NewsDetailActivity.class);
            intent.putExtra("ID", themeStory.getId());
            activity.startActivity(intent);
        }
    };

    public ThemeStoryAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addList(ArrayList<ThemeStory> stories) {
        this.stories.addAll(stories);
    }
    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == TYPE_TEXT_IMAGE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_story_with_image,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_story,parent,false);
        }
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {
        ThemeStory story = stories.get(position);
        holder.textView.setText(story.getTitle());
        if(story.getImages() != null){
            Glide.with(activity).load(story.getImages().get(0)).into(holder.imageView);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return stories.get(position).getImages() == null ? TYPE_TEXT:TYPE_TEXT_IMAGE;
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.iv)
        ImageView imageView;
        @BindView(R.id.tvTitle)
        TextView textView;

        public StoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

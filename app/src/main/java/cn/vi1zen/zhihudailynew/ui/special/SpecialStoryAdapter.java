package cn.vi1zen.zhihudailynew.ui.special;

import android.app.Activity;
import android.content.Intent;
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
import cn.vi1zen.zhihudailynew.model.SectionStory;
import cn.vi1zen.zhihudailynew.tool.OnItemClickListener;
import cn.vi1zen.zhihudailynew.ui.NewsDetailActivity;

/**
 * Created by vi1zen on 2017/3/20.
 */

public class SpecialStoryAdapter extends RecyclerView.Adapter<SpecialStoryAdapter.StoryHolder>{
    private Activity activity;
    private ArrayList<SectionStory> stories = new ArrayList<>();

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            SectionStory sectionStory = stories.get(pos);
            Intent intent = new Intent(activity, NewsDetailActivity.class);
            intent.putExtra("ID", sectionStory.getId());
            activity.startActivity(intent);
        }
    };

    public SpecialStoryAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addList(ArrayList<SectionStory> stories) {
        this.
                stories.addAll(stories);
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_story_with_image, parent, false));
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        SectionStory sectionStory = stories.get(position);
        holder.tvTitle.setText(sectionStory.getTitle());
        holder.tvTime.setText(sectionStory.getDate());
        if (sectionStory.getImages() != null) {
            Glide.with(activity).load(sectionStory.getImages().get(0)).placeholder(R.mipmap.avater).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    class StoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvTime)
        TextView tvTime;

        public StoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }
}

package cn.vi1zen.zhihudailynew.ui.daily;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.Daily;
import cn.vi1zen.zhihudailynew.tool.OnItemClickListener;
import cn.vi1zen.zhihudailynew.ui.NewsDetailActivity;

/**
 * Created by Destiny on 2017/3/15.
 */

public class DailyRecycleViewAdapter extends RecyclerView.Adapter<DailyRecycleViewAdapter.TitleHolder> {
    private ArrayList<Daily> stories = new ArrayList<>();
    private Fragment fragment;
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
//            Toast.makeText(fragment.getActivity().getApplicationContext(),"跳转详情页...",Toast.LENGTH_SHORT).show();
            Daily daily = stories.get(position);
            Intent intent = new Intent(fragment.getActivity(),NewsDetailActivity.class);
            intent.putExtra("ID",daily.getId());
            fragment.getActivity().startActivity(intent);

        }
    };

    public DailyRecycleViewAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public TitleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_item,parent,false));
    }

    @Override
    public void onBindViewHolder(TitleHolder holder, int position) {
        Daily daily = stories.get(position);
        if(daily.getImages().size() != 0){
            Glide.with(fragment).load(daily.getImages().get(0)).into(holder.imageView);
        }
        holder.textView.setText(daily.getTitle());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);
    }

    public void addList(ArrayList<Daily> stories) {
        this.stories.addAll(stories);
    }

    public void addListToHeader(ArrayList<Daily> stories) {
        this.stories.addAll(0, stories);
    }

    @Override
    public int getItemCount() {
        return stories.size();
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
}

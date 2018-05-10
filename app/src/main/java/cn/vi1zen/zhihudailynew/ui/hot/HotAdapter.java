package cn.vi1zen.zhihudailynew.ui.hot;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.Hot;
import cn.vi1zen.zhihudailynew.tool.OnItemClickListener;
import cn.vi1zen.zhihudailynew.ui.NewsDetailActivity;

/**
 * Created by vi1zen on 2017/3/19.
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HotViewHolder>{

    private ArrayList<Hot> hots = new ArrayList<Hot>();
    private Fragment fragment;
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Hot hot = hots.get(position);
            Intent intent = new Intent(fragment.getActivity(), NewsDetailActivity.class);
            intent.putExtra("ID", hot.getNewsId());
            fragment.getActivity().startActivity(intent);
        }
    };
    /**
     *  构造函数，初始化相关数据
     */
    public HotAdapter(ArrayList<Hot> hots,Fragment fragment) {
        this.fragment = fragment;
        this.hots = hots;
    }
    public HotAdapter(Fragment fragment) {
        this.fragment = fragment;
    }
    public void addList(ArrayList<Hot> hot) {
        this.hots.addAll(hot);
    }
    /**
     * 创建ItemView
     */
    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_item,parent,false);
        return new HotViewHolder(itemView);
    }

    /**
     * 将数据与item绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(HotViewHolder holder, int position) {
        Hot hot = hots.get(position);
        if(!TextUtils.isEmpty(hot.getThumbnail())){
            Glide.with(fragment).load(hot.getThumbnail()).asBitmap().into(holder.imageView);
        }
        holder.textView.setText(hot.getTitle());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return hots.size();
    }

    class HotViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;
        public HotViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_title);
            textView = (TextView) itemView.findViewById(R.id.tv_title);

        }
    }
}

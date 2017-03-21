package cn.vi1zen.zhihudailynew.ui.special;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.model.Section;
import cn.vi1zen.zhihudailynew.tool.OnItemClickListener;

/**
 * Created by Destiny on 2017/3/20.
 */

public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.SpecialHolder>{
    private Fragment fragment;
    private ArrayList<Section> sections = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Section section = sections.get(pos);
            Intent intent = new Intent(fragment.getActivity(), SpecialActivity.class);
            intent.putExtra("id", section.getId());
            intent.putExtra("name", section.getName());
            fragment.getActivity().startActivity(intent);
        }
    };

    public SpecialAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public SpecialAdapter(ArrayList<Section> sections, Fragment fragment) {
        this.sections = sections;
        this.fragment = fragment;
    }

    public void addList(ArrayList<Section> sections) {
        this.sections.addAll(sections);
    }


    @Override
    public SpecialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpecialHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.special_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SpecialHolder holder, int position) {
        Section section = sections.get(position);
        holder.tvTitle.setText(section.getName());
        holder.tvDescription.setText(section.getDescription());
        if (!TextUtils.isEmpty(section.getThumbnail())) {
            Glide.with(fragment).load(section.getThumbnail()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    public class SpecialHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        public SpecialHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

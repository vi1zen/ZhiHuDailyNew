package cn.vi1zen.zhihudailynew.ui.theme;

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
import cn.vi1zen.zhihudailynew.model.Theme;
import cn.vi1zen.zhihudailynew.tool.OnItemClickListener;

/**
 * Created by vi1zen on 2017/3/18.
 */
public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeHolder> {

    private Fragment fragment;
    private ArrayList<Theme> themes = new ArrayList<>();
    private OnItemClickListener listener = new OnItemClickListener(){
        @Override
        public void onItemClick(int pos) {
            Theme theme = themes.get(pos);
            Intent intent = new Intent(fragment.getActivity(), ThemeActivity.class);
            intent.putExtra("ID", theme.getId());
            fragment.getActivity().startActivity(intent);
        }
    };
    public ThemeAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void addList(ArrayList<Theme> themes) {
        this.themes.addAll(themes);
    }

    @Override
    public ThemeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ThemeHolder holder, int position) {
        Theme theme = themes.get(position);
        holder.tvName.setText(theme.getName());
        holder.tvDescription.setText(theme.getDescription());
        if (!TextUtils.isEmpty(theme.getThumbnail())) {
            Glide.with(fragment).load(theme.getThumbnail()).into(holder.iv);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    class ThemeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_title)
        ImageView iv;
        @BindView(R.id.tv_title)
        TextView tvName;
        @BindView(R.id.tv_desc)
        TextView tvDescription;

        public ThemeHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }


}

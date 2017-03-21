package cn.vi1zen.zhihudailynew.tool;

import android.view.View;


public abstract class OnItemClickListener implements View.OnClickListener, View.OnLongClickListener{

    public void onItemClick(int position) {

    }

    public void onItemLongClick(int position) {

    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        onItemClick(position);
    }

    @Override
    public boolean onLongClick(View view) {
        int position = (int) view.getTag();
        onItemLongClick(position);
        return false;
    }
}

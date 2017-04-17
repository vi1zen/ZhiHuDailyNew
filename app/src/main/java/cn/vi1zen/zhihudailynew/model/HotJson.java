package cn.vi1zen.zhihudailynew.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class HotJson {

    @SerializedName("recent")
    private ArrayList<Hot> hots;

    public ArrayList<Hot> getHots() {
        return hots;
    }

    public void setHots(ArrayList<Hot> hots) {
        this.hots = hots;
    }
}

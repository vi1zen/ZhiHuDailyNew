package cn.vi1zen.zhihudailynew.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/24.
 */
public class DailiesJson {

    private String date;
    private ArrayList<Daily> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Daily> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Daily> stories) {
        this.stories = stories;
    }
}

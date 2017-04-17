package cn.vi1zen.zhihudailynew.model;

import java.util.ArrayList;

/**
 * Created by Desiny on 2017/3/15.
 */
public class DailiesJson {

    private String date;
    private ArrayList<Daily> stories;
    private ArrayList<TopStory> top_stories;

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

    public ArrayList<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(ArrayList<TopStory> top_stories) {
        this.top_stories = top_stories;
    }
}

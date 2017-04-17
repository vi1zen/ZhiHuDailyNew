package cn.vi1zen.zhihudailynew.model;

import java.util.ArrayList;

public class ThemesJson {

    private int limit;
    private ArrayList<String> subscribed;
    private ArrayList<Theme> others;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ArrayList<String> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(ArrayList<String> subscribed) {
        this.subscribed = subscribed;
    }

    public ArrayList<Theme> getOthers() {
        return others;
    }

    public void setOthers(ArrayList<Theme> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "ThemesJson{" +
                "limit=" + limit +
                ", subscribed='" + subscribed + '\'' +
                ", others=" + others +
                '}';
    }
}

package cn.vi1zen.zhihudailynew.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/22.
 */

public class SectionsJson {

    @SerializedName("data")
    private ArrayList<Section> sections;

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }
}

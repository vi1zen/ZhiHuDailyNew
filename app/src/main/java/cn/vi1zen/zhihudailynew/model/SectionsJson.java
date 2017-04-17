package cn.vi1zen.zhihudailynew.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

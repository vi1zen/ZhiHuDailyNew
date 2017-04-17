package cn.vi1zen.zhihudailynew.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SectionStory {

    private int id;
    private ArrayList<String> images;
    private String title;
    private String date;
    @SerializedName("display_date")
    private String displayDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    @Override
    public String toString() {
        return "SectionStory{" +
                "id=" + id +
                ", images=" + images +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", displayDate='" + displayDate + '\'' +
                '}';
    }
}

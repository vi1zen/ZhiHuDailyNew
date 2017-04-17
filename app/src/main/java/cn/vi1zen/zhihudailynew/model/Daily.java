package cn.vi1zen.zhihudailynew.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Daily implements Serializable {

    private int id;

    private int type;

    @SerializedName("ga_prefix")
    private String time;

    private String title;

    @SerializedName("mutlipic")
    private boolean multiPic;

    private ArrayList<String> images;

    private String image;//轮播图

    private boolean isRead;

    private int showType;

    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMultiPic() {
        return multiPic;
    }

    public void setMultiPic(boolean multiPic) {
        this.multiPic = multiPic;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "id=" + id +
                ", type=" + type +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", multiPic=" + multiPic +
                ", images=" + images +
                ", image='" + image + '\'' +
                '}';
    }
}

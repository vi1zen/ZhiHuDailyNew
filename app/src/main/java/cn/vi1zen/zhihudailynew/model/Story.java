package cn.vi1zen.zhihudailynew.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author White
 * Date 2016/8/13
 * Time 14:46
 */
public class Story implements Serializable {

    private String title;// 新闻标题
    private List<String> images; // 图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
    private String ga_prefix; // 供 Google Analytics 使用
    private int type; // 作用未知
    private int id; // url 与 share_url 中最后的数字（应为内容的 id）
    private boolean multipic; // 消息是否包含多张图片（仅出现在包含多图的新闻中）

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }

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
}

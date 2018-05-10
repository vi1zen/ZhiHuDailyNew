package cn.vi1zen.zhihudailynew.model;

import java.io.Serializable;

/**
 * Created by vi1zen on 2017/4/7.
 */

public class StartImage implements Serializable{

    private String url;
    private String id;
    private String start_time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
}

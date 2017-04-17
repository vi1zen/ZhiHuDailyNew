package cn.vi1zen.zhihudailynew.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class CommentJson {

    @SerializedName("comments")
    private ArrayList<Comment> comments;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}

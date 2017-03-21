package cn.vi1zen.zhihudailynew.net;

import cn.vi1zen.zhihudailynew.model.CommentJson;
import cn.vi1zen.zhihudailynew.model.DailiesJson;
import cn.vi1zen.zhihudailynew.model.DailyJson;
import cn.vi1zen.zhihudailynew.model.HotJson;
import cn.vi1zen.zhihudailynew.model.RecommendsJson;
import cn.vi1zen.zhihudailynew.model.SectionJson;
import cn.vi1zen.zhihudailynew.model.SectionsJson;
import cn.vi1zen.zhihudailynew.model.StartImageJson;
import cn.vi1zen.zhihudailynew.model.StoryExtra;
import cn.vi1zen.zhihudailynew.model.ThemeJson;
import cn.vi1zen.zhihudailynew.model.ThemesJson;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface ZhiHuApi {

    @GET("4/start-image/1080*1776")
    Observable<StartImageJson> getStartImage();

    @GET("4/news/latest")
    Observable<DailiesJson> getDailies();

    @GET("4/news/before/{date}")
    Observable<DailiesJson> getDailiesBefore(@Path("date") String date);

    @GET("4/news/{id}")
    Observable<DailyJson> getNews(@Path("id") String id);

    @GET("4/story-extra/{id}")
    Observable<StoryExtra> getStoryExtra(@Path("id") String id);

    @GET("4/story/{id}/short-comments")
    Observable<CommentJson> getShortComments(@Path("id") String id);

    @GET("4/story/{id}/long-comments")
    Observable<CommentJson> getLongComments(@Path("id") String id);

    @GET("4/themes")
    Observable<ThemesJson> getThemes();

    @GET("4/theme/{id}")
    Observable<ThemeJson> getTheme(@Path("id") String id);

    @GET("3/news/hot")
    Observable<HotJson> getHot();

    @GET("3/sections")
    Observable<SectionsJson> getSections();

    @GET("3/section/{id}")
    Observable<SectionJson> getSection(@Path("id") String id);

    @GET("3/section/{id}/before/{timestamp}")
    Observable<SectionJson> getSectionBefore(@Path("id") String id, @Path("timestamp") String timestamp);

    @GET("4/story/{id}/recommenders")
    Observable<RecommendsJson> getRecommends(@Path("id") String id);

    @GET("4/editor/{id}/profile-page/android")
    Observable getEditor(@Path("id") String id);

}

package cn.vi1zen.zhihudailynew.net;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.vi1zen.zhihudailynew.tool.Constants;
import cn.vi1zen.zhihudailynew.util.YoutuUtils;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vi1zen on 2017/3/15.
 */
public class OkHttpSync {

    public static final MediaType MEDIA_TYPE_NONE = MediaType.parse("application/x-; charset=utf-8");
    public static final MediaType MEDIA_TYPE_UNKNOWN = MediaType.parse("application/octet-stream; charset=utf-8");
    private static final String TAG = "OkHttpSync";
    private static final OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

    }

    public static Response get(String url) throws IOException {
        return get(client, url);
    }

    public static void postParams(String url, RequestBody requestBody, Callback callback) throws IOException {
        postParams(client, url, requestBody,callback);
    }

    public static Response postFile(String url, File file) throws IOException {
        return postFile(client, url, file);
    }

    public static Response postFiles(String url, FileInput... fileInputs) throws IOException {
        return postFiles(client, url, fileInputs);
    }

    public static Response get(OkHttpClient okHttpClient, String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newCall(request).execute();
    }

    public static void postParams(OkHttpClient okHttpClient, String url, RequestBody requestBody, Callback callback) throws IOException {
        StringBuffer mySign = new StringBuffer("");
        YoutuUtils.appSign(Constants.TENCENT_YOUTU_APPID,Constants.TENCENT_YOUTU_SECRETID,
                Constants.TENCENT_YOUTU_SECRETKEY,2592000,"278250644",mySign);
        Log.i("IdCard","Authorization = "+mySign.toString());
        Request request = new Request.Builder()
                .url(url)
//                .header("accept","*/*")
//                .header("Authorization",mySign.toString())
                .removeHeader("Authorization")
                .addHeader("Authorization",mySign.toString())
                .addHeader("Host","api.youtu.qq.com")
                .addHeader("Content-Length", String.valueOf(requestBody.contentLength()))
                .addHeader("Content-Type","text/json")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static Response postFile(OkHttpClient okHttpClient, String url, File file) throws IOException {
        RequestBody requestBody = null;
        requestBody = RequestBody.create(MEDIA_TYPE_UNKNOWN, file);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return okHttpClient.newCall(request).execute();
    }

    public static Response postFiles(OkHttpClient okHttpClient, String url, FileInput... fileInputs) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (FileInput fileInput : fileInputs) {
            builder.addFormDataPart(fileInput.key, fileInput.name, RequestBody.create(MEDIA_TYPE_UNKNOWN, fileInput.file));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return okHttpClient.newCall(request).execute();
    }

    public static class FileInput {
        public String key;
        public String name;
        public File file;

        public FileInput(String key, String name, File file) {
            this.key = key;
            this.name = name;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", name='" + name + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}

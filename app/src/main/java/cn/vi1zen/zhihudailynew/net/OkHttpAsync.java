package cn.vi1zen.zhihudailynew.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Destiny on 2017/3/13.
 * 异步网络请求
 */

public class OkHttpAsync {

    private static final OkHttpClient okHttpClient;

    //创建okHttpClient实例
    static {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS).build();
    }

    public static void get(String url, Callback callback){
        get(okHttpClient,url,callback);
    }

    /**
     *  创建请求，发起请求,不带参数
     *
     */
    private static void get(OkHttpClient okHttpClient, String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postParams(String url, FormBody formBody, Callback callback){
        postParams(okHttpClient,url,formBody,callback);
    }

    /**
      *  创建请求，发起请求,带参数
      */
    private static void postParams(OkHttpClient okHttpClient, String url, FormBody formBody, Callback callback) {
        Request request = new Request.Builder().url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 存储网络下载文件
     * @param response 响应
     * @param sourceFileDir 存储目录
     * @param sourceFileName 存储文件名
     */
    public static File saveFile(Response response, String sourceFileDir, String sourceFileName){
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();

            //totalLength：文件总长度
            long totalLength = response.body().contentLength();

            //writeLenth：文件已下载长度
            long writeLenth = 0;

            File dir = new File(sourceFileDir);
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir,sourceFileName);

            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1){
                writeLenth+=len;
                fos.write(buf,0,len);
            }
            fos.flush();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            response.body().close();
            //关闭输入流
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭输出流
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}

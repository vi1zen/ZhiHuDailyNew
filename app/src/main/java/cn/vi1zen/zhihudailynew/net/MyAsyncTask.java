package cn.vi1zen.zhihudailynew.net;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by vi1zen on 2017/3/30.
 */

public class MyAsyncTask extends AsyncTask<String,Void,String>{
    @Override
    protected String doInBackground(String... strings) {
        OkHttpAsync.get(strings[0], new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("LOADMORE","MyAsyncTask Fail...");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultStr = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    String date = jsonObject.getString("date");
                    JSONArray stories = jsonObject.getJSONArray("stories");
                    for(int i=0;i<stories.length();i++){
                        JSONObject obj = (JSONObject) stories.get(i);
                    }
                    JSONArray top_stories = jsonObject.getJSONArray("top_stories");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

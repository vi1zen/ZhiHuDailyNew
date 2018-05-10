package cn.vi1zen.zhihudailynew.util;

import android.util.Base64;
import android.util.Log;

import com.youtu.Youtu;
import com.youtu.sign.Base64Util;
import com.youtu.sign.HMACSHA1;
import com.youtu.sign.YoutuSign;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Timestamp;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.vi1zen.zhihudailynew.net.OkHttpSync;
import cn.vi1zen.zhihudailynew.tool.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vi1zen on 2017/6/16.
 */

public class YoutuUtils {

    /**
     * u=10000&a=2011541224&k=AKID2ZkOXFyDRHZRlbPo93SMtzVY79kpAdGP&e=1432970065&t=1427786065&r=270494647&f=
     * u为开发者创建应用时的QQ号
     * a为用户的AppID
     * k为用户的SecretID
     * t为当前时间戳，是一个符合UNIX Epoch时间戳规范的数值，单位为秒
     * e为此签名的凭证有效期，是一个符合UNIX Epoch时间戳规范的数值，单位为秒, e应大于t, 生成的签名在 t 到 e 的时间内 都是有效的. 如果是0, 则生成的签名只有再t的时刻是有效的.
     * r为随机串，无符号10进制整数，用户需自行生成，最长10位。
     * f为空
     * 拼接有效签名串的结果,下文称之为orignal
     * 签名方法Sign= Base64(HMAC-SHA1(SecretKey, orignal) + original)
     * @return
     */
    private static final String TAG = "YoutuUtils";
    private static String u = "278250644";

    public static final String API_YOUTU_END_POINT = "http://api.youtu.qq.com/youtu/";
    public static final String API_YOUTU_CHARGE_END_POINT = "http://vip-api.youtu.qq.com/youtu/";
    public static final String API_TENCENTYUN_END_POINT = "https://youtu.api.qcloud.com/youtu/";
    private static int EXPIRED_SECONDS = 2592000;
    private String m_appid;
    private String m_secret_id;
    private String m_secret_key;
    private String m_end_point;
    private boolean m_not_use_https;
    JSONObject jsonObject = null;

    public YoutuUtils(String appid, String secret_id, String secret_key, String end_point) {
        this.m_appid = appid;
        this.m_secret_id = secret_id;
        this.m_secret_key = secret_key;
        this.m_end_point = end_point;
        this.m_not_use_https = !end_point.startsWith("https");
    }

    public RequestBody IdCardOcr(String image_path, int card_type) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();
        this.GetBase64FromFile(image_path, image_data);
        data.put("app_id",this.m_appid);
        data.put("image", image_data.toString());
        data.put("card_type", card_type);

        StringBuffer mySign = new StringBuffer("");
        data.put("Authorization", appSign(this.m_appid, this.m_secret_id, this.m_secret_key,
                System.currentTimeMillis() / 1000L + (long)EXPIRED_SECONDS, "", mySign));
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/json"),data.toString());
        return requestBody;
//        JSONObject respose = this.m_not_use_https?this.SendHttpRequest(data, "ocrapi/idcardocr"):this.SendHttpRequest(data, "ocrapi/idcardocr");

    }
    private void GetBase64FromFile(String filePath, StringBuffer base64) throws IOException {
        File imageFile = new File(filePath);
        if(imageFile.exists()) {
            FileInputStream in = new FileInputStream(imageFile);
            byte[] data = new byte[(int)imageFile.length()];
            in.read(data);
            in.close();
            base64.append(Base64Util.encode(data));
            Log.i("IdCard","图片路径存在 ");
        } else {
            Log.i("IdCard","图片路径不存在");
            throw new FileNotFoundException(filePath + " not exist");
        }
    }
    private JSONObject SendHttpRequest(JSONObject postData, String mothod) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer mySign = new StringBuffer("");
        YoutuSign.appSign(this.m_appid, this.m_secret_id, this.m_secret_key, System.currentTimeMillis() / 1000L + (long)EXPIRED_SECONDS, "", mySign);
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");
        URL url = new URL(this.m_end_point + mothod);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("user-agent", "youtu-java-sdk");
        connection.setRequestProperty("Authorization", mySign.toString());
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "text/json");
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        postData.put("app_id", this.m_appid);
        out.write(postData.toString().getBytes("utf-8"));
        out.flush();
        out.close();
        Log.i("IdCard","connection.getInputStream()前");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Log.i("IdCard","connection.getInputStream()后");
        StringBuffer resposeBuffer = new StringBuffer("");

        String lines;
        while((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            resposeBuffer.append(lines);
        }

        reader.close();
        connection.disconnect();
        JSONObject respose = new JSONObject(resposeBuffer.toString());
        Log.i("IdCard","respose = "+ respose.toString());
        return respose;
    }

    public static int appSign(String appId, String secret_id, String secret_key, long expired, String userid, StringBuffer mySign) {
        return appSignBase(appId, secret_id, secret_key, expired, "278250644", (String)null, mySign);
    }

    private static int appSignBase(String appId, String secret_id, String secret_key, long expired, String userid, String url, StringBuffer mySign) {
        if(!empty(secret_id) && !empty(secret_key)) {
            String puserid = "";
            if(!empty(userid)) {
                if(userid.length() > 64) {
                    return -2;
                }

                puserid = userid;
            }

            long now = System.currentTimeMillis() / 1000L;
            int rdm = Math.abs((new Random()).nextInt());
            String plain_text = "a=" + appId + "&k=" + secret_id + "&e=" + expired + "&t=" + now + "&r=" + rdm + "&u=" + puserid;
            byte[] bin = hashHmac(plain_text, secret_key);
            byte[] all = new byte[bin.length + plain_text.getBytes().length];
            System.arraycopy(bin, 0, all, 0, bin.length);
            System.arraycopy(plain_text.getBytes(), 0, all, bin.length, plain_text.getBytes().length);
            mySign.append(Base64Util.encode(all));
            return 0;
        } else {
            return -1;
        }
    }

    private static byte[] hashHmac(String plain_text, String accessKey) {
        try {
            return HMACSHA1.getSignature(plain_text, accessKey);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static boolean empty(String s) {
        return s == null || s.trim().equals("") || s.trim().equals("null");
    }

}

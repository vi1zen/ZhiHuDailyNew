package cn.vi1zen.zhihudailynew.tool;

import android.os.Environment;

import java.io.File;

import cn.vi1zen.zhihudailynew.App;
import cn.vi1zen.zhihudailynew.util.DateUtil;

/**
 * Created by vi1zen on 2017/3/13.
 */

public class Constants {
    public static final String STORAGE_DIR = Environment.getExternalStorageDirectory()+ File.separator + App.app.getPackageName() + File.separator;;
    public static final String FILE_NAME = "zh_start_img_"+ DateUtil.getCurrentDate() + ".jpg";

    //腾讯优图相关
    public static final String YOUTU_URL= "http://api.youtu.qq.com/youtu/ocrapi/idcardocr";
    public static final String TENCENT_YOUTU_APPID = "10085456";
    public static final String TENCENT_YOUTU_SECRETID = "AKIDbz7za0v4h5D5g1szc093j9KI0Sdbnkvq";
    public static final String TENCENT_YOUTU_SECRETKEY = "pPimbCXeWBbmhNo0wd7MM7duwiFZeU5Q";
}

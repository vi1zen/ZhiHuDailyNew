package cn.vi1zen.zhihudailynew.tool;

import android.os.Environment;

import java.io.File;

import cn.vi1zen.zhihudailynew.App;
import cn.vi1zen.zhihudailynew.util.DateUtil;

/**
 * Created by Destiny on 2017/3/13.
 */

public class Constants {
    public static final String STORAGE_DIR = Environment.getExternalStorageDirectory()+ File.separator + App.app.getPackageName() + File.separator;;
    public static String FILE_NAME = "zh_start_img_"+ DateUtil.getCurrentDate() + ".jpg";
}

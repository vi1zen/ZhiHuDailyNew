package cn.vi1zen.zhihudailynew.util;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.vi1zen.zhihudailynew.App;
import cn.vi1zen.zhihudailynew.tool.Constants;

/**
 * Created by Administrator on 2016/8/17 0012.
 */
public class ResUtil {

    public static String getString (int resId) {
        return App.app.getResources().getString(resId);
    }

    public static String getString(int format, int resId)
    {
        return String.format(App.app.getResources().getString(format), App.app.getResources().getString(resId));
    }

    public static String getString(int format, String content)
    {
        return String.format(App.app.getResources().getString(format), content);
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;
    }

    public static void saveImage(Bitmap bmp) {
        File appDir = new File(Constants.STORAGE_DIR);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, Constants.FILE_NAME);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

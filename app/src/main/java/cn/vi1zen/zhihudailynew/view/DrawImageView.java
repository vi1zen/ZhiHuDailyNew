package cn.vi1zen.zhihudailynew.view;

import android.content.Context;
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Paint.Style;  
import android.graphics.Rect;  
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
  
public class DrawImageView extends ImageView{

    private final int width;
    private final int height;

    public DrawImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
    }  
      
    Paint paint = new Paint();  
    {  
        paint.setAntiAlias(true);  
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.STROKE);  
        paint.setStrokeWidth(5.0f);//设置线宽
        paint.setAlpha(100);  
    };  
      
    @Override  
    protected void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub  
        super.onDraw(canvas);
//        canvas.drawRect(new Rect(width/8, height/6, width/8*7, height/6*5), paint);//绘制矩形(竖屏)
        canvas.drawRect(new Rect(width/6, height/8, width/6*5, height/8*7), paint);//绘制矩形（横屏）
    }  

}  
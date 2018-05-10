package cn.vi1zen.zhihudailynew.tool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by vi1zen on 2017/3/28.
 */

public class MyOrientationListener implements SensorEventListener {
    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mSensor;
    private OnOrientationListener onOrientationListener;
    //方向的X坐标
    private float lastX;

    public MyOrientationListener(Context mContext) {
        this.mContext = mContext;
    }

    public void start(){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager != null){
            //获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if(mSensor != null){
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION){
            float x = sensorEvent.values[SensorManager.DATA_X];
            if(Math.abs(x-lastX) > 1.0){
                if(onOrientationListener != null){
                    onOrientationListener.onOrientationChange(lastX);
                }
            }
            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public  interface  OnOrientationListener{
        void onOrientationChange(float x);
    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }
}

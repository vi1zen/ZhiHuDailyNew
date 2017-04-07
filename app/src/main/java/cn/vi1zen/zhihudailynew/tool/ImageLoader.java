package cn.vi1zen.zhihudailynew.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Destiny on 2017/4/5.
 */

public class ImageLoader {
    private static ImageLoader imageloader;
    /**
     * 图像缓存
     */
    private LruCache<String,Bitmap> bitmapLruCache;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 默认线程数量
     */
    private static final int DEFAULT_THREAD_COUNT = 1;
    /**
     * 加载策略
     * FIFO:先进先出
     * LIFO:后进先出
     */
    private enum LoadType{
        FIFO,LIFO
    }
    private LoadType loadType = LoadType.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    private Handler mUIHandler;

    public ImageLoader(int mthreadCount,LoadType loadType) {
        init(mthreadCount,loadType);
    }

    public static ImageLoader getInstance(){
        if(imageloader == null){//提高效率
            synchronized (ImageLoader.class){
                if(imageloader == null){
                    imageloader = new ImageLoader(DEFAULT_THREAD_COUNT,LoadType.LIFO);
                }
            }
        }
        return imageloader;
    }

    private void init(int mthreadCount,LoadType loadType){
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池取出一个任务执行
                        mThreadPool.execute(getTask());
                    }
                };
                Looper.loop();
            }
        };
        mPoolThread.start();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;
        bitmapLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {//测量bitmap的大小
                return value.getRowBytes()*value.getHeight();
            }
        };
        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(mthreadCount);
        mTaskQueue = new LinkedList<Runnable>();
        this.loadType = loadType;
    }

    /**
     * 根据path为imageview设置图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        if(mUIHandler == null){
            /**
             * 防止这个类在子线程中调用，导致UI Handler失效。
             * Handler默认构造函数中，会获取当前所在线程的Looper作为自己的Looper对象。
             * 所以，Handler在UI线程中初始化，获取的是UI的Looper，在子线程中初始化，获取的是子线程的Looper。
             */
            mUIHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    //获取到图片，为imageview回调设置图片
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    Bitmap bitmap = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;
                    //将path与getTag存储路径进行比较
                    if(imageView.getTag().toString().equals(path)){
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
        }
        //根据path在缓存中获取bitmap
        Bitmap bitmap = getBitmapFromLruCache(path);
        if(bitmap!=null){
            Message msg = new Message();
            ImageBeanHolder holder = new ImageBeanHolder();
            holder.bitmap = bitmap;
            holder.imageView = imageView;
            holder.path = path;
            msg.obj = holder;
            mUIHandler.sendMessage(msg);
        }else{
            addTasks(new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    /**
                     * 图片压缩
                     */
                    //1.获得图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2.压缩图片
                    Bitmap bitmap = decodeSampleBitmapFromPath(path,imageSize.width,imageSize.height);
                }
            });
        }
    }

    private void addTasks(Runnable runnable) {
        mTaskQueue.add(runnable);
        mPoolThreadHandler.sendEmptyMessage(0x210);
    }
    private Runnable getTask(){
        if(loadType == LoadType.FIFO){
            return mTaskQueue.removeFirst();
        }else{
            return mTaskQueue.removeLast();
        }
    }
    private Bitmap getBitmapFromLruCache(String key) {
        return bitmapLruCache.get(key);
    }
    private Bitmap decodeSampleBitmapFromPath(String path,int width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //获取图片的宽和高，但是图片不加载到内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        options.inSampleSize = caculateSampleSize(options,width,height);

        //使用获取到的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    private int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if(width > reqWidth || height > reqHeight){
            int widthRadio = Math.round(width*1.0f/reqWidth);
            int heightRadio = Math.round(height*1.0f/reqHeight);

            inSampleSize = Math.max(widthRadio,heightRadio);
        }
        return inSampleSize;
    }

    /**
     * 获得图片需要显示的大小
     * @param imageView
     */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize size = new ImageSize();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        DisplayMetrics metrics = imageView.getContext().getResources().getDisplayMetrics();

        int width = imageView.getWidth();
        if(width <= 0){
            width =lp.width;//获取imageView在layout中声明的宽度
        }
        if(width <= 0){
            width = imageView.getMaxWidth();
        }
        if(width <= 0){
            width = metrics.widthPixels;
        }

        int height = imageView.getHeight();
        if(height <= 0){
            height =lp.height;//获取imageView在layout中声明的宽度
        }
        if(height <= 0){
            height = imageView.getMaxHeight();
        }
        if(height <= 0){
            height = metrics.heightPixels;
        }
        size.width = width;
        size.height = height;
        return size;
    }
    private class ImageSize{
        int width;
        int height;
    }
    private class ImageBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}

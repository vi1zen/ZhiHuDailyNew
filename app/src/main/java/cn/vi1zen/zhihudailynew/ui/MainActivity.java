package cn.vi1zen.zhihudailynew.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.baidu.mapapi.map.BaiduMap;

import java.io.File;
import java.util.ArrayList;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.tool.Constants;
import cn.vi1zen.zhihudailynew.ui.daily.DailyMainFragment;
import cn.vi1zen.zhihudailynew.ui.hot.HotMainFragment;
import cn.vi1zen.zhihudailynew.ui.special.SpecialMainFragment;
import cn.vi1zen.zhihudailynew.ui.theme.ThemeMainFragment;
import cn.vi1zen.zhihudailynew.util.ResUtil;
import cn.vi1zen.zhihudailynew.view.MyPopupWindow;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,DrawerLayout.DrawerListener{

    private static final String TAG = "MainActivity";

    private static final int REQUEST_PERMISSION_CAMERA_CODE = 1;
    private static final int REQUEST_PERMISSION_STORAGE_CODE = 2;
    private DrawerLayout drawerLayout;
    private Toolbar toolBar;
    private ViewPager viewPager;
    private AHBottomNavigation bottomNavigation;//底部的BottomNavigation
    private NavigationView navigationView;
    private CircleImageView circleImageView;
    private int[] tabColors;
    private AHBottomNavigationAdapter navigationAdapter;
    private MyViewPagerAdapter myViewPagerAdapter;
    private MainFragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolBar,R.string.open_drawer,R.string.close_drawer);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle.syncState();
    }

    private void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.addDrawerListener(this);
        //侧滑栏view
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        //头部toolBar
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.inflateMenu(R.menu.over_flow_menu);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.share:
                        Bitmap bitmap = ResUtil.screenShot(MainActivity.this);
                        String imgPath = ResUtil.saveImage(bitmap,"zh_screenshot.jpg");

                        if("".equals(imgPath)){
                            File file = new File(Constants.STORAGE_DIR,"zh_screenshot.jpg");
                            imgPath = file.getAbsolutePath();
                        }
//                        String imgUrl = ResUtil.insertImageToSystem(MainActivity.this,imgPath);
                        MyPopupWindow mPopupWindow = new MyPopupWindow(MainActivity.this);
                        mPopupWindow.addImage(bitmap,imgPath);
                        mPopupWindow.showPopupWindow(toolBar);
                        break;
                    case R.id.setting:
                        //跳转到设置页面
                        MainActivity.this.startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                        break;
                    case R.id.about:
                        //跳转到关于页面
                        MainActivity.this.startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        break;
                }
                return true;//true表示事件不再传递
            }
        });

        //底部导航栏
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        navigationAdapter = new AHBottomNavigationAdapter(this,R.menu.bottom_nav_menu);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation,tabColors);
        bottomNavigation.setBehaviorTranslationEnabled(true);//重要属性 设置向上滑动时是否隐藏底部栏
        bottomNavigation.setAccentColor(R.color.zhihu_blue);//设置选中的颜色
        bottomNavigation.setInactiveColor(R.color.bottomnavigation_inactive);//设置闲置的颜色
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.bottomnavigation_bg));//设置背景颜色

        //viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);//限定预加载的页面个数
        DailyMainFragment dailyMainFragment = new DailyMainFragment();
        ThemeMainFragment themeMainFragment = new ThemeMainFragment();
        HotMainFragment hotMainFragment = new HotMainFragment();
        SpecialMainFragment specialMainFragment = new SpecialMainFragment();

        ArrayList<MainFragment> fragments = new ArrayList<MainFragment>();
        fragments.add(dailyMainFragment);
        fragments.add(themeMainFragment);
        fragments.add(hotMainFragment);
        fragments.add(specialMainFragment);

        myViewPagerAdapter = new MyViewPagerAdapter(getFragmentManager(),fragments);
        viewPager.setAdapter(myViewPagerAdapter);
        currFragment = myViewPagerAdapter.getCurrentFragment();

        //设置监听
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {//wasSelected为真时,表示当前显示与当前点击的是同一个Item
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(currFragment == null){
                    currFragment = myViewPagerAdapter.getCurrentFragment();
                }
                if(wasSelected){
                    currFragment.refresh();
                    return true;
                }

                if (currFragment != null) {
                    currFragment.willBeHidden();
                }

                viewPager.setCurrentItem(position, false);
                currFragment = myViewPagerAdapter.getCurrentFragment();
                currFragment.willBeDisplayed();
                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_location://定位
                startActivity(new Intent(MainActivity.this, MyBaiduMapActivity.class));
                break;
            case R.id.nav_camera://相机
                requestPermission();
                startActivity(new Intent(MainActivity.this, IdCardScanActivity.class));
                break;
            case R.id.nav_gallery://相册
//                startActivity(new Intent(MainActivity.this,GalleryActivity.class));
                startActivity(new Intent(MainActivity.this,IdCardOcrActivity.class));
                break;
            case R.id.nav_slideshow:
                Snackbar.make(drawerLayout,"功能暂未开发!",Snackbar.LENGTH_LONG).show();
                break;
            case R.id.nav_manage:
                Snackbar.make(drawerLayout,"功能暂未开发!",Snackbar.LENGTH_LONG).show();
                break;
            case R.id.nav_share:
                Snackbar.make(drawerLayout,"功能暂未开发!",Snackbar.LENGTH_LONG).show();
                break;
            case R.id.nav_send:
                Snackbar.make(drawerLayout,"功能暂未开发!",Snackbar.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        //头像
        circleImageView = (CircleImageView) navigationView.findViewById(R.id.avater);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private boolean requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)){
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission_group.STORAGE},REQUEST_PERMISSION_CAMERA_CODE);
                return false;
            }else if(!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                Log.d(TAG, "requestPermission: 存储读写权限未获取...");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_STORAGE_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            Log.i(TAG, "onRequestPermissionsResult granted=" + granted);
        }else if(requestCode == REQUEST_PERMISSION_STORAGE_CODE){
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            Log.i(TAG, "onRequestPermissionsResult granted=" + granted);
        }
    }
}

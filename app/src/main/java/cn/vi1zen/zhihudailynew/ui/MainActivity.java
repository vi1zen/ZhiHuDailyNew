package cn.vi1zen.zhihudailynew.ui;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.baidu.mapapi.map.BaiduMap;

import java.util.ArrayList;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.ui.daily.DailyMainFragment;
import cn.vi1zen.zhihudailynew.ui.hot.HotMainFragment;
import cn.vi1zen.zhihudailynew.ui.special.SpecialMainFragment;
import cn.vi1zen.zhihudailynew.ui.theme.ThemeMainFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,DrawerLayout.DrawerListener{

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
            case R.id.nav_location:
                startActivity(new Intent(MainActivity.this, MyBaiduMapActivity.class));
                break;
            case R.id.nav_camera:
                Snackbar.make(drawerLayout,"功能暂未开发!",Snackbar.LENGTH_LONG).show();
                break;
            case R.id.nav_gallery:
                Snackbar.make(drawerLayout,"功能暂未开发!",Snackbar.LENGTH_LONG).show();
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
}

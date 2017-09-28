package com.mrs.materialtablayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    MaterialTabLayout materialTabLayout;
    android.support.design.widget.TabLayout tableLayout;
    ViewPager materialVp;
    ViewPager vp;


    FrameLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialTabLayout = (MaterialTabLayout) findViewById(R.id.materialTab);
        tableLayout = (android.support.design.widget.TabLayout) findViewById(R.id.Tab);
        materialVp = (ViewPager) findViewById(R.id.materialVp);
        vp = (ViewPager) findViewById(R.id.vp);

        content = (FrameLayout) findViewById(R.id.content);
        setUpMaterialTab();

        setTab();
    }

    public void setUpMaterialTab() {

        final ArrayList<View> mViewList = getViewList();
        final ArrayList<String> mTitleList = getTitleList();


        materialVp.setAdapter(new MyPagerAdapter(mViewList, mTitleList));
        materialTabLayout
                .setupWithViewPager(materialVp)
                .setBottomLineColor(Color.RED)
                .setTabMargin(25)
                .setTabsFromPagerAdapter(materialVp.getAdapter())
                .setOnMaterialTabSelectedListener(new MaterialTabLayout.MaterialTabSelectedListener() {
                    @Override
                    public void onTabSelected(MaterialTabLayout.Tab tab, boolean reSelected) {
                        materialVp.setCurrentItem(tab.getPosition(), false);
                        Toast.makeText(MainActivity.this, tab.getPosition() + "--" + reSelected, Toast.LENGTH_SHORT).show();
                    }
                });
        // .setSelectTab(0);

    }

    public void setTab() {

        final ArrayList<View> mViewList = getViewList();
        final ArrayList<String> mTitleList = getTitleList();

        // vp.setAdapter(new MyPagerAdapter(mViewList, mTitleList));
        // tableLayout.setupWithViewPager(vp);
        // tableLayout.setTabsFromPagerAdapter(vp.getAdapter());
        tableLayout.addTab(tableLayout.newTab().setText("tab0"));
        tableLayout.addTab(tableLayout.newTab().setText("tab1"));
        tableLayout.addTab(tableLayout.newTab().setText("tab2"));
        tableLayout.addTab(tableLayout.newTab().setText("tab3"));
        tableLayout.addTab(tableLayout.newTab().setText("tab4"));
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = mViewList.get(tab.getPosition());
                if (view.getParent()!=null){
                    ((ViewGroup)view.getParent()).removeView(view);
                }
                content.addView(view);
                Toast.makeText(MainActivity.this, tab.getPosition() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        try {
            Field mTabStrip = tableLayout.getClass().getDeclaredField("mTabStrip");
            mTabStrip.setAccessible(true);
            LinearLayout ltab = (LinearLayout) mTabStrip.get(tableLayout);
            int childCount = ltab.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ltab.getChildAt(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1);
                params.weight=1;
                params.leftMargin = 20;
                params.rightMargin = 20;
                childAt.setLayoutParams(params);
                childAt.invalidate();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        tableLayout.getTabAt(0).select();
    }

    @NonNull
    private ArrayList<String> getTitleList() {
        //添加页卡标题
        final ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("No:1");
        mTitleList.add("No:2");
        mTitleList.add("No:3");
        mTitleList.add("No:4");
        mTitleList.add("No:5");
        return mTitleList;
    }

    @NonNull
    private ArrayList<View> getViewList() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view1 = mInflater.inflate(R.layout.viewpager_item1, null);
        View view2 = mInflater.inflate(R.layout.viewpager_item2, null);
        View view3 = mInflater.inflate(R.layout.viewpager_item3, null);
        View view4 = mInflater.inflate(R.layout.viewpager_item4, null);
        View view5 = mInflater.inflate(R.layout.viewpager_item5, null);

        //添加页卡视图
        final ArrayList<View> mViewList = new ArrayList<>();
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);
        mViewList.add(view5);
        return mViewList;
    }


    class MyPagerAdapter extends android.support.v4.view.PagerAdapter {
        private ArrayList<View> mViewList;
        private ArrayList<String> mTitleList;

        public MyPagerAdapter(ArrayList<View> mViewList, ArrayList<String> mTitleList) {

            this.mViewList = mViewList;
            this.mTitleList = mTitleList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public int getCount() {
            return mTitleList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }
    }
}

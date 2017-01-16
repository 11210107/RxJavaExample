package com.example.wangzhen.rxjavaexample;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.wangzhen.rxjavaexample.fragment.ElementaryFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.common_toolbar)
    Toolbar   mToolBar;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar(mToolBar);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ElementaryFragment();
                    default:
                        return new ElementaryFragment();
                }
            }

            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "基本";
                    default:
                        return "基本";
                }
            }
        });
        mTabs.setupWithViewPager(mViewPager);
    }

    //初始化ToolBar
    private void initToolBar(Toolbar toolBar) {
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //            toolBar.setNavigationIcon(R.drawable.icon_close);
            TextView title = (TextView) toolBar.findViewById(R.id.common_toolbar_title);
            title.setText("RxJava");
        }

    }
}
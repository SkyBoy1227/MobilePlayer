package com.sky.app.mobileplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.base.BaseFragment;
import com.sky.app.mobileplayer.base.BasePager;
import com.sky.app.mobileplayer.pager.AudioPager;
import com.sky.app.mobileplayer.pager.NetAudioPager;
import com.sky.app.mobileplayer.pager.NetVideoPager;
import com.sky.app.mobileplayer.pager.VideoPager;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * 描述: 主页面
 * Date: 2018/1/19
 * Time: 10:44
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class MainActivity extends AppCompatActivity {
    private FrameLayout fl_main_content;
    private RadioGroup rg_bottom_tag;
    /**
     * 页面集合
     */
    private ArrayList<BasePager> basePagers;
    /**
     * 页面下标
     */
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = findViewById(R.id.fl_main_content);
        rg_bottom_tag = findViewById(R.id.rg_bottom_tag);
        basePagers = new ArrayList<>();
        //添加各个页面
        basePagers.add(new VideoPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new NetAudioPager(this));
        rg_bottom_tag.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_netvideo:
                    position = 2;
                    break;
                case R.id.rb_netaudio:
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }
            setFragment();
        });
        rg_bottom_tag.check(R.id.rb_video);
    }

    private void setFragment() {
        //1.得到FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //3.替换Fragment
        BaseFragment baseFragment = new BaseFragment();
        baseFragment.setBasePager(getBasePager());
        ft.replace(R.id.fl_main_content, baseFragment);
        //4.提交事务
        ft.commit();
    }

    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null) {
            basePager.initData();
        }
        return basePager;
    }
}

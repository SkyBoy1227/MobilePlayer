package com.sky.app.mobileplayer.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    private static final int REQUEST_CODE_ASK_READ_EXTERNAL_STORAGE = 1;
    private RadioGroup rg_bottom_tag;
    /**
     * 页面集合
     */
    private ArrayList<BasePager> basePagers;
    /**
     * 页面下标
     */
    private int position;
    private BasePager basePager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //默认显示主页
        rg_bottom_tag.check(R.id.rb_video);
        //申请动态权限
        requestPermission();
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
        basePager = basePagers.get(position);
        setInitData();
        return basePager;
    }

    /**
     * 设置初始化数据
     */
    private void setInitData() {
        if (basePager != null && !basePager.isInitData) {
            basePager.initData();
            basePager.isInitData = true;
        }
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_READ_EXTERNAL_STORAGE);
            //如果权限未通过验证，则初始化标志要重置为false，即未初始化状态
            if (basePager != null && basePager.isInitData) {
                basePager.isInitData = false;
            }
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setInitData();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "READ_EXTERNAL_STORAGE Denied!\n没有读取SD卡的权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

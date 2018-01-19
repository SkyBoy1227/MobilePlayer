package com.sky.app.mobileplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.sky.app.mobileplayer.R;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = findViewById(R.id.fl_main_content);
        rg_bottom_tag = findViewById(R.id.rg_bottom_tag);
        rg_bottom_tag.check(R.id.rb_video);
    }
}

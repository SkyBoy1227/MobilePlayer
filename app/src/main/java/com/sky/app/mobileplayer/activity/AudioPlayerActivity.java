package com.sky.app.mobileplayer.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.sky.app.mobileplayer.R;

/**
 * Created with Android Studio.
 * 描述: 音乐播放器界面
 * Date: 2018/2/8
 * Time: 16:16
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class AudioPlayerActivity extends AppCompatActivity {
    private ImageView iv_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        iv_icon = findViewById(R.id.iv_icon);
        AnimationDrawable drawable = (AnimationDrawable) iv_icon.getBackground();
        drawable.start();
    }
}

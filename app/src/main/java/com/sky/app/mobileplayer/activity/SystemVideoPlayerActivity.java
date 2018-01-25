package com.sky.app.mobileplayer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.utils.LogUtil;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/1/23
 * Time: 15:28
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class SystemVideoPlayerActivity extends AppCompatActivity {
    private VideoView videoView;
    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate");
        setContentView(R.layout.activity_system_video_player);
        videoView = findViewById(R.id.videoview);
        // 得到播放地址
        uri = getIntent().getData();

        // 监听准备好了
        videoView.setOnPreparedListener(mp -> videoView.start());

        // 监听播放出错
        videoView.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(this, "播放出错了", Toast.LENGTH_SHORT).show();
            return false;
        });

        // 监听播放完成
        videoView.setOnCompletionListener(mp ->
                Toast.makeText(this, "播放完成 = " + uri, Toast.LENGTH_SHORT).show());

        videoView.setVideoURI(uri);
        // 设置控制面板
        videoView.setMediaController(new MediaController(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(this, TestBActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy");
    }
}

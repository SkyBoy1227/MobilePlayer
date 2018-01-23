package com.sky.app.mobileplayer.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.sky.app.mobileplayer.R;

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
        videoView.setMediaController(new MediaController(this));
    }
}

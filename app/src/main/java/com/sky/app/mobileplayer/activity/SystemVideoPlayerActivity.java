package com.sky.app.mobileplayer.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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
public class SystemVideoPlayerActivity extends Activity implements View.OnClickListener {
    private VideoView videoView;
    private Uri uri;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarAudio;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnSwitchScreen;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-26 17:59:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnSwitchPlayer = (Button) findViewById(R.id.btn_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnSwitchScreen = (Button) findViewById(R.id.btn_switch_screen);
        videoView = findViewById(R.id.videoview);

        btnVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnSwitchScreen.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-01-26 17:59:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            // Handle clicks for btnVoice
        } else if (v == btnSwitchPlayer) {
            // Handle clicks for btnSwitchPlayer
        } else if (v == btnExit) {
            // Handle clicks for btnExit
        } else if (v == btnVideoPre) {
            // Handle clicks for btnVideoPre
        } else if (v == btnVideoStartPause) {
            // Handle clicks for btnVideoStartPause
            if (videoView.isPlaying()) {
                // 视频暂停
                videoView.pause();
                // 按钮状态：播放
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
            } else {
                // 视频播放
                videoView.start();
                // 按钮状态：暂停
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            }

        } else if (v == btnVideoNext) {
            // Handle clicks for btnVideoNext
        } else if (v == btnSwitchScreen) {
            // Handle clicks for btnSwitchScreen
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate");
        setContentView(R.layout.activity_system_video_player);
        findViews();
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
//        videoView.setMediaController(new MediaController(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

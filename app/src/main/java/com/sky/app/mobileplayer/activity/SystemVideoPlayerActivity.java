package com.sky.app.mobileplayer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.sky.app.mobileplayer.utils.Utils;

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
    /**
     * 视频进度的更新
     */
    private static final int PROGRESS = 1;
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
    private Utils utils;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    // 1.得到当前进度
                    int currentPosition = videoView.getCurrentPosition();
                    // 2.SeekBar.setProgress(当前进度)
                    seekbarVideo.setProgress(currentPosition);
                    // 设置当前进度文本
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    // 3.每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-26 17:59:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        llTop = findViewById(R.id.ll_top);
        tvName = findViewById(R.id.tv_name);
        ivBattery = findViewById(R.id.iv_battery);
        tvSystemTime = findViewById(R.id.tv_system_time);
        btnVoice = findViewById(R.id.btn_voice);
        seekbarAudio = findViewById(R.id.seekbar_audio);
        btnSwitchPlayer = findViewById(R.id.btn_switch_player);
        llBottom = findViewById(R.id.ll_bottom);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        seekbarVideo = findViewById(R.id.seekbar_video);
        tvDuration = findViewById(R.id.tv_duration);
        btnExit = findViewById(R.id.btn_exit);
        btnVideoPre = findViewById(R.id.btn_video_pre);
        btnVideoStartPause = findViewById(R.id.btn_video_start_pause);
        btnVideoNext = findViewById(R.id.btn_video_next);
        btnSwitchScreen = findViewById(R.id.btn_switch_screen);
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
        utils = new Utils();
        findViews();
        // 得到播放地址
        uri = getIntent().getData();

        // 监听准备好了
        videoView.setOnPreparedListener(mp -> {
            // 得到总进度
            int duration = videoView.getDuration();
            seekbarVideo.setMax(duration);
            // 设置总进度文本
            tvDuration.setText(utils.stringForTime(duration));
            handler.sendEmptyMessage(PROGRESS);
            // 播放视频
            videoView.start();
        });

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
}

package com.sky.app.mobileplayer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.sky.app.mobileplayer.domain.MediaItem;
import com.sky.app.mobileplayer.utils.LogUtil;
import com.sky.app.mobileplayer.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    /**
     * 电量改变的广播接收器
     */
    private MyReceiver receiver;
    /**
     * 传入进来的视频列表
     */
    private ArrayList<MediaItem> mediaItems;
    /**
     * 要播放的列表中的具体位置
     */
    private int position;

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
                    // 设置系统时间
                    tvSystemTime.setText(getSystemTime());
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
     * 得到系统时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

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
            finish();
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
            playNextVideo();
        } else if (v == btnSwitchScreen) {
            // Handle clicks for btnSwitchScreen
        }
    }

    /**
     * 播放下一个视频
     */
    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem item = mediaItems.get(position);
                tvName.setText(item.getName());
                videoView.setVideoPath(item.getData());
                setButtonState();
            }
        } else if (uri != null) {
            setButtonState();
        }
    }

    /**
     * 设置按钮状态
     */
    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == 1) {
                btnVideoPre.setEnabled(false);
                btnVideoPre.setBackgroundResource(R.mipmap.btn_pre_gray);
            } else {
                if (position == 0) {
                    btnVideoPre.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.mipmap.btn_pre_gray);
                } else if (position == mediaItems.size() - 1) {
                    btnVideoNext.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.mipmap.btn_next_gray);
                } else {
                    setButtonEnable(true);
                }
            }
        } else if (uri != null) {
            // 设置两个按钮灰色
            setButtonEnable(false);
        }
    }

    /**
     * 设置按钮是否可用
     *
     * @param isEnable
     */
    private void setButtonEnable(boolean isEnable) {
        if (isEnable) {
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
        } else {
            btnVideoNext.setBackgroundResource(R.mipmap.btn_next_gray);
            btnVideoPre.setBackgroundResource(R.mipmap.btn_pre_gray);
        }
        btnVideoNext.setEnabled(isEnable);
        btnVideoPre.setEnabled(isEnable);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate");
        setContentView(R.layout.activity_system_video_player);
        initData();
        findViews();
        setListener();
        getData();
        setData();
        // 设置控制面板
//        videoView.setMediaController(new MediaController(this));
    }

    private void getData() {
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
        // 得到播放地址
        uri = getIntent().getData();
    }

    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            // 设置视频名称
            tvName.setText(mediaItem.getName());
            videoView.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            // 设置视频名称
            tvName.setText(uri.toString());
            videoView.setVideoURI(uri);
        } else {
            Toast.makeText(this, "对不起，你没有传入视频信息", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
    }

    private void initData() {
        utils = new Utils();
        // 注册电量改变的广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, intentFilter);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            setBattery(level);
        }
    }

    /**
     * 设置电量
     *
     * @param level
     */
    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.mipmap.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.mipmap.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.mipmap.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.mipmap.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.mipmap.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.mipmap.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.mipmap.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.mipmap.ic_battery_100);
        }
    }

    /**
     * 设置监听器
     */
    private void setListener() {
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

        // 设置SeekBar状态变化的监听
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 当手指滑动的时候，会引起SeekBar进度变化，会回调这个方法
             * @param seekBar
             * @param progress
             * @param fromUser 如果是用户引起的，true 否则 false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            /**
             * 当手指触碰的时候回调这个方法
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * 当手指离开的时候回调这个方法
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        // 释放资源的时候，先释放子类，再释放父类
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
        LogUtil.e("onDestroy");
    }
}

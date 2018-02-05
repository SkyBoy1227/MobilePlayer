package com.sky.app.mobileplayer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.domain.MediaItem;
import com.sky.app.mobileplayer.utils.LogUtil;
import com.sky.app.mobileplayer.utils.Utils;
import com.sky.app.mobileplayer.view.VideoView;

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

    /**
     * 隐藏控制面板
     */
    private static final int HIDE_MEDIACONTROLLER = 2;

    /**
     * 显示网速
     */
    private static final int SHOW_SPEED = 3;

    /**
     * 默认播放
     */
    private static final int DEFAULT_SCREEN = 1;

    /**
     * 全屏播放
     */
    private static final int FULL_SCREEN = 2;

    private VideoView videoView;
    private Uri uri;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarAudio;
    private Button btnSwitchPlayer;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnSwitchScreen;
    private RelativeLayout media_controller;
    private LinearLayout llBuffer;
    private TextView tvBufferNetspeed;
    private LinearLayout llLoading;
    private TextView tvLoadingNetspeed;

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

    /**
     * 手势识别器
     */
    private GestureDetector detector;

    /**
     * 是否显示控制面板
     */
    private boolean isShowMediaController = false;

    /**
     * 是否全屏播放
     */
    private boolean isFullScreen;

    /**
     * 屏幕的宽
     */
    private int screenWidth;

    /**
     * 屏幕的高
     */
    private int screenHeight;

    /**
     * 视频真实的宽
     */
    private int videoWidth;

    /**
     * 视频真实的高
     */
    private int videoHeight;

    /**
     * 调节音量
     */
    private AudioManager am;

    /**
     * 当前音量
     */
    private int currentVolume;

    /**
     * 最大音量
     */
    private int maxVolume;

    /**
     * 是否静音
     */
    private boolean isMute = false;

    /**
     * 手指按下时的Y坐标
     */
    private float startY;

    /**
     * 屏幕的高，移动总距离
     */
    private float touchRang;

    /**
     * 刚按下时的音量值
     */
    private int mVol;

    /**
     * 是否是网络资源
     */
    private boolean isNetUri;

    /**
     * 是否调用系统接口
     */
    private boolean isUseSystem;

    /**
     * 上一次播放的进度
     */
    private int prePosition;

    /**
     * 滑动的是否为屏幕左半部
     */
    private boolean isLeft;

    /**
     * 当前屏幕的亮度
     */
    private int screenBrightness;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_SPEED:
                    // 得到当前网速
                    String netSpeed = utils.showNetSpeed(SystemVideoPlayerActivity.this);
                    // 显示网速
                    tvLoadingNetspeed.setText("玩命加载中..." + netSpeed);
                    tvBufferNetspeed.setText("缓冲中..." + netSpeed);
                    // 每两秒更新一次
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED, 2000);
                    break;
                case HIDE_MEDIACONTROLLER:
                    hideMediaController();
                    break;
                case PROGRESS:
                    // 1.得到当前进度
                    int currentPosition = videoView.getCurrentPosition();
                    // 2.SeekBar.setProgress(当前进度)
                    seekbarVideo.setProgress(currentPosition);
                    // 得到缓冲进度
                    if (isNetUri) {
                        // 0~100
                        int buffer = videoView.getBufferPercentage();
                        int max = seekbarVideo.getMax();
                        int secondaryProgress = buffer * max / 100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    } else {
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    // 监听卡顿
                    if (!isUseSystem) {
                        if (videoView.isPlaying()) {
                            int buffer = currentPosition - prePosition;
                            if (buffer < 500) {
                                // 视频卡顿
                                llBuffer.setVisibility(View.VISIBLE);
                            } else {
                                // 视频不卡顿
                                llBuffer.setVisibility(View.GONE);
                            }
                        } else {
                            llBuffer.setVisibility(View.GONE);
                        }
                    }
                    prePosition = currentPosition;
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
        setContentView(R.layout.activity_system_video_player);
        tvName = findViewById(R.id.tv_name);
        ivBattery = findViewById(R.id.iv_battery);
        tvSystemTime = findViewById(R.id.tv_system_time);
        btnVoice = findViewById(R.id.btn_voice);
        seekbarAudio = findViewById(R.id.seekbar_audio);
        btnSwitchPlayer = findViewById(R.id.btn_switch_player);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        seekbarVideo = findViewById(R.id.seekbar_video);
        tvDuration = findViewById(R.id.tv_duration);
        btnExit = findViewById(R.id.btn_exit);
        btnVideoPre = findViewById(R.id.btn_video_pre);
        btnVideoStartPause = findViewById(R.id.btn_video_start_pause);
        btnVideoNext = findViewById(R.id.btn_video_next);
        btnSwitchScreen = findViewById(R.id.btn_switch_screen);
        videoView = findViewById(R.id.videoview);
        media_controller = findViewById(R.id.media_controller);
        llBuffer = findViewById(R.id.ll_buffer);
        tvBufferNetspeed = findViewById(R.id.tv_buffer_netspeed);
        llLoading = findViewById(R.id.ll_loading);
        tvLoadingNetspeed = findViewById(R.id.tv_loading_netspeed);

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
            isMute = !isMute;
            updateVolume(currentVolume);
        } else if (v == btnSwitchPlayer) {
            // Handle clicks for btnSwitchPlayer
            showSwitchPlayerDialog();
        } else if (v == btnExit) {
            // Handle clicks for btnExit
            finish();
        } else if (v == btnVideoPre) {
            // Handle clicks for btnVideoPre
            playPreVideo();
        } else if (v == btnVideoStartPause) {
            // Handle clicks for btnVideoStartPause
            startAndPause();
        } else if (v == btnVideoNext) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if (v == btnSwitchScreen) {
            // Handle clicks for btnSwitchScreen
            setFullScreenAndDefault();
        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
    }

    /**
     * 显示切换播放器的对话框
     */
    private void showSwitchPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统播放器提醒您")
                .setMessage("当您播放视频，有声音没有画面的时候，请切换万能播放器播放")
                .setPositiveButton("确定", (dialog, which) -> startVitamioPlayer())
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 视频播放或暂停
     */
    private void startAndPause() {
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
    }

    /**
     * 播放上一个视频
     */
    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position >= 0) {
                MediaItem item = mediaItems.get(position);
                tvName.setText(item.getName());
                isNetUri = utils.isNetUri(item.getData());
                videoView.setVideoPath(item.getData());
                setButtonState();
                llLoading.setVisibility(View.VISIBLE);
            }
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
                isNetUri = utils.isNetUri(item.getData());
                videoView.setVideoPath(item.getData());
                setButtonState();
                llLoading.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置按钮状态
     */
    private void setButtonState() {
        // 按钮默认为暂停状态
        btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        // 如果接收的是视频列表
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == 1) {
                // 如果只有一个视频，则都按钮都设置为灰色
                setButtonEnable(false);
            } else {
                if (position == 0) {
                    // 如果播放的是第一个视频，则把“上一个”按钮设置为灰色
                    btnVideoPre.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    // 把“下一个"按钮设置可点击
                    btnVideoNext.setEnabled(true);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                } else if (position == mediaItems.size() - 1) {
                    // 如果播放的是最后一个视频，则把“下一个”按钮设置为灰色
                    btnVideoNext.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    // 把“上一个”按钮设置可点击
                    btnVideoPre.setEnabled(true);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                } else {
                    // 播放的视频既不是第一个，也不是最后一个，则将“上一个、下一个”按钮都设置可点击
                    setButtonEnable(true);
                }
            }
        } else if (uri != null) {
            // 如果接收的是单个播放地址
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
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
        }
        btnVideoNext.setEnabled(isEnable);
        btnVideoPre.setEnabled(isEnable);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate");
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
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 得到播放地址
        uri = getIntent().getData();
    }

    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            // 设置视频名称
            tvName.setText(mediaItem.getName());
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            // 设置视频名称
            tvName.setText(uri.toString());
            isNetUri = utils.isNetUri(uri.toString());
            videoView.setVideoURI(uri);
        } else {
            Toast.makeText(this, "对不起，你没有传入视频信息", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
        // 默认隐藏控制面板
        hideMediaController();
        seekbarAudio.setMax(maxVolume);
        seekbarAudio.setProgress(currentVolume);
        handler.sendEmptyMessage(SHOW_SPEED);
    }

    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        media_controller.setVisibility(View.GONE);
        isShowMediaController = false;
        handler.removeMessages(HIDE_MEDIACONTROLLER);
    }

    /**
     * 显示控制面板
     */
    private void showMediaController() {
        media_controller.setVisibility(View.VISIBLE);
        isShowMediaController = true;
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
    }

    private void initData() {
        utils = new Utils();
        // 注册电量改变的广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, intentFilter);
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShowMediaController) {
                    hideMediaController();
                } else {
                    showMediaController();
                }
                return super.onSingleTapConfirmed(e);
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        requestPermission();
    }


    /**
     * 申请动态权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                // 如果没有修改系统的权限则请求修改系统的权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        }
    }

    /**
     * 设置视频全屏播放或者默认播放
     */
    private void setFullScreenAndDefault() {
        if (isFullScreen) {
            // 默认
            setVideoType(DEFAULT_SCREEN);
        } else {
            // 全屏
            setVideoType(FULL_SCREEN);
        }
    }

    private void setVideoType(int type) {
        switch (type) {
            case FULL_SCREEN:
                // 设置视频画面的大小-屏幕有多大视频就多大
                videoView.setVideoSize(screenWidth, screenHeight);
                // 设置按钮的状态--默认
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_default_selector);
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                // 设置视频画面的大小
                // 视频真实的宽、高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                // 屏幕的宽、高
                int width = screenWidth;
                int height = screenHeight;
                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSize(width, height);
                // 设置按钮的状态--全屏
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_full_selector);
                isFullScreen = false;
                break;
            default:
                break;
        }
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
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
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
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();
            // 视频默认大小
            setVideoType(DEFAULT_SCREEN);
            // 隐藏加载页面
            llLoading.setVisibility(View.GONE);
        });

        // 监听播放出错
        videoView.setOnErrorListener((mp, what, extra) -> {
            // 1.播放的视频格式不支持--跳转到万能播放器继续播放
            // 2.播放网络视频的时候，网络中断--1.如果网络确实断了，可以提示用户网络断了；2.网络断断续续的，重新播放
            // 3.播放的时候本地文件中间有空白
            startVitamioPlayer();
            return true;
        });

        // 监听播放完成
        videoView.setOnCompletionListener(mp -> playNextVideo());

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
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            /**
             * 当手指离开的时候回调这个方法
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            }
        });

        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    isMute = progress <= 0;
                    updateVolume(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            }
        });

        if (isUseSystem) {
            // 设置视频卡顿的监听
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener((mp, what, extra) -> {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            // 视频开始卡顿
                            llBuffer.setVisibility(View.VISIBLE);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            // 视频卡顿结束
                            llBuffer.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                    return true;
                });
            }
        }
    }

    /**
     * 调用万能播放器
     */
    private void startVitamioPlayer() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
        Intent intent = new Intent(this, VitamioVideoPlayerActivity.class);
        if (mediaItems != null && mediaItems.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
        } else if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
        // 关闭当前页面
        finish();
    }

    /**
     * 调节音量大小
     *
     * @param progress
     */
    private void updateVolume(int progress) {
        if (isMute) {
            // 静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarAudio.setProgress(0);
        } else {
            // 非静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbarAudio.setProgress(progress);
            currentVolume = progress;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下
                // 按下时记录初始值
                startY = event.getY();
                isLeft = event.getX() <= screenWidth / 2;
                touchRang = Math.min(screenWidth, screenHeight);
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                screenBrightness = getScreenBrightness();
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指滑动
                // 移动时记录相关值
                float endY = event.getY();
                float distance = startY - endY;
                float deltaVoice = distance / touchRang * maxVolume;
                float deltaBrightness = distance / touchRang * 255;
                if (isLeft) {
                    if (deltaBrightness != 0) {
                        int brightness = (int) Math.min(Math.max(screenBrightness + deltaBrightness, 0), 255);
                        setScreenBrightness(brightness);
                    }
                } else {
                    if (deltaVoice != 0) {
                        int voice = (int) Math.min(Math.max(mVol + deltaVoice, 0), maxVolume);
                        isMute = voice == 0;
                        updateVolume(voice);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 手指离开
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 获得当前屏幕亮度值  0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 设置屏幕的亮度
     */
    private void setScreenBrightness(int process) {

        //设置当前窗口的亮度值.这种方法需要权限android.permission.WRITE_EXTERNAL_STORAGE
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        float f = process / 255.0F;
        localLayoutParams.screenBrightness = f;
        getWindow().setAttributes(localLayoutParams);
        //修改系统的亮度值,以至于退出应用程序亮度保持
        saveBrightness(getContentResolver(), process);
    }

    public static void saveBrightness(ContentResolver resolver, int brightness) {
        //改变系统的亮度值
        //这里需要权限android.permission.WRITE_SETTINGS
        //设置为手动调节模式
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //保存到系统中
        Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        resolver.notifyChange(uri, null);
    }

    /**
     * 监听物理键，实现声音的调节
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            currentVolume--;
            if (currentVolume < 0) {
                currentVolume = 0;
            }
            isMute = false;
            updateVolume(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            currentVolume++;
            if (currentVolume > maxVolume) {
                currentVolume = maxVolume;
            }
            isMute = false;
            updateVolume(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

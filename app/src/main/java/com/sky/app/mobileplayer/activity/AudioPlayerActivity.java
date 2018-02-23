package com.sky.app.mobileplayer.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.app.mobileplayer.IMusicPlayerService;
import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.domain.MediaItem;
import com.sky.app.mobileplayer.service.MusicPlayerService;
import com.sky.app.mobileplayer.utils.LyricUtils;
import com.sky.app.mobileplayer.utils.Utils;
import com.sky.app.mobileplayer.view.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created with Android Studio.
 * 描述: 音乐播放器界面
 * Date: 2018/2/8
 * Time: 16:16
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 进度更新
     */
    private static final int PROGRESS = 1;

    /**
     * 显示歌词
     */
    private static final int SHOW_LYRIC = 2;

    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlayMode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnLyrics;
    private ShowLyricView showLyricView;

    /**
     * 音频列表的具体位置
     */
    private int position;

    /**
     * 是否来自状态栏
     * true:状态栏
     * false:音乐列表
     */
    private boolean notification;

    /**
     * 服务的代理类，通过它可以调用服务的方法
     */
    private IMusicPlayerService service;
    //    private MyReceiver receiver;
    private Utils utils;

    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 当连接成功的时候回调这个方法
         * @param name
         * @param binder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = IMusicPlayerService.Stub.asInterface(binder);
            if (service != null) {
                try {
                    // 当服务连接时校验播放模式
                    checkPlayMode();
                    if (!notification) {
                        service.openAudio(position);
                    } else {
                        showViewData();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当断开连接的时候回调这个方法
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (service != null) {
                try {
                    service.stop();
                    service = null;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LYRIC:
                    try {
                        // 1.得到当前的进度
                        int currentPosition = service.getCurrentPosition();
                        // 2.把进度传入ShowLyricView控件，并且计算该高亮哪一句
                        showLyricView.setShowNextLyric(currentPosition);
                        // 3.实时发送消息
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PROGRESS:
                    try {
                        // 得到当前进度
                        int currentPosition = service.getCurrentPosition();
                        // 设置进度
                        seekbarAudio.setProgress(currentPosition);
                        // 设置时间文本
                        tvTime.setText(utils.stringForTime(currentPosition) + "/" + utils.stringForTime(service.getDuration()));
                        // 每秒更新一次
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-02-09 17:57:34 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_audio_player);
        ivIcon = findViewById(R.id.iv_icon);
        AnimationDrawable drawable = (AnimationDrawable) ivIcon.getBackground();
        drawable.start();
        tvArtist = findViewById(R.id.tv_artist);
        tvName = findViewById(R.id.tv_name);
        tvTime = findViewById(R.id.tv_time);
        seekbarAudio = findViewById(R.id.seekbar_audio);
        btnAudioPlayMode = findViewById(R.id.btn_audio_play_mode);
        btnAudioPre = findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = findViewById(R.id.btn_audio_next);
        btnLyrics = findViewById(R.id.btn_lyrics);
        showLyricView = findViewById(R.id.showLyricView);

        btnAudioPlayMode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnLyrics.setOnClickListener(this);

        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    try {
                        if (service != null) {
                            service.seekTo(progress);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-02-09 17:57:34 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnAudioPlayMode) {
            // Handle clicks for btnAudioPlayMode
            setPlayMode();
        } else if (v == btnAudioPre) {
            // Handle clicks for btnAudioPre
            if (service != null) {
                try {
                    service.pre();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnAudioStartPause) {
            // Handle clicks for btnAudioStartPause
            if (service != null) {
                try {
                    if (service.isPlaying()) {
                        // 暂停
                        service.pause();
                        // 按钮状态：播放
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    } else {
                        // 暂停
                        service.start();
                        // 按钮状态：播放
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnAudioNext) {
            // Handle clicks for btnAudioNext
            if (service != null) {
                try {
                    service.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (v == btnLyrics) {
            // Handle clicks for btnLyrics
        }
    }

    /**
     * 设置播放模式
     */
    private void setPlayMode() {
        try {
            int playMode = service.getPlayMode();
            if (playMode == MusicPlayerService.REPEAT_NORMAL) {
                playMode = MusicPlayerService.REPEAT_SINGLE;
                btnAudioPlayMode.setBackgroundResource(R.drawable.btn_audio_play_mode_single_selector);
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                playMode = MusicPlayerService.REPEAT_ALL;
                btnAudioPlayMode.setBackgroundResource(R.drawable.btn_audio_play_mode_all_selector);
                Toast.makeText(this, "全部循环", Toast.LENGTH_SHORT).show();
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                playMode = MusicPlayerService.REPEAT_NORMAL;
                btnAudioPlayMode.setBackgroundResource(R.drawable.btn_audio_play_mode_normal_selector);
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
            }
            service.setPlayMode(playMode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验播放模式，完成图片设置
     */
    private void checkPlayMode() {
        try {
            int playMode = service.getPlayMode();
            if (playMode == MusicPlayerService.REPEAT_NORMAL) {
                btnAudioPlayMode.setBackgroundResource(R.drawable.btn_audio_play_mode_normal_selector);
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                btnAudioPlayMode.setBackgroundResource(R.drawable.btn_audio_play_mode_single_selector);
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                btnAudioPlayMode.setBackgroundResource(R.drawable.btn_audio_play_mode_all_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        bindAndStartService();
    }

    private void initData() {
        utils = new Utils();
//        // 注册广播
//        receiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(MusicPlayerService.OPENAUDIO);
//        registerReceiver(receiver, intentFilter);

        // 注册
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showData(MediaItem mediaItem) {
        showLyric();
        showViewData();
    }

    /**
     * 显示歌词
     */
    private void showLyric() {
        LyricUtils lyricUtils = new LyricUtils();
        try {
            // 得到歌曲播放的绝对路径
            String path = service.getData();
            path = path.substring(0, path.lastIndexOf("."));
            File file = new File(path + ".lrc");
            if (!file.exists()) {
                file = new File(path + ".txt");
            }
            // 传入歌词文件
            lyricUtils.readLyricFile(file);
            showLyricView.setLyrics(lyricUtils.getLyrics());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (lyricUtils.isExistLyric()) {
            // 发消息开始歌词同步
            handler.sendEmptyMessage(SHOW_LYRIC);
        }
    }

//    class MyReceiver extends BroadcastReceiver {
//
//        @Override`
//        public void onReceive(Context context, Intent intent) {
//            showViewData();
//        }
//    }

    private void showViewData() {
        try {
            // 初始化播放按钮的状态
            btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
            tvArtist.setText(service.getArtist());
            tvName.setText(service.getName());
            // 设置进度条的最大值
            seekbarAudio.setMax(service.getDuration());
            // 发送消息
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.sky.app_AUDIOPLAYER");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        // 不至于实例化多个服务
        startService(intent);
    }

    private void getData() {
        notification = getIntent().getBooleanExtra("notification", false);
        if (!notification) {
            position = getIntent().getIntExtra("position", 0);
        }
    }

    @Override
    protected void onDestroy() {
        // 移除消息
        handler.removeCallbacksAndMessages(null);
//        // 取消注册广播
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//            receiver = null;
//        }
        // 解注册
        EventBus.getDefault().unregister(this);
        // 解绑服务
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }
        super.onDestroy();
    }
}

package com.sky.app.mobileplayer.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.sky.app.mobileplayer.IMusicPlayerService;
import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.service.MusicPlayerService;
import com.sky.app.mobileplayer.utils.Utils;

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

    /**
     * 音频列表的具体位置
     */
    private int position;

    /**
     * 服务的代理类，通过它可以调用服务的方法
     */
    private IMusicPlayerService service;
    private MyReceiver receiver;
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
                    service.openAudio(position);
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
        } else if (v == btnAudioPre) {
            // Handle clicks for btnAudioPre
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
        } else if (v == btnLyrics) {
            // Handle clicks for btnLyrics
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
        // 注册广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPENAUDIO);
        registerReceiver(receiver, intentFilter);
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            showViewData();
        }
    }

    private void showViewData() {
        try {
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
        position = getIntent().getIntExtra("position", 0);
    }

    @Override
    protected void onDestroy() {
        // 移除消息
        handler.removeCallbacksAndMessages(null);
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}

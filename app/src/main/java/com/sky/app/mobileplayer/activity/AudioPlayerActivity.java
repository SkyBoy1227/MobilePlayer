package com.sky.app.mobileplayer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.sky.app.mobileplayer.IMusicPlayerService;
import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.service.MusicPlayerService;

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

    /**
     * 音频列表的具体位置
     */
    private int position;

    /**
     * 服务的代理类，通过它可以调用服务的方法
     */
    private IMusicPlayerService service;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        initView();
        getData();
        bindAndStartService();
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

    private void initView() {
        iv_icon = findViewById(R.id.iv_icon);
        AnimationDrawable drawable = (AnimationDrawable) iv_icon.getBackground();
        drawable.start();
    }
}

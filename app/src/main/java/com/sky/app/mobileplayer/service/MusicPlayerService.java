package com.sky.app.mobileplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created with Android Studio.
 * 描述: 音乐播放器服务
 * Date: 2018/2/9
 * Time: 10:56
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class MusicPlayerService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 播放音乐
     */
    private void start() {

    }

    /**
     * 暂停音乐
     */
    private void pause() {

    }

    /**
     * 停止播放
     */
    private void stop() {

    }

    /**
     * 得到当前的播放进度
     *
     * @return
     */
    private int getCurrentPosition() {
        return 0;
    }

    /**
     * 得到当前音频的总时长
     *
     * @return
     */
    private int getDuration() {
        return 0;
    }

    /**
     * 得到艺术家
     *
     * @return
     */
    private String getArtist() {
        return "";
    }

    /**
     * 得到歌曲名称
     *
     * @return
     */
    private String getName() {
        return "";
    }

    /**
     * 得到歌曲播放的路径
     *
     * @return
     */
    private String getData() {
        return "";
    }

    /**
     * 播放下一个
     */
    private void next() {

    }

    /**
     * 播放上一个
     */
    private void pre() {

    }

    /**
     * 设置播放模式
     *
     * @param mode
     */
    private void setPlayMode(int mode) {

    }

    /**
     * 得到播放模式
     *
     * @return
     */
    private int getPlayMode() {
        return 0;
    }
}

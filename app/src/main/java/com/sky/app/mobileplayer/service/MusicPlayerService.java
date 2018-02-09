package com.sky.app.mobileplayer.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.sky.app.mobileplayer.IMusicPlayerService;
import com.sky.app.mobileplayer.domain.MediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private MusicPlayerService service = MusicPlayerService.this;

    private ArrayList<MediaItem> mediaItems;

    /**
     * 音频列表中的具体位置
     */
    private int position;

    /**
     * 线程池
     */
    private ExecutorService threadPool;

    /**
     * 当前播放的音频文件对象
     */
    private MediaItem mediaItem;
    private MediaPlayer mediaPlayer;
    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getData() throws RemoteException {
            return service.getData();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            service.setPlayMode(mode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        threadPool = Executors.newFixedThreadPool(5);
        getDataFromLocal();
    }

    /**
     * 从本地的sdcard得到数据
     */
    private void getDataFromLocal() {
        mediaItems = new ArrayList<>();
        threadPool.submit(() -> {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    // 音频的名称
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    // 音频的时长
                    MediaStore.Audio.Media.DURATION,
                    // 音频的文件大小
                    MediaStore.Audio.Media.SIZE,
                    // 音频的播放地址
                    MediaStore.Audio.Media.DATA,
                    //音频的演唱者
                    MediaStore.Audio.Media.ARTIST,
            };
            Cursor cursor = contentResolver.query(uri, projection, null, null, null);
            if (cursor != null) {
                // 清空集合
                mediaItems.clear();
                while (cursor.moveToNext()) {
                    MediaItem mediaItem = new MediaItem();
                    mediaItems.add(mediaItem);

                    String name = cursor.getString(0);
                    mediaItem.setName(name);

                    long duration = cursor.getLong(1);
                    mediaItem.setDuration(duration);

                    long size = cursor.getLong(2);
                    mediaItem.setSize(size);

                    String data = cursor.getString(3);
                    mediaItem.setData(data);

                    String artist = cursor.getString(4);
                    mediaItem.setArtist(artist);
                }
                cursor.close();
            }
            //发送消息
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    /**
     * 根据位置打开对应的音频文件
     *
     * @param position
     */
    private void openAudio(int position) {
        this.position = position;
        if (mediaItems != null && mediaItems.size() > 0) {
            mediaItem = mediaItems.get(position);
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer.reset();
            }
            try {
                mediaPlayer = new MediaPlayer();
                // 设置监听：播放出错，播放完成，准备好
                mediaPlayer.setOnPreparedListener(mp -> {
                    start();
                });
                mediaPlayer.setOnCompletionListener(mp -> {
                    next();
                });
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    next();
                    return true;
                });
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放音乐
     */
    private void start() {
        mediaPlayer.start();
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        mediaPlayer.pause();
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

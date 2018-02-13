package com.sky.app.mobileplayer.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.sky.app.mobileplayer.IMusicPlayerService;
import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.activity.AudioPlayerActivity;
import com.sky.app.mobileplayer.domain.MediaItem;
import com.sky.app.mobileplayer.utils.CacheUtils;

import org.greenrobot.eventbus.EventBus;

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

    public static final String OPENAUDIO = "com.sky.app.mobileplayer_OPENAUDIO";

    /**
     * 顺序播放
     */
    public static final int REPEAT_NORMAL = 1;

    /**
     * 单曲循环
     */
    public static final int REPEAT_SINGLE = 2;

    /**
     * 全部循环
     */
    public static final int REPEAT_ALL = 3;

    /**
     * 播放模式：默认顺序播放
     */
    private int playMode = REPEAT_NORMAL;

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
    private NotificationManager manager;

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

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mediaPlayer.seekTo(position);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        threadPool = Executors.newFixedThreadPool(5);
        playMode = CacheUtils.getPlayMode(this, "playMode");
        // 加载本地音频数据
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
                mediaPlayer.reset();
            }
            try {
                mediaPlayer = new MediaPlayer();
                // 设置监听：播放出错，播放完成，准备好
                mediaPlayer.setOnPreparedListener(mp -> {
//                    // 通知Activity来获取信息
//                    notifyChange(OPENAUDIO);
                    EventBus.getDefault().post(mediaItem);
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

                if (playMode == REPEAT_SINGLE) {
                    mediaPlayer.setLooping(true);
                } else {
                    mediaPlayer.setLooping(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(service, "对不起，还没有数据", Toast.LENGTH_SHORT).show();
        }
    }

//    /**
//     * 根据动作发广播
//     *
//     * @param action
//     */
//    private void notifyChange(String action) {
//        Intent intent = new Intent(action);
//        sendBroadcast(intent);
//    }

    /**
     * 播放音乐
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void start() {
        mediaPlayer.start();
        // 当播放歌曲的时候，在状态栏显示正在播放，点击的时候，可以进入音乐播放页面
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        // 标识来自状态栏
        intent.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("321音乐")
                .setContentText("正在播放..." + getName())
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1, notification);
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        mediaPlayer.pause();
        // 取消掉通知栏信息
        manager.cancel(1);
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
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 得到当前音频的总时长
     *
     * @return
     */
    private int getDuration() {
        return mediaPlayer.getDuration();
    }

    /**
     * 得到艺术家
     *
     * @return
     */
    private String getArtist() {
        return mediaItem.getArtist();
    }

    /**
     * 得到歌曲名称
     *
     * @return
     */
    private String getName() {
        return mediaItem.getName();
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
     * 播放下一曲
     */
    private void next() {
        // 根据当前的播放模式，设置下一个的位置
        setNextPosition();
        // 根据当前的播放模式和下标去播放音频
        openNextAudio();
    }

    private void openNextAudio() {
        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            if (position >= mediaItems.size()) {
                position = mediaItems.size() - 1;
            } else {
                openAudio(position);
            }
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            openAudio(position);
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            openAudio(position);
        }
    }

    private void setNextPosition() {
        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            position++;
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            position++;
            if (position >= mediaItems.size()) {
                position = 0;
            }
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            position++;
            if (position >= mediaItems.size()) {
                position = 0;
            }
        }
    }

    /**
     * 播放上一曲
     */
    private void pre() {
        // 根据当前的播放模式，设置上一个的位置
        setPrePosition();
        // 根据当前的播放模式和下标去播放音频
        openPreAudio();
    }

    private void openPreAudio() {
        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            if (position < 0) {
                position = 0;
            } else {
                openAudio(position);
            }
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            openAudio(position);
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            openAudio(position);
        }
    }

    private void setPrePosition() {
        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            position--;
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            position--;
            if (position < 0) {
                position = mediaItems.size() - 1;
            }
        }
    }

    /**
     * 设置播放模式
     *
     * @param mode
     */
    private void setPlayMode(int mode) {
        playMode = mode;
        // 如果模式为单曲循环，则不能触发onCompletionListener
        if (playMode == REPEAT_SINGLE) {
            mediaPlayer.setLooping(true);
        } else {
            mediaPlayer.setLooping(false);
        }
        CacheUtils.putPlayMode(this, "playMode", mode);
    }

    /**
     * 得到播放模式
     *
     * @return
     */
    private int getPlayMode() {
        return playMode;
    }

    /**
     * 是否在播放音频
     *
     * @return
     */
    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}

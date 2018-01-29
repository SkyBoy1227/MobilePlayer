package com.sky.app.mobileplayer.pager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.activity.SystemVideoPlayerActivity;
import com.sky.app.mobileplayer.adapter.VideoPagerAdapter;
import com.sky.app.mobileplayer.base.BasePager;
import com.sky.app.mobileplayer.domain.MediaItem;
import com.sky.app.mobileplayer.utils.LogUtil;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with Android Studio.
 * 描述: 本地视频页面
 * Date: 2018/1/19
 * Time: 12:01
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class VideoPager extends BasePager {

    private ListView listView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    /**
     * 线程池
     */
    private ExecutorService threadPool;
    /**
     * 数据集合
     */
    private ArrayList<MediaItem> mediaItems;
    private VideoPagerAdapter adapter;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0) {
                //隐藏文本，显示列表
                adapter = new VideoPagerAdapter(context, mediaItems);
                listView.setAdapter(adapter);
                tv_nomedia.setVisibility(View.GONE);
            } else {
                //显示文本
                tv_nomedia.setVisibility(View.VISIBLE);
            }
            //隐藏ProgressBar
            pb_loading.setVisibility(View.GONE);
        }
    };

    public VideoPager(Context context) {
        super(context);
        threadPool = Executors.newFixedThreadPool(5);
    }

    /**
     * 初始化当前页面的控件，由父类调用
     *
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.video_pager, null);
        listView = view.findViewById(R.id.listView);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            MediaItem mediaItem = mediaItems.get(position);
//            Toast.makeText(context, "mediaItem = " + mediaItem.toString(), Toast.LENGTH_SHORT).show();

            // 1.调起系统所有的播放器--隐式意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//            context.startActivity(intent);

            // 2.调用自己写的播放器--显示意图
//            Intent intent = new Intent(context, SystemVideoPlayerActivity.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//            context.startActivity(intent);

            // 3.传递列表数据-对象-序列化
            Intent intent = new Intent(context, SystemVideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地视频页面的数据被初始化了。。。");
        //加载本地视频数据
        getDataFromLocal();
    }

    /**
     * 从本地的sdcard得到数据
     */
    private void getDataFromLocal() {
        mediaItems = new ArrayList<>();
        threadPool.submit(() -> {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    //视频的名称
                    MediaStore.Video.Media.DISPLAY_NAME,
                    //视频的时长
                    MediaStore.Video.Media.DURATION,
                    //视频的文件大小
                    MediaStore.Video.Media.SIZE,
                    //视频的播放地址
                    MediaStore.Video.Media.DATA,
                    //音频的演唱者
                    MediaStore.Video.Media.ARTIST,
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
            handler.sendEmptyMessage(1);
        });
    }
}

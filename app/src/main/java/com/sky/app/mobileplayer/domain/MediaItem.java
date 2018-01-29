package com.sky.app.mobileplayer.domain;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * 描述: 视频/音频文件
 * Date: 2018/1/22
 * Time: 15:31
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class MediaItem implements Serializable {
    private static final long serialVersionUID = -3385260687855510506L;
    /**
     * 名称
     */
    private String name;

    /**
     * 时长
     */
    private long duration;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 播放地址
     */
    private String data;

    /**
     * 音频的演唱者
     */
    private String artist;

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}

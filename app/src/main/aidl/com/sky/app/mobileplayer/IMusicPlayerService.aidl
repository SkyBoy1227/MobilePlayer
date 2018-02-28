// IMusicPlayerService.aidl
package com.sky.app.mobileplayer;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
    /**
     * 根据位置打开对应的音频文件
     *
     * @param position
     */
    void openAudio(int position);

    /**
     * 播放音乐
     */
    void start();

    /**
     * 暂停音乐
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 得到当前的播放进度
     *
     * @return
     */
    int getCurrentPosition();

    /**
     * 得到当前音频的总时长
     *
     * @return
     */
    int getDuration();

    /**
     * 得到艺术家
     *
     * @return
     */
    String getArtist();

    /**
     * 得到歌曲名称
     *
     * @return
     */
    String getName();

    /**
     * 得到歌曲播放的路径
     *
     * @return
     */
    String getData();

    /**
     * 播放下一曲
     */
    void next();

    /**
     * 播放上一曲
     */
    void pre();

    /**
     * 设置播放模式
     *
     * @param mode
     */
    void setPlayMode(int mode);

    /**
     * 得到播放模式
     *
     * @return
     */
    int getPlayMode();

    /**
     * 是否在播放音频
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 拖动音频
     *
     * @param position
     */
    void seekTo(int position);

    /**
     * 获取音频SessionID
     */
    int getAudioSessionId();

    /**
     * 得到当前播放的音乐在列表中的位置
     *
     * @return
     */
    int getPosition();
}

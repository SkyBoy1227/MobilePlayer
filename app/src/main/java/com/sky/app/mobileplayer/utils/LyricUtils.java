package com.sky.app.mobileplayer.utils;

import com.sky.app.mobileplayer.domain.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with Android Studio.
 * 描述: 解析歌词工具类
 * Date: 2018/2/22
 * Time: 18:15
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class LyricUtils {

    private List<Lyric> lyrics;

    /**
     * 得到解析好的歌词列表
     *
     * @return
     */
    public List<Lyric> getLyrics() {
        return lyrics;
    }

    /**
     * 读取解析歌词文件
     *
     * @param file
     */
    public void readLyricFile(File file) {
        if (file == null || !file.exists()) {
            // 歌词文件不存在
            lyrics = null;
        } else {
            // 解析歌词
            lyrics = new ArrayList<>();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
                String line;
                while ((line = reader.readLine()) != null) {
                    parseLyric(line);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 排序
            Collections.sort(lyrics, (o1, o2) -> {
                if (o1.getTimePoint() == o2.getTimePoint()) {
                    return 0;
                } else if (o1.getTimePoint() > o2.getTimePoint()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            // 计算每句歌词的高亮显示时间
            for (int i = 0; i < lyrics.size(); i++) {
                Lyric oneLyric = lyrics.get(i);
                if (i + 1 < lyrics.size()) {
                    Lyric twoLyric = lyrics.get(i + 1);
                    oneLyric.setSleepTime(twoLyric.getTimePoint() - oneLyric.getTimePoint());
                }
            }
        }
    }

    /**
     * 解析一句歌词
     *
     * @param line [02:04.12][03:37.32][00:59.73]我在这里欢笑
     */
    private void parseLyric(String line) {
        int pos1 = line.indexOf("[");
        int pos2 = line.indexOf("]");
        if (pos1 == 0 && pos2 != -1) {
            // 肯定有一句歌词
            // 装时间
            long[] times = new long[getCountTag(line)];
            // 02:04.12
            String strTime = line.substring(pos1 + 1, pos2);
            times[0] = strTime2LongTime(strTime);

            String content = line;
            int i = 1;
            while (pos1 == 0 && pos2 != -1) {
                // [03:37.32][00:59.73]我在这里欢笑--->[00:59.73]我在这里欢笑-->我在这里欢笑
                content = content.substring(pos2 + 1);
                pos1 = line.indexOf("[");
                pos2 = line.indexOf("]");
                if (pos2 != -1) {
                    // 03:37.32-->00:59.73
                    strTime = content.substring(pos1 + 1, pos2);
                    times[i] = strTime2LongTime(strTime);
                    if (times[i] == -1) {
                        return;
                    }
                    i++;
                }
            }
            // 把时间数组和文本联系起来，并且加入到集合中
            for (int j = 0; j < times.length; j++) {
                // 有时间戳
                if (times[j] != 0) {
                    Lyric lyric = new Lyric();
                    lyric.setTimePoint(times[j]);
                    lyric.setContent(content);
                    // 添加到集合中
                    lyrics.add(lyric);
                }
            }
        }
    }

    /**
     * 把String类型的时间转换为long类型
     *
     * @param strTime 02:04.12
     * @return
     */
    private long strTime2LongTime(String strTime) {
        long result;
        try {
            // 把02:04.12按照:切割成02和04.12
            String[] str1 = strTime.split(":");
            // 把04.12按照.切割成04和12
            String[] str2 = str1[1].split("\\.");
            // 分
            long min = Long.parseLong(str1[0]);
            // 秒
            long second = Long.parseLong(str2[0]);
            // 毫秒
            long mil = Long.parseLong(str2[1]);
            result = min * 60 * 1000 + second * 1000 + mil * 10;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 判断有多少句歌词
     *
     * @param line [02:04.12][03:37.32][00:59.73]我在这里欢笑
     * @return
     */
    private int getCountTag(String line) {
        int result;
        String[] left = line.split("\\[");
        String[] right = line.split("\\]");
        if (left.length == 0 && right.length == 0) {
            result = 1;
        } else if (left.length > right.length) {
            result = left.length;
        } else {
            result = right.length;
        }
        return result;
    }
}

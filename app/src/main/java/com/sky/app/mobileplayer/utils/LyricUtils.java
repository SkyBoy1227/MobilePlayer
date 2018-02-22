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
     * 解析歌词文件
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
                String line = "";
                while ((line = reader.readLine()) != null) {
                    line = parseLyric(line);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 排序

            // 计算每句歌词的高亮显示时间
        }
    }

    /**
     * 解析一句歌词
     *
     * @param line
     * @return
     */
    private String parseLyric(String line) {
        return null;
    }
}

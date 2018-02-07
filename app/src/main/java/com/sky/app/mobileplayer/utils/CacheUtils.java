package com.sky.app.mobileplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created with Android Studio.
 * 描述: 缓存工具类
 * Date: 2018/2/7
 * Time: 10:50
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class CacheUtils {
    /**
     * 保存数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
        preferences.edit()
                .putString(key, value)
                .apply();
    }

    /**
     * 得到缓存的数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}

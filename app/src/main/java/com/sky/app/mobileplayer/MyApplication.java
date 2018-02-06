package com.sky.app.mobileplayer;

import android.app.Application;

import org.xutils.x;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/2/6
 * Time: 14:20
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        // 是否输出debug日志, 开启debug会影响性能.
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}

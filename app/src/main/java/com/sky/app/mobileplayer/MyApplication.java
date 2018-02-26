package com.sky.app.mobileplayer;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

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
        // 初始化
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5a924f4b");
    }
}

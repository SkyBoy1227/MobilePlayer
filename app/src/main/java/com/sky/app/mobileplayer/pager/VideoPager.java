package com.sky.app.mobileplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sky.app.mobileplayer.base.BasePager;
import com.sky.app.mobileplayer.utils.LogUtil;

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
    private TextView textView;

    public VideoPager(Context context) {
        super(context);
    }

    /**
     * 初始化当前页面的控件，由父类调用
     *
     * @return
     */
    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地视频页面的数据被初始化了。。。");
        //联网
        //音频内容
        textView.setText("本地视频的内容");
    }
}

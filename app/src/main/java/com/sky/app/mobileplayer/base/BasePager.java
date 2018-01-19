package com.sky.app.mobileplayer.base;

import android.content.Context;
import android.view.View;

/**
 * Created with Android Studio.
 * 描述: 基类、公共类
 * Date: 2018/1/19
 * Time: 11:55
 * VideoPager
 * <p>
 * AudioPager
 * <p>
 * NetVideoPager
 * <p>
 * NetAudioPager
 * 都继承该类
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public abstract class BasePager {
    /**
     * 上下文
     */
    public Context context;
    /**
     * 接收各个页面的实例
     */
    public View rootView;

    public BasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    /**
     * 强制子页面实现该方法，实现想要的特定的效果
     *
     * @return
     */
    public abstract View initView();

    /**
     * 当子页面，需要绑定数据，或者联网请求数据并且绑定的时候，重写该方法
     */
    public void initData() {

    }
}

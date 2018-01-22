package com.sky.app.mobileplayer.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.app.mobileplayer.R;
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
    private ListView listView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;

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
        View view = View.inflate(context, R.layout.video_pager, null);
        listView = view.findViewById(R.id.listView);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地视频页面的数据被初始化了。。。");
        //联网
        //音频内容
    }
}

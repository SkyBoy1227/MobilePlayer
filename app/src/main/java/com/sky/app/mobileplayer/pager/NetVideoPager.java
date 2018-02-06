package com.sky.app.mobileplayer.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.base.BasePager;
import com.sky.app.mobileplayer.utils.Constants;
import com.sky.app.mobileplayer.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created with Android Studio.
 * 描述: 网络视频页面
 * Date: 2018/1/19
 * Time: 12:01
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NetVideoPager extends BasePager {
    @ViewInject(R.id.listView)
    private ListView listView;
    @ViewInject(R.id.tv_nonet)
    private TextView tvNonet;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;

    public NetVideoPager(Context context) {
        super(context);
    }

    /**
     * 初始化当前页面的控件，由父类调用
     *
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.netvideo_pager, null);
        x.view().inject(NetVideoPager.this, view);
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        LogUtil.e("网络视频页面的数据被初始化了。。。");
        //联网
        //音频内容
        RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result = " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError..." + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled..." + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished...");
            }
        });
    }
}

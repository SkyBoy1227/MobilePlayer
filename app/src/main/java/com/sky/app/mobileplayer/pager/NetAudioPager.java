package com.sky.app.mobileplayer.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.adapter.NetAudioPagerAdapter;
import com.sky.app.mobileplayer.base.BasePager;
import com.sky.app.mobileplayer.domain.NetAudioPagerData;
import com.sky.app.mobileplayer.utils.CacheUtils;
import com.sky.app.mobileplayer.utils.Constants;
import com.sky.app.mobileplayer.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: 网络音乐页面
 * Date: 2018/1/19
 * Time: 12:01
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NetAudioPager extends BasePager {

    @ViewInject(R.id.listView)
    private ListView listView;

    @ViewInject(R.id.tv_nonet)
    private TextView tvNonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;

    /**
     * 数据集合
     */
    private List<NetAudioPagerData.ListBean> datas;

    /**
     * NetAudioPager适配器
     */
    private NetAudioPagerAdapter adapter;

    public NetAudioPager(Context context) {
        super(context);
    }

    /**
     * 初始化当前页面的控件，由父类调用
     *
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.netaudio_pager, null);
        x.view().inject(this, view);
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        LogUtil.e("网络音乐页面的数据被初始化了。。。");
        String savaJson = CacheUtils.getString(context, Constants.ALL_RES_URL);
        if (!TextUtils.isEmpty(savaJson)) {
            // 解析Json
            processData(savaJson);
        }
        //联网
        //音频内容
        getDataFromNet();
    }

    /**
     * 解析Json并显示数据
     *
     * @param json
     */
    private void processData(String json) {
        NetAudioPagerData data = parseJson(json);
        datas = data.getList();
        showViewData();
    }

    private void showViewData() {
        if (datas != null && datas.size() > 0) {
            // 设置适配器
            adapter = new NetAudioPagerAdapter(context, datas);
            listView.setAdapter(adapter);
            tvNonet.setVisibility(View.GONE);
        } else {
            tvNonet.setVisibility(View.VISIBLE);
        }
        pbLoading.setVisibility(View.GONE);
    }

    private NetAudioPagerData parseJson(String json) {
        return new Gson().fromJson(json, NetAudioPagerData.class);
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.ALL_RES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("数据获取成功=====" + result);
                // 保存数据
                CacheUtils.putString(context, Constants.ALL_RES_URL, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("数据获取失败=====" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled=====" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished=====");
            }
        });
    }
}

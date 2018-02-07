package com.sky.app.mobileplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.activity.SystemVideoPlayerActivity;
import com.sky.app.mobileplayer.adapter.NetVideoPagerAdapter;
import com.sky.app.mobileplayer.base.BasePager;
import com.sky.app.mobileplayer.domain.MediaItem;
import com.sky.app.mobileplayer.utils.CacheUtils;
import com.sky.app.mobileplayer.utils.Constants;
import com.sky.app.mobileplayer.utils.LogUtil;
import com.sky.app.mobileplayer.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private XListView listView;
    @ViewInject(R.id.tv_nonet)
    private TextView tvNonet;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;

    private ArrayList<MediaItem> mediaItems;
    private NetVideoPagerAdapter adapter;

    /**
     * 是否已经加载更多数据
     */
    private boolean isLoadMore;

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
        listView.setPullLoadEnable(true);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(context, SystemVideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position - 1);
            context.startActivity(intent);
        });
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getDataFromNet();
            }

            @Override
            public void onLoadMore() {
                getMoreDataFromNet();
            }
        });
        return view;
    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime("更新时间:" + getSystemTime());
    }

    /**
     * 加载更多数据
     */
    private void getMoreDataFromNet() {
        //联网
        //音频内容
        RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result = " + result);
                isLoadMore = true;
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError..." + ex.getMessage());
                isLoadMore = false;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled..." + cex.getMessage());
                isLoadMore = false;
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished...");
                isLoadMore = false;
            }
        });
    }

    /**
     * 得到系统时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("网络视频页面的数据被初始化了。。。");
        String data = CacheUtils.getString(context, Constants.NET_URL);
        if (!TextUtils.isEmpty(data)) {
            processData(data);
        }
        getDataFromNet();
    }

    /**
     * 联网请求数据
     */
    private void getDataFromNet() {
        //联网
        //音频内容
        RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result = " + result);
                // 缓存数据
                CacheUtils.putString(context, Constants.NET_URL, result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError..." + ex.getMessage());
                showData();
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

    /**
     * 处理json数据，并显示列表
     *
     * @param json
     */
    private void processData(String json) {
        if (!isLoadMore) {
            mediaItems = parseJson(json);
            showData();
        } else {
            mediaItems.addAll(parseJson(json));
            // 刷新
            adapter.notifyDataSetChanged();
            onLoad();
            isLoadMore = false;
        }
    }

    private void showData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //隐藏文本，显示列表
            adapter = new NetVideoPagerAdapter(context, mediaItems);
            listView.setAdapter(adapter);
            onLoad();
            tvNonet.setVisibility(View.GONE);
        } else {
            //显示文本
            tvNonet.setVisibility(View.VISIBLE);
        }
        //隐藏ProgressBar
        pbLoading.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     *
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parseJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectItem = jsonArray.optJSONObject(i);
                    if (jsonObjectItem != null) {
                        MediaItem mediaItem = new MediaItem();
                        String movieName = jsonObjectItem.optString("movieName");
                        mediaItem.setName(movieName);

                        String videoTitle = jsonObjectItem.optString("videoTitle");
                        mediaItem.setDesc(videoTitle);

                        String hightUrl = jsonObjectItem.optString("hightUrl");
                        mediaItem.setData(hightUrl);

                        String coverImg = jsonObjectItem.optString("coverImg");
                        mediaItem.setImageUrl(coverImg);

                        mediaItems.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }
}

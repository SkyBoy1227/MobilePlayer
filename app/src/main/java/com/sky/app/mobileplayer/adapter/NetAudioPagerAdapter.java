package com.sky.app.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sky.app.mobileplayer.domain.NetAudioPagerData;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: NetAudioPager的适配器
 * Date: 2018/2/27
 * Time: 11:32
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NetAudioPagerAdapter extends BaseAdapter {
    private Context context;
    private List<NetAudioPagerData.ListBean> datas;

    public NetAudioPagerAdapter(Context context, List<NetAudioPagerData.ListBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}

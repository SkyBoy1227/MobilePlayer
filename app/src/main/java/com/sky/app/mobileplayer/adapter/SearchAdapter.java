package com.sky.app.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.domain.SearchBean;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/2/26
 * Time: 15:52
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<SearchBean.ItemsBean> items;

    public SearchAdapter(Context context, List<SearchBean.ItemsBean> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_netvideo_pager, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 根据position得到列表中对应位置的数据
        SearchBean.ItemsBean item = items.get(position);
        holder.tv_name.setText(item.getItemTitle());
        holder.tv_desc.setText(item.getKeywords());
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(item.getItemImage().getImgUrl1())
                .apply(options)
                .into(holder.iv_icon);
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;

        public ViewHolder(View view) {
            iv_icon = view.findViewById(R.id.iv_icon);
            tv_name = view.findViewById(R.id.tv_name);
            tv_desc = view.findViewById(R.id.tv_desc);
        }
    }
}

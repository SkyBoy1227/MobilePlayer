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
import com.sky.app.mobileplayer.domain.MediaItem;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * 描述: NetVideoPager的适配器
 * Date: 2018/2/6
 * Time: 15:38
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NetVideoPagerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MediaItem> mediaItems;

    public NetVideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaItems.get(position);
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
        MediaItem mediaItem = mediaItems.get(position);
        holder.tv_name.setText(mediaItem.getName());
        holder.tv_desc.setText(mediaItem.getDesc());
        // 1.使用xUtils3加载网络图片
//        x.image().bind(holder.iv_icon, mediaItem.getImageUrl());
        // 2.使用Glide加载网络图片
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(mediaItem.getImageUrl())
                .apply(options)
                .into(holder.iv_icon);
        // 3.使用Picasso加载网络图片
//        Picasso.with(context)
//                .load(mediaItem.getImageUrl())
//                .placeholder(R.drawable.video_default)
//                .error(R.drawable.video_default)
//                .into(holder.iv_icon);
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

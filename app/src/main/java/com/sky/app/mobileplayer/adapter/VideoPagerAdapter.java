package com.sky.app.mobileplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.domain.MediaItem;
import com.sky.app.mobileplayer.utils.Utils;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: VideoPager的适配器
 * Date: 2018/1/23
 * Time: 11:28
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class VideoPagerAdapter extends BaseAdapter {
    private Context context;
    private List<MediaItem> mediaItems;
    private Utils utils;

    public VideoPagerAdapter(Context context, List<MediaItem> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
        utils = new Utils();
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
            convertView = View.inflate(context, R.layout.item_video_pager, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 根据position得到数据
        MediaItem mediaItem = mediaItems.get(position);
        holder.tv_name.setText(mediaItem.getName());
        holder.tv_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
        holder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
        return convertView;
    }


    static class ViewHolder {
        TextView tv_name;
        TextView tv_size;
        TextView tv_time;

        public ViewHolder(View view) {
            tv_name = view.findViewById(R.id.tv_name);
            tv_size = view.findViewById(R.id.tv_size);
            tv_time = view.findViewById(R.id.tv_time);
        }
    }
}

package com.sky.app.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.domain.NetAudioPagerData;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pl.droidsonroids.gif.GifImageView;

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
    /**
     * 视频
     */
    private static final int TYPE_VIDEO = 0;

    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;

    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;

    /**
     * GIF图片
     */
    private static final int TYPE_GIF = 3;

    /**
     * 软件推广
     */
    private static final int TYPE_AD = 4;

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
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        NetAudioPagerData.ListBean listBean = datas.get(position);
        String type = listBean.getType();
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else if ("ad".equals(type)) {
            itemViewType = TYPE_AD;
        }
        return itemViewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 初始化
            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case TYPE_VIDEO:
                    // 视频
                    convertView = View.inflate(context, R.layout.all_video_item, null);
                    //在这里实例化特有的
                    viewHolder.tv_play_nums = convertView.findViewById(R.id.tv_play_nums);
                    viewHolder.tv_video_duration = convertView.findViewById(R.id.tv_video_duration);
                    viewHolder.iv_commant = convertView.findViewById(R.id.iv_commant);
                    viewHolder.tv_commant_context = convertView.findViewById(R.id.tv_commant_context);
                    viewHolder.jcv_videoplayer = convertView.findViewById(R.id.jcv_videoplayer);
                    break;
                case TYPE_IMAGE:
                    // 图片
                    convertView = View.inflate(context, R.layout.all_image_item, null);
                    viewHolder.iv_image_icon = convertView.findViewById(R.id.iv_image_icon);
                    break;
                case TYPE_TEXT:
                    // 文字
                    convertView = View.inflate(context, R.layout.all_text_item, null);
                    break;
                case TYPE_GIF:
                    // gif
                    convertView = View.inflate(context, R.layout.all_gif_item, null);
                    viewHolder.iv_image_gif = convertView.findViewById(R.id.iv_image_gif);
                    break;
                case TYPE_AD:
                    // 软件广告
                    convertView = View.inflate(context, R.layout.all_ad_item, null);
                    viewHolder.btn_install = convertView.findViewById(R.id.btn_install);
                    viewHolder.iv_image_icon = convertView.findViewById(R.id.iv_image_icon);
                    break;
                default:
                    break;
            }
            // 设置tag
            convertView.setTag(viewHolder);
        } else {
            // 得到tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        // user_info
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;
        // bottom
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_number;
        TextView tv_posts_number;
        LinearLayout ll_download;

        // 中间公共部分 -所有的都有
        TextView tv_context;

        // Video
//        TextView tv_context;
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayer jcv_videoplayer;

        // Image
        ImageView iv_image_icon;
//        TextView tv_context;

        // Text
//        TextView tv_context;

        // Gif
        GifImageView iv_image_gif;
//        TextView tv_context;

        // 软件推广
        Button btn_install;
//        TextView iv_image_icon;
        //TextView tv_context;
    }
}

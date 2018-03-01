package com.sky.app.mobileplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sky.app.mobileplayer.R;

/**
 * Created with Android Studio.
 * 描述: 图片详情界面
 * Date: 2018/3/1
 * Time: 14:21
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class ImageDetailActivity extends AppCompatActivity {
    private PhotoView pvImage;

    /**
     * 图片URL地址
     */
    private String mImgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        pvImage = findViewById(R.id.pv_image);
        initData();
    }

    private void initData() {
        mImgUrl = getIntent().getStringExtra("img_url");
        // 使用Glide加载网络图片
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .load(mImgUrl)
                .apply(options)
                .into(pvImage);
        pvImage.enable();
        pvImage.setOnClickListener(v -> finish());
    }
}

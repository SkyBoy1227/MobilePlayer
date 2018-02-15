package com.sky.app.mobileplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sky.app.mobileplayer.domain.Lyric;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/2/15
 * Time: 23:32
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class ShowLyricView extends android.support.v7.widget.AppCompatTextView {

    /**
     * 歌词列表
     */
    private ArrayList<Lyric> lyrics;

    /**
     * 控件的宽
     */
    private int width;

    /**
     * 控件的高
     */
    private int height;

    /**
     * 画笔
     */
    private Paint paint;

    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }

    public ShowLyricView(Context context) {
        this(context, null);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lyrics != null && lyrics.size() > 0) {

        } else {
            canvas.drawText("没有歌词", width / 2, height / 2, paint);
        }
    }

    private void initView() {
        lyrics = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Lyric lyric = new Lyric();
            lyric.setContent(i + "cccccccccccccc" + i);
            lyric.setTimePoint(1000 * i);
            lyric.setSleepTime(1500 + i);
            lyrics.add(lyric);
        }

        // 创建画笔
        paint = new Paint();
        paint.setTextSize(20);
        paint.setColor(Color.GREEN);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 设置居中对齐
        paint.setTextAlign(Paint.Align.CENTER);
    }
}

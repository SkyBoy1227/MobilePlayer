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
    private Paint whitePaint;
    /**
     * 歌词列表中的索引，是第几句歌词
     */
    private int index;

    /**
     * 每行歌词的高
     */
    private float textHeight;

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
            // 绘制歌词：绘制当前句
            String currentText = lyrics.get(index).getContent();
            canvas.drawText(currentText, width / 2, height / 2, paint);
            // 绘制前面部分
            float tempY = height / 2;
            for (int i = index - 1; i >= 0; i--) {
                tempY -= textHeight;
                if (tempY < 0) {
                    break;
                }
                String preText = lyrics.get(i).getContent();
                canvas.drawText(preText, width / 2, tempY, whitePaint);
            }
            // 绘制后面部分
            tempY = height / 2;
            for (int i = index + 1; i < lyrics.size(); i++) {
                tempY += textHeight;
                if (tempY > height) {
                    break;
                }
                String nextText = lyrics.get(i).getContent();
                canvas.drawText(nextText, width / 2, tempY, whitePaint);
            }
        } else {
            canvas.drawText("没有歌词", width / 2, height / 2, paint);
        }
    }

    private void initView() {
        textHeight = dip2px(20);
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
        paint.setTextSize(dip2px(20));
        paint.setColor(Color.GREEN);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 设置居中对齐
        paint.setTextAlign(Paint.Align.CENTER);

        whitePaint = new Paint();
        whitePaint.setTextSize(dip2px(20));
        whitePaint.setColor(Color.WHITE);
        // 抗锯齿
        whitePaint.setAntiAlias(true);
        // 设置居中对齐
        whitePaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * dip转换为px
     *
     * @param dpValue
     * @return
     */
    public int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (scale * dpValue + 0.5f);
    }
}

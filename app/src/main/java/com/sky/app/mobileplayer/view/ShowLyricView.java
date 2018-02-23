package com.sky.app.mobileplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sky.app.mobileplayer.domain.Lyric;
import com.sky.app.mobileplayer.utils.DensityUtil;

import java.util.List;

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
    private List<Lyric> lyrics;

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

    /**
     * 当前播放进度
     */
    private int currentPosition;

    /**
     * 时间戳，什么时刻高亮哪句歌词
     */
    private long timePoint;

    /**
     * 高亮显示的时间或者休眠时间
     */
    private long sleepTime;

    public void setLyrics(List<Lyric> lyrics) {
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
        initView(context);
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
        if (lyrics != null && lyrics.size() > 0 && index < lyrics.size()) {
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

    private void initView(Context context) {
        textHeight = DensityUtil.dip2px(context, 18);
//        lyrics = new ArrayList<>();
//        for (int i = 0; i < 1000; i++) {
//            Lyric lyric = new Lyric();
//            lyric.setContent(i + "cccccccccccccc" + i);
//            lyric.setTimePoint(1000 * i);
//            lyric.setSleepTime(1500 + i);
//            lyrics.add(lyric);
//        }

        // 创建画笔
        paint = new Paint();
        paint.setTextSize(DensityUtil.dip2px(context, 16));
        paint.setColor(Color.GREEN);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 设置居中对齐
        paint.setTextAlign(Paint.Align.CENTER);

        whitePaint = new Paint();
        whitePaint.setTextSize(DensityUtil.dip2px(context, 16));
        whitePaint.setColor(Color.WHITE);
        // 抗锯齿
        whitePaint.setAntiAlias(true);
        // 设置居中对齐
        whitePaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 根据当前播放的位置，找出该高亮显示哪句歌词
     *
     * @param currentPosition
     */
    public void setShowNextLyric(int currentPosition) {
        this.currentPosition = currentPosition;
        if (lyrics != null && lyrics.size() > 0) {
            for (int i = 1; i < lyrics.size(); i++) {
                if (currentPosition < lyrics.get(i).getTimePoint()) {
                    int tempIndex = i - 1;
                    if (currentPosition >= lyrics.get(tempIndex).getTimePoint()) {
                        // 当前正在播放的那句歌词
                        index = tempIndex;
                        timePoint = lyrics.get(tempIndex).getTimePoint();
                        sleepTime = lyrics.get(tempIndex).getSleepTime();
                        break;
                    }
                }
                // 最后一句歌词
                if (i == lyrics.size() - 1) {
                    index = i;
                    timePoint = lyrics.get(i).getTimePoint();
                    sleepTime = lyrics.get(i).getSleepTime();
                }
            }
            // 重新绘制
            invalidate();
        }
    }
}

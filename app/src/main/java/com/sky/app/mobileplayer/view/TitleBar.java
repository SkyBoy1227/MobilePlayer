package com.sky.app.mobileplayer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sky.app.mobileplayer.R;

/**
 * Created with Android Studio.
 * 描述: 标题栏
 * Date: 2018/1/20
 * Time: 13:43
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {
    /**
     * 上下文对象
     */
    private Context context;

    private View tv_search;
    private View rl_game;
    private View iv_record;

    /**
     * 在代码中实例化时使用该方法
     *
     * @param context
     */
    public TitleBar(Context context) {
        this(context, null);
    }

    /**
     * 在布局中使用该类，会调用该方法
     *
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 设置样式时会调用该方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 当布局加载完成的时候回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //实例化子View
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        iv_record = getChildAt(3);

        //设置监听事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(context, "播放历史", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

package com.sky.app.mobileplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.sky.app.mobileplayer.R;

/**
 * Created with Android Studio.
 * 描述: 欢迎页面
 * Date: 2018/1/19
 * Time: 10:38
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    /**
     * 启动主页面的标志
     * true ： 执行过startMainActivity()方法
     * false ：未执行过startMainActivity()方法
     */
    private boolean isStartFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //延迟两秒进入程序主页面
        handler.postDelayed(this::startMainActivity, 2000);
    }

    /**
     * 进入主页面
     */
    private void startMainActivity() {
        if (!isStartFlag) {
            isStartFlag = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //结束当前页面
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

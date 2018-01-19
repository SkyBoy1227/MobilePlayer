package com.sky.app.mobileplayer.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/1/19
 * Time: 10:44
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("我是主页面");
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        setContentView(textView);
    }
}

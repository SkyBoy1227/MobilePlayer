package com.sky.app.startallplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPlayer(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("http://192.168.56.1:8080/Video.mp4"), "video/*");
        intent.setDataAndType(Uri.parse("http://192.168.56.1:8080/atguigu.wmv"), "video/*");
//        intent.setDataAndType(Uri.parse("http://vf2.mtime.cn/Video/2016/07/19/mp4/160719095812990469.mp4"), "video/*");
        startActivity(intent);
    }
}

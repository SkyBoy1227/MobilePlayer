package com.sky.app.mobileplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.app.mobileplayer.R;

/**
 * Created with Android Studio.
 * 描述: 语音搜索界面
 * Date: 2018/2/25
 * Time: 12:59
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etInput;
    private ImageView ivVoice;
    private TextView tvSearch;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView tvNodata;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-02-25 13:12:57 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        etInput = findViewById(R.id.et_input);
        ivVoice = findViewById(R.id.iv_voice);
        tvSearch = findViewById(R.id.tv_search);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        tvNodata = findViewById(R.id.tv_nodata);

        // 设置点击事件
        ivVoice.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_voice:
                Toast.makeText(this, "语音输入", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_search:
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

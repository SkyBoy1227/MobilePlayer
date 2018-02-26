package com.sky.app.mobileplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.sky.app.mobileplayer.R;
import com.sky.app.mobileplayer.adapter.SearchAdapter;
import com.sky.app.mobileplayer.domain.SearchBean;
import com.sky.app.mobileplayer.utils.Constants;
import com.sky.app.mobileplayer.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
     * 用HashMap存储听写结果
     */
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    /**
     * 联网请求地址
     */
    private String url;

    /**
     * 数据集合
     */
    private List<SearchBean.ItemsBean> items;

    /**
     * 适配器
     */
    private SearchAdapter adapter;

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
                // 语音输入
                showDialog();
                break;
            case R.id.tv_search:
                // 搜索
                searchText();
                break;
            default:
                break;
        }
    }

    private void searchText() {
        String text = etInput.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            if (items != null && items.size() > 0) {
                items.clear();
            }
            try {
                text = URLEncoder.encode(text, "UTF-8");
                url = Constants.SEARCH_URL + text;
                getDataFromNet();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 联网请求搜索数据
     */
    private void getDataFromNet() {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String result) {
        SearchBean searchBean = parseJson(result);
        items = searchBean.getItems();
        showData();
    }

    private void showData() {
        if (items != null && items.size() > 0) {
            adapter = new SearchAdapter(this, items);
            listView.setAdapter(adapter);
            tvNodata.setVisibility(View.GONE);
        } else {
            tvNodata.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     *
     * @param result
     * @return
     */
    private SearchBean parseJson(String result) {
        return new Gson().fromJson(result, SearchBean.class);
    }

    private void showDialog() {
        //1.创建 RecognizerDialog 对象
        RecognizerDialog mDialog = new RecognizerDialog(this, i -> {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        });
        //2.设置accent、 language等参数
        // 中文
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 普通话
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将 RecognizerDialog 用于语义理解，必须添加以下参数设置，设置之后 onResult 回调返回将是语义理解的结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "3.0");

        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String result = recognizerResult.getResultString();
                Log.e("MainActivity", "result = " + result);
                // 解析好的文本
                String text = JsonParser.parseIatResult(result);

                String sn = null;
                // 读取json结果中的sn字段
                try {
                    JSONObject resultJson = new JSONObject(result);
                    sn = resultJson.optString("sn");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mIatResults.put(sn, text);

                StringBuffer resultBuffer = new StringBuffer();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults.get(key));
                }

                etInput.setText(resultBuffer.toString());
                etInput.setSelection(etInput.length());
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.e("MainActivity", "onError = " + speechError.getMessage());
            }
        });

        //4.显示 dialog，接收语音输入
        mDialog.show();
    }
}

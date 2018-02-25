package com.sky.app.speechdemo2;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_input;
    private Button btn_start;
    private Button btn_speech_text;

    /**
     * 用HashMap存储听写结果
     */
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5a924f4b");
        setContentView(R.layout.activity_main);
        et_input = findViewById(R.id.et_input);
        btn_speech_text = findViewById(R.id.btn_speech_text);
        btn_start = findViewById(R.id.btn_start);
        btn_speech_text.setOnClickListener(this);
        btn_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                // 语音输入
                showDialog();
                break;
            case R.id.btn_speech_text:
                // 语音合成
                speechText();
                break;
            default:
                break;
        }
    }

    private void speechText() {
        // 1.创建 SpeechSynthesizer 对象, 第二个参数： 本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        // 2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
        // 设置云端
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        // 设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        // 设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        // 设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
        // 3.开始合成
        mTts.startSpeaking(et_input.getText().toString(), mSynListener);
    }

    private SynthesizerListener mSynListener = new SynthesizerListener() {
        /**
         * 开始播放
         */
        @Override
        public void onSpeakBegin() {

        }

        /**
         * 缓冲进度回调
         * @param percent 缓冲进度0~100
         * @param beginPos 缓冲音频在文本中开始位置
         * @param endPos 表示缓冲音频在文本中结束位置
         * @param info 附加信息
         */
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {

        }

        /**
         * 暂停播放
         */
        @Override
        public void onSpeakPaused() {

        }

        /**
         * 恢复播放回调接口
         */
        @Override
        public void onSpeakResumed() {

        }

        /**
         * 播放进度回调
         * @param percent 播放进度0~100
         * @param beginPos 播放音频在文本中开始位置
         * @param endPos 播放音频在文本中结束位置
         */
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        /**
         * 会话结束回调接口，没有错误时， error为null
         * @param speechError
         */
        @Override
        public void onCompleted(SpeechError speechError) {

        }

        /**
         * 会话事件回调接口
         * @param i
         * @param i1
         * @param i2
         * @param bundle
         */
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private void showDialog() {
        //1.创建 RecognizerDialog 对象
        RecognizerDialog mDialog = new RecognizerDialog(this, i -> {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        });

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

                et_input.setText(resultBuffer.toString());
                et_input.setSelection(et_input.length());
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

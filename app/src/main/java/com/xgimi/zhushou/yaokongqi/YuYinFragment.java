package com.xgimi.zhushou.yaokongqi;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.voicerecognition.android.Candidate;
import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.baidu.voicerecognition.android.VoiceRecognitionClient.VoiceClientStatusChangeListener;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.fragment.BaseFragment;

import java.util.List;

import rx.Observable;

public class YuYinFragment extends BaseFragment {

    private Context context;
    private VoiceRecognitionClient mRecognitionClient; // 百度语音引擎
    private VoiceRecognitionConfig config; // 百度语音配置项

    private ImageView voice;
    private boolean isRecognition = false;
    private static final int POWER_UPDATE_INTERVAL = 100;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (mView == null) {
            mView = View.inflate(getActivity(), R.layout.yuyin, null);
            initView(mView);
        }
        return mView;
    }

    private void initView(View view) {
        tv_speak = (TextView) view.findViewById(R.id.speak);
        wave = (WaveformView) view.findViewById(R.id.wave);
        yuyin = (ImageView) view.findViewById(R.id.yuyin);


        mRecognitionClient = App.getContext().getBaiduVoiceClient();
        config = new VoiceRecognitionConfig();

        // 设置识别开始提示音
        config.enableBeginSoundEffect(R.raw.bdspeech_recognition_start);
        // 设置说话结束提示音
        config.enableEndSoundEffect(R.raw.bdspeech_speech_end);
        voice = (ImageView) view.findViewById(R.id.iv_yuyin);
        voice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isRecognition) {
                    wave.setVisibility(View.GONE);
                    tv_speak.setVisibility(View.VISIBLE);
                    mRecognitionClient.speakFinish();
                }
            }
        });
        voice.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                mRecognitionClient.startVoiceRecognition(
                        new MyVoiceRecogListener(), config);
                wave.setVisibility(View.VISIBLE);
                tv_speak.setVisibility(View.GONE);
                return false;
            }
        });

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        }
    };

    /**
     * 音量更新任务
     */
    private Runnable mUpdateVolume = new Runnable() {

        public void run() {

            if (isRecognition) {

                long vol = mRecognitionClient.getCurrentDBLevelMeter();
                // SetPower(vol);
                update(vol);
                handler.removeCallbacks(mUpdateVolume);
                handler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);

            }
        }
    };
    private ImageView yuyin;
    private WaveformView wave;
    private TextView tv_speak;

    /**
     * 自定义的语音监听类
     *
     * @author liuyang
     */
    private void update(final float volume) {
        wave.post(new Runnable() {
            @Override
            public void run() {
//                Log.e("info", volume + "---");
                wave.updateAmplitude(volume / 500);
            }
        });
    }

    public void SetPower(long lev) {
        int index = (int) (lev / 10);
        switch (index) {
            case 0:
                yuyin.setImageResource(R.drawable.yuyinone);
                break;
            case 1:
                yuyin.setImageResource(R.drawable.yuyintwo);
                break;
            case 2:
                yuyin.setImageResource(R.drawable.yuyinthre);
                break;
            case 3:
                yuyin.setImageResource(R.drawable.yuyinfour);
                break;
            case 4:
                yuyin.setImageResource(R.drawable.yuyinfive);
                break;
            case 5:
                yuyin.setImageResource(R.drawable.yuyinsix);
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                yuyin.setImageResource(R.drawable.yuyinsix);
                break;
        }

    }

    class MyVoiceRecogListener implements VoiceClientStatusChangeListener {

        @Override
        public void onClientStatusChange(int arg0, Object arg1) {

            switch (arg0) {
                case VoiceRecognitionClient.CLIENT_STATUS_START_RECORDING:
                    isRecognition = true;
                    handler.removeCallbacks(mUpdateVolume);
                    handler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
                    break;

                case VoiceRecognitionClient.CLIENT_STATUS_USER_CANCELED:
                    isRecognition = false;
                    break;

                case VoiceRecognitionClient.CLIENT_STATUS_FINISH:
                    wave.setVisibility(View.GONE);
                    tv_speak.setVisibility(View.VISIBLE);
                    isRecognition = false;
                    updateRecognitionResult(arg1);
                    break;
            }
        }

        @Override
        public void onError(int arg0, int arg1) {
        }

        @Override
        public void onNetworkStatusChange(int arg0, Object arg1) {
            // 这里不做任何操作不影响简单识别
        }

    }

    /**
     * 将识别结果更新到UI上，搜索模式结果类型为List<String>,输入模式结果类型为List<List<Candidate>>
     *
     * @param result
     */
    private void updateRecognitionResult(Object result) {

        if (result != null && result instanceof List) {

            List results = (List) result;

            if (results.size() > 0) {
                if (results.get(0) instanceof List) {

                    List<List<Candidate>> sentences = (List<List<Candidate>>) result;

                    StringBuffer sb = new StringBuffer();

                    for (List<Candidate> candidates : sentences) {

                        if (candidates != null && candidates.size() > 0) {
                            sb.append(candidates.get(0).getWord());
                        }
                    }
                    Log.e("voice1", sb.toString());
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                            "2", new VcontrolCmd.ControlCmd(5, sb.toString()), 20000)));
//					GMDeviceController.getInstance().SendJC(
//							"VOICE:" + sb.toString());
                    // SendJCommand("VOICE:" + sb.toString());
                } else {
                    Log.e("voice", results.get(0).toString());
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                            "2", new VcontrolCmd.ControlCmd(5, results.get(0).toString()), 20000)));
//					GMDeviceController.getInstance().SendJC(
//							"VOICE:" + results.get(0).toString());
                    // SendJCommand("VOICE:" + results.get(0).toString());

                }
            }
        }
    }
}

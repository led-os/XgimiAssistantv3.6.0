package com.xgimi.zhushou.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.List;

import confignetwork.ChTask;
import confignetwork.IChListener;
import confignetwork.IChResult;
import confignetwork.IChTask;
import confignetwork.WifiAdminSimple;
import confignetwork.task.__IChTask;

public class ConfigActivity extends AppCompatActivity {
    private static final String TAG = "ConfigActivity";

    private EditText mEdtApSsid;
    private EditText mEdtApPassword;
    private CheckBox mCbHidden;
    private Button mBtnConfig;

    private WifiAdminSimple mWifiAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mWifiAdmin = new WifiAdminSimple(this);

        mEdtApSsid = (EditText) findViewById(R.id.et_ssid);
        mEdtApPassword = (EditText) findViewById(R.id.et_password);
        mCbHidden = (CheckBox) findViewById(R.id.cb_hidden);

        ImageView ivNavBack = (ImageView) findViewById(R.id.nav_back);
        ivNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnConfig = (Button) findViewById(R.id.btn_config);
        mBtnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apSsid = mEdtApSsid.getText().toString();
                String apPassword = mEdtApPassword.getText().toString();
                String apBssid = mWifiAdmin.getWifiConnectedBssid();
                String isSsidHiddenStr = mCbHidden.isChecked() ? "YES" : "NO";

                int[] tastResultCount = {0, 1, 2, 3, 4, 5};
                String taskResultCountStr = Integer.toString(tastResultCount[1]); //设置任务的数量为1个

                if (__IChTask.DEBUG) {
                }
                new AsyncTask3().execute(apSsid, apBssid, apPassword, isSsidHiddenStr, taskResultCountStr);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        // display the connected ap's ssid
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            mEdtApSsid.setText(apSsid);
        } else {
            mEdtApSsid.setText("");
        }
        // check whether the wifi is connected
        boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
        mBtnConfig.setEnabled(!isApSsidEmpty);
    }


    private class AsyncTask2 extends AsyncTask<String, Void, IChResult> {

        private ProgressDialog mProgressDialog;

        private IChTask mChTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(ConfigActivity.this);
            mProgressDialog.setMessage("正在配置，请稍侯...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IChTask.DEBUG) {
                        }
                        if (mChTask != null) {
                            mChTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            mProgressDialog.show();
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        }

        @Override
        protected IChResult doInBackground(String... params) {
            synchronized (mLock) {
                String apSsid = params[0];
                String apBssid = params[1];
                String apPassword = params[2];
                String isSsidHiddenStr = params[3];
                boolean isSsidHidden = false;
                if (isSsidHiddenStr.equals("YES")) {
                    isSsidHidden = true;
                }
                XGIMILOG.D("apSsid = " + apBssid + ", apSsid== null? : " + (apSsid == null));
                mChTask = new ChTask(apSsid, apBssid, apPassword, isSsidHidden, ConfigActivity.this);
            }
            IChResult result = mChTask.executeForResult();
            return result;
        }

        @Override
        protected void onPostExecute(IChResult result) {
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确认");
            // it is unnecessary at the moment, add here just to show how to use isCancelled()
            if (!result.isCancelled()) {
                if (result.isSuc()) {
                    mProgressDialog.setMessage("配置成功" + "\n" + "BSSID = " + result.getBssid() + "\n" + "InetAddress = " + result.getInetAddress().getHostAddress());

                } else {
                    mProgressDialog.setMessage("配置失败");
                }
            }
        }
    }

    private void onChResultAddedPerform(final IChResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " 已连接到wifi";
                Toast.makeText(ConfigActivity.this, text, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private IChListener myListener = new IChListener() {

        @Override
        public void onChResultAdded(final IChResult result) {
            onChResultAddedPerform(result);
        }
    };

    private class AsyncTask3 extends AsyncTask<String, Void, List<IChResult>> {

        private ProgressDialog mProgressDialog;

        private IChTask mChTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(ConfigActivity.this);
            mProgressDialog.setMessage("正在配置, 请稍候...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IChTask.DEBUG) {
                        }
                        if (mChTask != null) {
                            mChTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            mProgressDialog.show();
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        }

        @Override
        protected List<IChResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                String apSsid = params[0];
                String apBssid = params[1];
                String apPassword = params[2];
                String isSsidHiddenStr = params[3];
                String taskResultCountStr = params[4];
                boolean isSsidHidden = false;
                if (isSsidHiddenStr.equals("YES")) {
                    isSsidHidden = true;
                }
                taskResultCount = Integer.parseInt(taskResultCountStr);
//                if (apSsid != null) {
//                    mChTask = new ChTask(apSsid, apBssid, apPassword, isSsidHidden, ConfigActivity.this);
//                    mChTask.setChListener(myListener);
//                }
                XGIMILOG.D("apSsid = " + apBssid + ", apSsid== null? : " + (apSsid == null));
                mChTask = new ChTask(apSsid, apBssid, apPassword, isSsidHidden, ConfigActivity.this);
                mChTask.setChListener(myListener);
            }
            List<IChResult> resultList = mChTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IChResult> result) {
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确认");
            IChResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IChResult resultInList : result) {
                        sb.append("配置成功" + "\n" + "BSSID = " + resultInList.getBssid() + "\n" + "InetAddress = " + resultInList.getInetAddress().getHostAddress());

                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\n有 " + (result.size() - count) + " 个结果未显示\n");
                    }
                    mProgressDialog.setMessage(sb.toString());
                } else {
                    mProgressDialog.setMessage("配置失败");
                }
            }
        }

    }
}

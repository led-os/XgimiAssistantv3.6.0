package com.xgimi.zhushou.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.IntelligenceActivity;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import confignetwork.ChTask;
import confignetwork.IChListener;
import confignetwork.IChResult;
import confignetwork.IChTask;
import confignetwork.WifiAdminSimple;
import confignetwork.task.__IChTask;

/**
 * Created by 霍长江 on 2016/10/12.
 */

/**
 * 智能幕布对话框
 */
public class IntelligenceDilog {
    public Dialog mDialog;
    private Activity mconte;
    private WifiAdminSimple mWifiAdmin;
    private final TextView tv_ssid;
    private Context mContext;
    private final EditText mPassword;

    public IntelligenceActivity.ConfigureCallBack mLisener;
    private final View view;

    public void setmLisener(IntelligenceActivity.ConfigureCallBack lisener) {
        this.mLisener = lisener;
    }

    public IntelligenceDilog(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mconte = context;
        view = inflater.inflate(R.layout.intelligence_layout, null);
        mPassword = (EditText) view.findViewById(R.id.search);
        Button mConnect = (Button) view.findViewById(R.id.connect);
        tv_ssid = (TextView) view.findViewById(R.id.tv_ssid);
        mPassword.requestFocus();
        mWifiAdmin = new WifiAdminSimple(context);

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mPassword, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            tv_ssid.setText(apSsid);
        } else {
            tv_ssid.setText("");
        }
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            view.getApplicationWindowToken(), 0);
                }
                String apSsid = tv_ssid.getText().toString().trim();
                String apPassword = mPassword.getText().toString();
                String apBssid = mWifiAdmin.getWifiConnectedBssid();

                int[] tastResultCount = {0, 1, 2, 3, 4, 5};
                String taskResultCountStr = Integer.toString(tastResultCount[1]); //设置任务的数量为1个

                if (__IChTask.DEBUG) {
                }
                XGIMILOG.D("apBssid = " + apBssid);
                if (apSsid != null && !"".equals(apSsid)) {
                    new AsyncTask3().execute(apSsid, apBssid, apPassword, "NO", taskResultCountStr);
                } else {
                    Toast.makeText(mContext, "请先连接WiFi", Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                }
            }
        });
    }

    public void show() {
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        WindowManager windowManager = mconte.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        mDialog.getWindow().setAttributes(lp);
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (mDialog.isShowing()) {
            return true;
        }
        return false;
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
            mProgressDialog = new ProgressDialog(mContext);
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
//            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
            mProgressDialog.show();
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
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
                if (apSsid != null) {
                    mChTask = new ChTask(apSsid, apBssid, apPassword, isSsidHidden, mContext);
                }
            }
            IChResult result = mChTask.executeForResult();
            return result;
        }

        @Override
        protected void onPostExecute(IChResult result) {
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确认");
            // it is unnecessary at the moment, add here just to show how to use isCancelled()
            if (!result.isCancelled()) {
                if (result.isSuc()) {
                    if (mLisener != null)
                        mLisener.getConfigIp(result.getInetAddress().getHostAddress());
                    mProgressDialog.setMessage("配置成功" + "\n" + "BSSID = " + result.getBssid() + "\n" + "InetAddress = " + result.getInetAddress().getHostAddress());
                    mProgressDialog.dismiss();
                    mProgressDialog.cancel();
                    mDialog.dismiss();
                } else {
                    mProgressDialog.setMessage("配置失败");
                }
            }
        }
    }

    private void onChResultAddedPerform(final IChResult result) {
        String text = result.getBssid() + " 已连接到wifi";
        Log.e("info", text);

//       runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                String text = result.getBssid() + " 已连接到wifi";
//                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
//            }
//
//        });
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
            mProgressDialog = new ProgressDialog(mContext);
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
//            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
            mProgressDialog.show();
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
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
                if (apSsid != null && !"".equals(apBssid)) {
                    XGIMILOG.D("");
                    mChTask = new ChTask(apSsid, apBssid, apPassword, isSsidHidden, mContext);
                } else {
                    XGIMILOG.D("");
                    return new ArrayList<>();
                }
                mChTask.setChListener(myListener);
            }
            List<IChResult> resultList = mChTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IChResult> result) {
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确认");
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
                        if (mLisener != null)
                            mLisener.getConfigIp(resultInList.getInetAddress().getHostAddress());
                        mProgressDialog.dismiss();
                        mDialog.dismiss();
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
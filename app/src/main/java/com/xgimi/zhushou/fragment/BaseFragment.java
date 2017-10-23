package com.xgimi.zhushou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.MVTypes;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.widget.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 霍长江 on 2016/8/8.
 */
public class BaseFragment extends Fragment {
    public Subscription subscription;
    public int window_height;
    public int width;
    private WindowManager wm;
    public App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        wm = getActivity().getWindowManager();
        window_height = wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        app = (App) getActivity().getApplicationContext();
    }

    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private Subscription subscription1;
    private List<MVTypes.data> mvdique = new ArrayList<>();

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

    }

    public BaseFragment() {
        initList();
    }

    //获取MV地区类型信息
    private void initList() {
        subscription1 = Api.getMangoApi().getMVTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    Observer<MVTypes> observer1 = new Observer<MVTypes>() {
        @Override
        public void onCompleted() {
            unRegist(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(MVTypes mvTypes) {
            if (mvTypes != null && mvTypes.data != null) {
                for (int i = 0; i < mvTypes.data.size(); i++) {
                    mvdique.addAll(mvTypes.data);
                }
                SaveData.getInstance().mTypes = mvTypes;
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }

    public void showFragmentWithoutBackStackAndAnim(Fragment home, Fragment hideFragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        if (hideFragment != null) {
            fragmentTransaction.hide(hideFragment);
        }
        if (!home.isAdded()) {
            fragmentTransaction.add(R.id.layout_music, home).
                    commitAllowingStateLoss();
        } else {
            fragmentTransaction.show(home).commitAllowingStateLoss();
        }
    }

    private MyProgressDialog dilog;

    public void showDilog(String s) {
        if (dilog != null && dilog.isShowing()) {
            dilog.dismiss();
        }
        dilog = new MyProgressDialog(getActivity(), s);
        dilog.show();
    }

    public void MissDilog() {
        if (dilog != null) {
            dilog.dismiss();
            dilog = null;
        }
    }
}

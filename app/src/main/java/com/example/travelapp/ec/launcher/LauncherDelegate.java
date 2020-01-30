package com.example.travelapp.ec.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.AccountManger;
import com.example.travelapp.core.app.IUserChecker;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.launcher.ILauncherListener;
import com.example.travelapp.core.ui.launcher.OnLauncherFinishTag;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.core.utils.timer.BaseTimeTask;
import com.example.travelapp.core.utils.timer.ITimerListener;

import java.text.MessageFormat;
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.OnClick;

public class LauncherDelegate extends TravelDelegate implements ITimerListener {

    @BindView(R2.id.tv_launcher_timer)
    AppCompatTextView mTvTimer = null;

    @OnClick(R2.id.tv_launcher_timer)
    void onClickTimerView() {
        if (mTvTimer != null) {
            mTimer.cancel();
            mTimer = null;
            checkIsShowScroll();
        }
    }

    private int mCount = 5;
    private Timer mTimer = null;
    private ILauncherListener mLauncherListener;

    public void initTimer() {
        mTimer = new Timer();
        final BaseTimeTask task = new BaseTimeTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_launcher;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        initTimer();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ILauncherListener){
            mLauncherListener = (ILauncherListener) activity;
        }
    }

    //判断是否显示滑动启动页
    private void checkIsShowScroll(){
        if(!TravelPreference.getAppFlag(ScrollLaucherTag.HAS_FIRST_LAUNCHER_APP.name())){
            start(new LauncherScrollDelegate(),SINGLETASK);
        }else {
            //检查用户是否已经登录
            AccountManger.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if(mLauncherListener != null){
                        mLauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }

                @Override
                public void onNotSignIn() {
                    if(mLauncherListener != null){
                        mLauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }

    @Override
    public void onTimer() {
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTvTimer != null) {
                    mTvTimer.setText(MessageFormat.format("跳过\n{0}s", mCount));
                    mCount--;
                    if (mCount < 1) {
                        if (mTvTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            checkIsShowScroll();
                        }
                    }
                }
            }
        });

    }
}

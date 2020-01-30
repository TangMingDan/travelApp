package com.example.travelapp.core.utils.timer;

import java.util.TimerTask;

public class BaseTimeTask extends TimerTask {

    private ITimerListener mITimerListener = null;

    public BaseTimeTask(ITimerListener mITimerListener) {
        this.mITimerListener = mITimerListener;
    }

    @Override
    public void run() {
        if(mITimerListener != null){
            mITimerListener.onTimer();
        }
    }
}

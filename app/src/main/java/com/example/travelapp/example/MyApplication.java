package com.example.travelapp.example;

import android.app.Application;

import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.ec.icon.FontEcModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import androidx.annotation.Nullable;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Travel.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withApiHost("http://49.232.144.185:8080/travelWeb_ssm/")
                .configure();
        //开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        CallbackManager.getInstance()
                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if(JPushInterface.isPushStopped(Travel.getApplicationContext())){
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(Travel.getApplicationContext());
                        }
                    }
                })
                .addCallback(CallbackType.TAG_STOP_PYSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if(!JPushInterface.isPushStopped(Travel.getApplicationContext())){
                            JPushInterface.stopPush(Travel.getApplicationContext());
                        }
                    }
                });

    }
}

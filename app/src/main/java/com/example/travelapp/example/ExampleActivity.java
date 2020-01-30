package com.example.travelapp.example;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;
import qiu.niorgai.StatusBarCompat;

import android.os.Bundle;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.core.activities.ProxyActivity;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.launcher.ILauncherListener;
import com.example.travelapp.core.ui.launcher.OnLauncherFinishTag;
import com.example.travelapp.ec.launcher.LauncherDelegate;
import com.example.travelapp.ec.launcher.LauncherScrollDelegate;
import com.example.travelapp.ec.main.EcBottomDelegate;
import com.example.travelapp.ec.sign.ISignListener;
import com.example.travelapp.ec.sign.SignInDelegate;

public class ExampleActivity extends ProxyActivity implements ISignListener, ILauncherListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Travel.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this,true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public TravelDelegate setRootDelegate() {
        return new LauncherDelegate();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show();
        startWithPop(new EcBottomDelegate());
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
        startWithPop(new EcBottomDelegate());
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag){
            case SIGNED:
                Toast.makeText(this,"启动结束，用户登陆了",Toast.LENGTH_SHORT).show();
                startWithPop(new EcBottomDelegate());
                break;
            case NOT_SIGNED:
                Toast.makeText(this,"启动结束，用户没有登陆了",Toast.LENGTH_SHORT).show();
                startWithPop(new SignInDelegate());
                break;
            default:
                break;
        }
    }
}

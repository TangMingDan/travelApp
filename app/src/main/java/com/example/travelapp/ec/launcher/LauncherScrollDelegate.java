package com.example.travelapp.ec.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.AccountManger;
import com.example.travelapp.core.app.IUserChecker;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.launcher.ILauncherListener;
import com.example.travelapp.core.ui.launcher.OnLauncherFinishTag;
import com.example.travelapp.core.utils.storage.TravelPreference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class LauncherScrollDelegate extends TravelDelegate implements OnItemClickListener {

    @BindView(R2.id.banner_launcher_scroll)
    ConvenientBanner mConvenientBanner = null;
    private ILauncherListener mLauncherListener = null;

    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();

    private void initBanner(){
        INTEGERS.add(R.mipmap.launchaer_01);
        INTEGERS.add(R.mipmap.launchaer_02);
        INTEGERS.add(R.mipmap.launchaer_03);
        INTEGERS.add(R.mipmap.launchaer_04);
        INTEGERS.add(R.mipmap.launchaer_05);
        mConvenientBanner
                .setPages(new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {

                        return new LauncherHolder(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.delegate_launcher_img;
                    }
                }, INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this)
                .setCanLoop(false);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ILauncherListener){
            mLauncherListener = (ILauncherListener) activity;
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_scroll_launcher;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        initBanner();
    }

    @Override
    public void onItemClick(int position) {
        //如果点击的是最后一个
        if(position == INTEGERS.size() - 1){
            TravelPreference.setAppFlag(ScrollLaucherTag.HAS_FIRST_LAUNCHER_APP.name(),true);
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
}

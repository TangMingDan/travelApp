package com.example.travelapp.ec.main.personal.profile;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.main.index.IndexDelegate;
import com.example.travelapp.ec.main.personal.PersonalDelegate;
import com.example.travelapp.ec.main.personal.list.ListAdapter;
import com.example.travelapp.ec.main.personal.list.ListBean;
import com.example.travelapp.ec.main.personal.list.ListItemType;
import com.example.travelapp.ec.main.personal.list.ListTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class UserProfileDelegate extends TravelDelegate {

    @BindView(R2.id.rv_user_profile)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.btn_change_user_profile)
    Button btnChangeUserProfile = null;
    @BindView(R2.id.btn_submit_change_user_profile)
    Button btnSubmitChange = null;
    @BindView(R2.id.btn_cancel_change_user_profile)
    Button btnCancelChange = null;
    WeakHashMap<String,Object> userProfileMap = new WeakHashMap<>();
    @BindView(R2.id.ll_user_info_not_change)
    LinearLayout llUserInfoNotChange = null;

    List<ListBean> data = new ArrayList<>();
    ListAdapter adapter = null;

    @OnClick(R2.id.btn_change_user_profile)
    void changeUserProfile(){
        llUserInfoNotChange.setVisibility(View.GONE);
        changeBeginAnimation();
    }

    @OnClick(R2.id.btn_cancel_change_user_profile)
    void cancelChange(){
        changeEndAnimation();
        calcelUpdateUserProfile();
        llUserInfoNotChange.setVisibility(View.VISIBLE);
    }
    @OnClick(R2.id.btn_submit_change_user_profile)
    void submitChange(){
        updateUserProfile();
        changeEndAnimation();
        llUserInfoNotChange.setVisibility(View.VISIBLE);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_profile;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getContext(), R.anim.fragment_userparofile_right_in);
        }
        return AnimationUtils.loadAnimation(getContext(), R.anim.fragment_userparofile_left_out);
    }


    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        final ListBean image = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_AVATAR)
                .setId(1)
                .setImageUrl(TravelPreference.getCustomAppProfile(UserInfoType.USER_AVATAR.name()))
                .setListTag(ListTag.LIST_AVATER)
                .build();
        final ListBean name = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setDelegate(new IndexDelegate())
                .setText("姓名")
                .setValue(TravelPreference.getCustomAppProfile(UserInfoType.USERNAME.name()))
                .setListTag(ListTag.LIST_NAME)
                .build();
        final ListBean gender = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(3)
                .setText("性别")
                .setValue(TravelPreference.getCustomAppProfile(UserInfoType.USER_SEX.name()))
                .setListTag(ListTag.LIST_SEX)
                .build();
        final ListBean birth = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(4)
                .setText("生日")
                .setValue(TravelPreference.getCustomAppProfile(UserInfoType.USER_BIRTHDAY.name()))
                .setListTag(ListTag.LIST_BIRTH)
                .build();
        final ListBean email = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(5)
                .setText("设置邮箱")
                .setListTag(ListTag.LIST_EMAIL)
                .build();
        final ListBean phone = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(6)
                .setText("更改电话")
                .setListTag(ListTag.LIST_PHONE)
                .build();
        final ListBean password = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(7)
                .setText("修改密码")
                .setListTag(ListTag.LIST_PASSWORD)
                .build();
        data = new ArrayList<>();
        data.add(image);
        data.add(name);
        data.add(gender);
        data.add(birth);
        data.add(phone);
        data.add(email);
        data.add(password);
        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new UserProfileClickListener(this));
        CallbackManager.getInstance().addCallback(CallbackType.CHANGED_AVATER, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                userProfileMap.put("avatar",String.valueOf(args));
            }
        });
        llUserInfoNotChange.setOnClickListener(null);
    }

    // 加载开始编辑的动画资源
    public void changeBeginAnimation(){
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(
                btnChangeUserProfile,
                "alpha",
                1F,
                0F);

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                btnSubmitChange,
                "translationX",
                250F);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                btnSubmitChange,
                "alpha",
                0F,
                1.0F);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(
                btnCancelChange,
                "translationX",
                -250F);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(
                btnCancelChange,
                "alpha",
                0F,
                1.0F);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(600);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(
                animator0,
                animator1,
                animator2,
                animator3,
                animator4);
        set.start();
    }

    //加载结束编辑的动画资源
    public void changeEndAnimation(){
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(
                btnChangeUserProfile,
                "alpha",
                0F,
                1F);

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                btnSubmitChange,
                "translationX",
                -250F);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                btnSubmitChange,
                "alpha",
                1.0F,
                0F);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(
                btnCancelChange,
                "translationX",
                250F);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(
                btnCancelChange,
                "alpha",
                1F,
                0F);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(600);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(
                animator0,
                animator1,
                animator2,
                animator3,
                animator4);
        set.start();
    }
    public  void updateUserProfile(){
        userProfileMap.put("userName",TravelPreference.getCustomAppProfile("tempName"));
        userProfileMap.put("sex",TravelPreference.getCustomAppProfile("tempSex"));
        userProfileMap.put("birthday",TravelPreference.getCustomAppProfile("tempBirthday"));

       mapTest("avatar",UserInfoType.USER_AVATAR.name());
       mapTest("userName",UserInfoType.USERNAME.name());
       mapTest("sex",UserInfoType.USER_SEX.name());
       mapTest("birthday",UserInfoType.USER_BIRTHDAY.name());
       mapTest("phone",UserInfoType.USER_PHONE.name());
       mapTest("email",UserInfoType.USER_EMAIL.name());
       mapTest("password",UserInfoType.USER_PASSWORD.name());

        //通知服务器更新信息
        RestClient.builder()
                .url("user/updateUser")
                .params(userProfileMap)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getContext(),"编辑成功",Toast.LENGTH_SHORT).show();
                        TravelPreference.addCustomAppProfile(UserInfoType.USER_AVATAR.name(),
                                String.valueOf(userProfileMap.get("avatar")));
                        TravelPreference.addCustomAppProfile(UserInfoType.USERNAME.name(),
                                String.valueOf(userProfileMap.get("userName")));
                        TravelPreference.addCustomAppProfile(UserInfoType.USER_SEX.name(),
                                String.valueOf(userProfileMap.get("sex")));
                        TravelPreference.addCustomAppProfile(UserInfoType.USER_BIRTHDAY.name(),
                                String.valueOf(userProfileMap.get("birthday")));
                        TravelPreference.addCustomAppProfile(UserInfoType.USER_PHONE.name(),
                                String.valueOf(userProfileMap.get("phone")));
                        TravelPreference.addCustomAppProfile(UserInfoType.USER_EMAIL.name(),
                                String.valueOf(userProfileMap.get("email")));
                        TravelPreference.addCustomAppProfile(UserInfoType.USER_PASSWORD.name(),
                                String.valueOf(userProfileMap.get("password")));
                        getSupportDelegate().start(new PersonalDelegate());
                    }
                })
                .bulid()
                .post();
    }
    public  void calcelUpdateUserProfile(){
       //还原本地信息
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data.get(0).setmValue(TravelPreference.getCustomAppProfile(UserInfoType.USER_AVATAR.name()));
                data.get(1).setmValue(TravelPreference.getCustomAppProfile(UserInfoType.USERNAME.name()));
                data.get(2).setmValue(TravelPreference.getCustomAppProfile(UserInfoType.USER_SEX.name()));
                data.get(3).setmValue(TravelPreference.getCustomAppProfile(UserInfoType.USER_BIRTHDAY.name()));
                data.get(4).setmValue(TravelPreference.getCustomAppProfile(UserInfoType.USER_PHONE.name()));
                data.get(5).setmValue(TravelPreference.getCustomAppProfile(UserInfoType.USER_EMAIL.name()));
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void mapTest(String info,String infoType){
        if( userProfileMap.get(info) == null || userProfileMap.get(info).equals("")){
            userProfileMap.put(info,TravelPreference.getCustomAppProfile(infoType));
        }
    }
}

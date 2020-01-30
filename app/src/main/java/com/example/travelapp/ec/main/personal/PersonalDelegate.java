package com.example.travelapp.ec.main.personal;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.AccountManger;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.loader.TravelLoader;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.main.EcBottomDelegate;
import com.example.travelapp.ec.main.index.IndexDelegate;
import com.example.travelapp.ec.main.personal.collect.CollectDelegate;
import com.example.travelapp.ec.main.personal.list.ListAdapter;
import com.example.travelapp.ec.main.personal.list.ListBean;
import com.example.travelapp.ec.main.personal.list.ListItemType;
import com.example.travelapp.ec.main.personal.list.ListTag;
import com.example.travelapp.ec.main.personal.note.NoteDelegate;
import com.example.travelapp.ec.main.personal.note.NoteType;
import com.example.travelapp.ec.main.personal.profile.UserProfileDelegate;
import com.example.travelapp.ec.main.personal.setting.SettingDelegate;
import com.example.travelapp.ec.sign.SignInDelegate;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDelegate extends TravelDelegate {

    public static final String NOTe_TYPE = "NOTE_TYPE";
    private Bundle mArgs = null;
    @BindView(R2.id.rv_personal_setting)
    RecyclerView mRecycler = null;

    @BindView(R2.id.img_user_avatar)
    CircleImageView circleAvatar = null;

    @BindView(R2.id.tv_user_name)
    TextView userName = null;

    @OnClick(R2.id.img_user_avatar)
    void onClickAvatar(){
        boolean loginStatus = AccountManger.isSignIn();
        if(loginStatus){
            getSupportDelegate().start(new UserProfileDelegate());
        }else {
            Toast.makeText(getContext(),"您暂未登陆，请先登陆",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R2.id.ll_all_note)
    void onClickAllNote(){
        boolean loginStatus = AccountManger.isSignIn();
        if(loginStatus){
            mArgs.putString(NOTe_TYPE, NoteType.ALL.name());
            startOrderListByType();
        }else {
            Toast.makeText(getContext(),"您暂未登陆，请先登陆",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R2.id.ll_played_note)
    void onClickPlayedNote(){
        boolean loginStatus = AccountManger.isSignIn();
        if(loginStatus){
            mArgs.putString(NOTe_TYPE,NoteType.PLAYED.name());
            startOrderListByType();
        }else {
            Toast.makeText(getContext(),"您暂未登陆，请先登陆",Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R2.id.ll_like_to_play_note)
    void onClickLikeToPlayNote(){
        boolean loginStatus = AccountManger.isSignIn();
        if(loginStatus){
            mArgs.putString(NOTe_TYPE,NoteType.LIKE_TO_PLAY.name());
            startOrderListByType();
        }else {
            Toast.makeText(getContext(),"您暂未登陆，请先登陆",Toast.LENGTH_SHORT).show();
        }

    }
    private void startOrderListByType(){
        final NoteDelegate delegate = new NoteDelegate();
        delegate.setArguments(mArgs);
        getSupportDelegate().start(delegate);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter){
            return AnimationUtils.loadAnimation(getContext(),R.anim.fragment_right_in);
        }
        return AnimationUtils.loadAnimation(getContext(),R.anim.fragment_left_out);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = new Bundle();
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
//        final ListBean help = new ListBean.Builder()
//                .setItemType(ListItemType.ITEM_NORMAL)
//                .setListTag(ListTag.LIST_HELP_DIALOG)
//                .setId(1)
//                .setText("帮帮忙")
//                .build();
        if(AccountManger.isSignIn()){
            Glide.with(getContext())
                    .load(TravelPreference
                            .getCustomAppProfile(UserInfoType.USER_AVATAR.name()))
                    .into((ImageView) circleAvatar);
        }
        final ListBean collect = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setDelegate(new CollectDelegate())
                .setListTag(ListTag.LIST_COLLECT)
                .setText("我的收藏")
                .build();
        final ListBean setting = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setDelegate(new SettingDelegate())
                .setListTag(ListTag.LIST_SETTING)
                .setText("系统设置")
                .build();
        final ListBean exit = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setDelegate(new SignInDelegate())
                .setListTag(ListTag.LIST_EXIT)
                .setText("退出")
                .build();
        final List<ListBean> data = new ArrayList<>();
//        data.add(help);
        data.add(collect);
        data.add(setting);
        data.add(exit);

        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecycler.setAdapter(adapter);
        mRecycler.addOnItemTouchListener(new PersionalClickListener(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AccountManger.isSignIn()){
            Glide.with(getContext())
                    .load(TravelPreference
                            .getCustomAppProfile(UserInfoType.USER_AVATAR.name()))
                    .into((ImageView) circleAvatar);
            userName.setText(TravelPreference
                    .getCustomAppProfile(UserInfoType.USERNAME.name()));
        }
    }
}

package com.example.travelapp.ec.main.personal.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.travelapp.R;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.IError;
import com.example.travelapp.core.net.callback.IFailure;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.date.DateDialogUtil;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.main.personal.list.ListBean;
import com.example.travelapp.ec.main.personal.list.ListTag;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class UserProfileClickListener extends SimpleClickListener {

    private final TravelDelegate DELEGATE;

    private String[] mGender = new String[]{"男","女","保密"};

    public UserProfileClickListener(TravelDelegate delegate) {
        this.DELEGATE = delegate;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        ListTag tag = bean.getmListTag();
        switch (tag){
            case LIST_AVATER:
                Log.d("123456","点击头像");
                //开始照相机或选择图片
                CallbackManager.getInstance().addCallback(CallbackType.ON_CROP, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        ImageView avater = view.findViewById(R.id.img_arrow_avatar);
                        Glide.with(DELEGATE.getContext())
                                .load(args)
                                .into(avater);
                        //将图片上传到服务器
                        RestClient.builder()
                                .url("upload/avater")
                                .loader(DELEGATE.getContext())
                                .file(((Uri)args).getPath())
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        JSONObject data = JSON.parseObject(response);
                                        String status = data.getString("status");
                                        if(status.equals("success")){
                                            Log.d("123456789",response + "上传成功");
                                            //通知服务器更新信息
                                            String path = data.getString("path");
                                            Log.d("123456789","上传成功：" + path);
                                            IGlobalCallback  callback = CallbackManager.getInstance().
                                                    getCallback(CallbackType.CHANGED_AVATER);
                                            if (callback != null){
                                                callback.executeCallback(path);
                                            }
                                        }else if(status.equals("failure")){
                                            String reason = data.getString("reason");
                                            Toast.makeText(Travel.getApplicationContext(),reason,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .failure(new IFailure() {
                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(Travel.getApplicationContext(),"失败",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .error(new IError() {
                                    @Override
                                    public void onError(int code, String msg) {
                                        Toast.makeText(Travel.getApplicationContext(),msg + "错误",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .bulid()
                                .upload();
                    }
                });
                DELEGATE.startCameraWithCheck();
                break;
            case LIST_NAME:
                getNameDialog(view);
                break;
            case LIST_SEX:
                getGenderDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final TextView txtView = view.findViewById(R.id.tv_arrow_value);
                        txtView.setText(mGender[which]);
                        TravelPreference.addCustomAppProfile("tempSex",mGender[which]);
                        dialog.cancel();
                    }
                });
                break;
            case LIST_EMAIL:

                break;
            case LIST_BIRTH:
                final DateDialogUtil dateDialogUtil = new DateDialogUtil();
                TextView txt = view.findViewById(R.id.tv_arrow_value);
                dateDialogUtil.setDateListener(new DateDialogUtil.IDateListener() {
                    @Override
                    public void onDateChange(String date) {
                        txt.setText(date);
                        TravelPreference.addCustomAppProfile("tempBirthday",date);
                    }
                });
                try {
                    dateDialogUtil.showDialog(DELEGATE.getContext(),txt.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LIST_PHONE:

                break;
            case LIST_PASSWORD:

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    private void getGenderDialog(DialogInterface.OnClickListener listener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        builder.setSingleChoiceItems(mGender,0,listener);
        builder.show();
    }

    private void getNameDialog(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        View dialogView = View.inflate(DELEGATE.getContext(),R.layout.dialog_username,null);
        AlertDialog dialog = builder.setView(dialogView).create();

        AppCompatButton submit = dialogView.findViewById(R.id.btn_name_submit);
        EditText username = dialogView.findViewById(R.id.edit_input_username);
        AppCompatButton cancel = dialogView.findViewById(R.id.btn_name_cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = view.findViewById(R.id.tv_arrow_value);
                textView.setText(username.getText().toString());
                TravelPreference.addCustomAppProfile("tempName",username.getText().toString());
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

}

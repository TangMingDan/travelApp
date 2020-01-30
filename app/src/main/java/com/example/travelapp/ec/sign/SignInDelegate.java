package com.example.travelapp.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.IError;
import com.example.travelapp.core.net.callback.IFailure;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.ec.info.User;
import com.example.travelapp.ec.main.EcBottomDelegate;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录界面
 */

public class SignInDelegate extends TravelDelegate {

    @BindView(R2.id.edit_sign_in_num)
    TextInputEditText mNum = null;
    @BindView(R2.id.edit_sign_in_password)
    TextInputEditText mPassword = null;

    private ISignListener mSignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ISignListener){
            mSignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn(){
        if(checkForm()){
            RestClient.builder()
                    .url("user/findUserByPhone")
                    .param("phone",mNum.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            boolean flag = false;   //设置flag为false
                            JSONObject data = JSON.parseObject(response);
                            String status = data.getString("status");
                            if(status.equals("success")){
                                JSONObject userData = data.getJSONObject("data");
                                String password = userData.getString("password");
                                if(mPassword.getText().toString().equals(password)){
                                    SignHandler.onSignIn(userData.toString(),mSignListener);
                                    flag = true;  //flag为true为登陆成功
                                }
                            }
                            if(!flag){
                                Toast.makeText(getContext(),"电话或者密码错误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .error(new IError() {
                        @Override
                        public void onError(int code, String msg) {
                            Toast.makeText(getContext(),msg+"错误",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure() {
                            Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .bulid()
                    .post();

        }
    }

    @OnClick(R2.id.tv_just_look_as_visitor)
    void into(){
        startWithPop(new EcBottomDelegate());
    }


    @OnClick(R2.id.tv_link_sign_up)
    void onClickLink(){
        start(new SignUpDelegate());
    }

    private boolean checkForm(){
        boolean isPass = true;
        final String num = mNum.getText().toString();
        final String password = mPassword.getText().toString();
        if(num.isEmpty() || num.equals("")){
            mNum.setError("账号输入错误");
            isPass = false;
        }else {
            mNum.setError(null);
        }
        if(password.isEmpty() || password.length() < 6){
            mNum.setError("请至少输入6位密码");
            isPass = false;
        }else {
            mPassword.setError(null);
        }
        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {

    }

}

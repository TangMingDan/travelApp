package com.example.travelapp.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.example.travelapp.core.ui.date.DateDialogUtil;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

public class SignUpDelegate extends TravelDelegate {

    @BindView(R2.id.edit_sign_up_name)
    TextInputEditText mName = null;
    @BindView(R2.id.edit_sign_up_email)
    TextInputEditText mEmail = null;
    @BindView(R2.id.edit_sign_up_phone)
    TextInputEditText mPhone = null;
    @BindView(R2.id.edit_sign_up_password)
    TextInputEditText mPassword = null;
    @BindView(R2.id.edit_sign_up_re_password)
    TextInputEditText mRePassword = null;
    @BindView(R2.id.rg_user_sex)
    RadioGroup rgSex = null;
    @BindView(R2.id.tv_user_birthday)
    TextView mBirthday = null;
    @OnClick(R2.id.ll_user_birth)
    void onClickBirth(){
        final DateDialogUtil dateDialogUtil = new DateDialogUtil();

        dateDialogUtil.setDateListener(new DateDialogUtil.IDateListener() {
            @Override
            public void onDateChange(String date) {
                mBirthday.setText(date);
            }
        });
        try {
            dateDialogUtil.showDialog(this.getContext(),mBirthday.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ISignListener mSignListener = null;

    private String mUserSex = "男";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ISignListener){
            mSignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.btn_sign_up)
    void onClickSignUp(){
        if (checkForm()) {
            RestClient.builder()
                    .url("user/saveUser")
                    .param("userName", mName.getText().toString())
                    .param("sex",mUserSex)
                    .param("birthday",mBirthday.getText().toString())
                    .param("email", mEmail.getText().toString())
                    .param("phone", mPhone.getText().toString())
                    .param("password", mPassword.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            JSONObject data = JSON.parseObject(response);
                            String status = data.getString("status");
                            if(status.equals("success")){
                                SignHandler.onSignUp(response,mSignListener);
                            }else {
                                String reason = data.getString("reason");
                                Toast.makeText(getContext(),reason,Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .error(new IError() {
                        @Override
                        public void onError(int code, String msg) {
                            Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();
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

    @OnClick(R2.id.tv_link_sign_in)
    void onClickLink(){
        start(new SignInDelegate());
    }

    private boolean checkForm(){
        final String name = mName.getText().toString();
        final String email = mEmail.getText().toString();
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();
        final String rePassword = mRePassword.getText().toString();

        boolean isPass = true;

        if(name.isEmpty()){
            mName.setError("请输入姓名");
            isPass = false;
        }else {
            mName.setError(null);
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("错误的邮箱格式");
            isPass = false;
        }else {
            mEmail.setError(null);
        }

        if(phone.isEmpty() || phone.length() != 11){
            mPhone.setError("手机号码错误");
            isPass = false;
        }else {
            mPhone.setError(null);
        }

        if(password.isEmpty() || password.length() < 6){
            mPassword.setError("请至少填写6位数密码");
            isPass = false;
        }else {
            mPassword.setError(null);
        }

        if(rePassword.isEmpty() || password.length() < 6 || ! password.equals(rePassword)){
            mRePassword.setError("密码验证错误");
            isPass = false;
        }else {
            mRePassword.setError(null);
        }
        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_user_sex_man:
                        mUserSex = "男";
                        break;
                    case R.id.rb_user_sex_woman:
                        mUserSex = "女";
                    default:
                        break;
                }
            }
        });
    }
}

package com.example.travelapp.ec.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.core.app.AccountManger;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.info.User;

public class SignHandler {

    public static void onSignUp(String response, ISignListener signListener) {
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        updateUserInfo(profileJson.toString());
        signListener.onSignUpSuccess();
    }

    public static void onSignIn(String response, ISignListener signListener) {
        updateUserInfo(response);
        signListener.onSignInSuccess();
    }

    public static void updateUserInfo(String response) {
        final JSONObject profileJson = JSON.parseObject(response);
        final int userId = profileJson.getInteger("userId");
        final String name = profileJson.getString("userName");
        final String avatar = profileJson.getString("avatar");
        final String sex = profileJson.getString("sex");
        final String birthday = profileJson.getString("birthday");
        final String email = profileJson.getString("email");
        final String phone = profileJson.getString("phone");
        final String password = profileJson.getString("password");

        final User user = new User();
        user.setBirthday(birthday);
        user.setEmail(email);
        user.setAvatar(avatar);
        user.setPhone(phone);
        user.setUserId(userId);
        user.setSex(sex);
        user.setUserName(name);

        TravelPreference.addCustomAppProfile(UserInfoType.USER_PASSWORD.name(),password);
        TravelPreference.addCustomAppProfile(UserInfoType.USERNAME.name(), name);
        TravelPreference.addCustomAppProfile(UserInfoType.USER_BIRTHDAY.name(), birthday);
        TravelPreference.addCustomAppProfile(UserInfoType.USER_EMAIL.name(), email);
        TravelPreference.addCustomAppProfile(UserInfoType.USER_AVATAR.name(), avatar);
        TravelPreference.addCustomAppProfile(UserInfoType.USER_PHONE.name(), phone);
        TravelPreference.addCustomAppProfile(UserInfoType.USER_SEX.name(), sex);
        TravelPreference.addCustomAppProfile(UserInfoType.USER_ID.name(), String.valueOf(userId));

        //设置当前用户状态为登陆
        AccountManger.setSignState(true);
    }
}

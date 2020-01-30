package com.example.travelapp.core.app;


import com.example.travelapp.core.utils.storage.TravelPreference;

public class AccountManger {
    private enum SignTag{
        SIGN_TAG;
    }

    //保存用户登陆状态，登陆后调用
    public static void setSignState(boolean state){
        TravelPreference.setAppFlag(SignTag.SIGN_TAG.name(),state);
    }
    public static boolean isSignIn(){
        return TravelPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker){
        if(isSignIn()){
            checker.onSignIn();
        }else {
            checker.onNotSignIn();
        }
    }

}

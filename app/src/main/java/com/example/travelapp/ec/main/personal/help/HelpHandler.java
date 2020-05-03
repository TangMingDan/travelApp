package com.example.travelapp.ec.main.personal.help;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.travelapp.R;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.main.map.TravleMap;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.travelapp.core.app.Travel.getApplicationContext;

public class HelpHandler implements View.OnClickListener {

    private String[] sCity = new String[]{"重庆市", "天津市", "北京市", "上海市"};

    public TravelDelegate mDelegate = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_help_sos:
                getMessageAuthority(1);
                break;
            case R.id.btn_help_hospital:
                getMessageAuthority(2);
                break;
            case R.id.btn_little_help:
                getMessageAuthority(3);
                callPhone();
                break;
            default:
                break;
        }
    }

    public static class HelpHolder {
        public static HelpHandler HELP_INSTANCE = new HelpHandler();
    }

    public static HelpHandler getInstance() {
        return HelpHolder.HELP_INSTANCE;
    }

    public void showDialog(TravelDelegate delegate) {
        mDelegate = delegate;
        AlertDialog.Builder builder = new AlertDialog.Builder(mDelegate.getContext());
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(mDelegate.getContext(), R.layout.dialog_help, null);
        dialog.setView(dialogView);
        dialog.show();

        Button sosHelp = dialogView.findViewById(R.id.btn_help_sos);
        sosHelp.setOnClickListener(this);

        Button hospitalHelp = dialogView.findViewById(R.id.btn_help_hospital);
        hospitalHelp.setOnClickListener(this);

        Button littleHelp = dialogView.findViewById(R.id.btn_little_help);
        littleHelp.setOnClickListener(this);

        Button cancaleHelp = dialogView.findViewById(R.id.btn_help_canel);
        cancaleHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void getMessageAuthority(Object tag) {
        IGlobalCallback callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.SEND_SOS_MESSAGE);
        if (callback != null) {
            callback.executeCallback(tag);
        }
    }

    public void sendSosMessage(Object tag) {
        String num = null;
        StringBuilder currentPosition = new StringBuilder();
        LocationClient client = TravleMap.getInstance().mLocationClient;
        BDLocation bdLocation = client.getLastKnownLocation();
        currentPosition.append(bdLocation.getProvince()).append(" ");
        int i = 0;
        int length = sCity.length;
        if (bdLocation.getProvince() != null) {
            for (; i < length; i++) {
                if (bdLocation.getProvince().equals(sCity[i])) {
                    break;
                }
            }
            if (i == length) {
                currentPosition.append(bdLocation.getCity()).append(" ");
            }
            currentPosition.append(bdLocation.getDistrict()).append(" ")
                    .append(bdLocation.getStreet()).append(" ");

        }
        switch ((Integer) tag) {
            case 1:
                currentPosition.append("出现紧急危险情况");
                num =  TravelPreference.getCustomAppProfile(UserInfoType.SOS_PHONE.name());;
                break;
            case 2:
                currentPosition.append("需要医疗救护人员");
                num = TravelPreference.getCustomAppProfile(UserInfoType.HELP_PHONE.name());
                break;
            case 3:
                currentPosition.append("是我当前的地址，请马上联系我");
                num = TravelPreference.getCustomAppProfile(UserInfoType.FRIEND_PHONE.name());
                break;
            default:
                break;
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(num,
                null, currentPosition.toString(), null, null);

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     */
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + TravelPreference.getCustomAppProfile(UserInfoType.FRIEND_PHONE.name()));
        intent.setData(data);
        mDelegate.startActivity(intent);
    }
}

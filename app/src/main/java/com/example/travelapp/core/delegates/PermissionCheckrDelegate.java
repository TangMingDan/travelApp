package com.example.travelapp.core.delegates;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.ui.camera.CameraImageBean;
import com.example.travelapp.core.ui.camera.RequestCode;
import com.example.travelapp.core.ui.camera.TraveCamera;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.ec.main.map.TravleMap;
import com.example.travelapp.ec.main.personal.help.HelpHandler;
import com.yalantis.ucrop.UCrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public abstract class PermissionCheckrDelegate extends BaseDelegate{

    //不是直接调用方法
    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startCamera(){
        Log.d("123456","调用相机");
        TraveCamera.start(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,})
    void initMap(MapView mapView){
        TravleMap.getInstance().initMap(mapView);
    }

    @NeedsPermission({Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,})
    void sendMessage(Object tag){
        HelpHandler.getInstance().sendSosMessage(tag);
    }

    //发送紧急短信
    public void sendSosMessage(Object args){
        PermissionCheckrDelegatePermissionsDispatcher.sendMessageWithPermissionCheck(this,args);
    }

    //真正的调用相机方法
    public void startCameraWithCheck(){
        Log.d("123456","检查权限");
        PermissionCheckrDelegatePermissionsDispatcher.startCameraWithPermissionCheck(this);
    }
    //调用地图初始化
    public void initTravelMapWithCheck(MapView mapView){
        PermissionCheckrDelegatePermissionsDispatcher.initMapWithPermissionCheck(this,mapView);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onDenied(){
        Toast.makeText(getContext(),"不允许拍照",Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onNever(){
        Toast.makeText(getContext(),"永久拒绝使用",Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onRationale(PermissionRequest request){
        showRationaleDialog(request);
    }

    private void showRationaleDialog(final PermissionRequest request){
        new AlertDialog.Builder(getContext())
                .setPositiveButton("同意使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("权限管理")
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheckrDelegatePermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RequestCode.TAKE_PHOTO:
                    final Uri resultUri = CameraImageBean.getInstance().getPath();
                    UCrop.of(resultUri,resultUri)
                            .withMaxResultSize(400,400)
                            .start(getContext(),this);
                    Log.d("123456","拍照结束");
                    break;
                case RequestCode.PICK_PHOTO:
                    if(data != null){
                        final Uri pickPath = data.getData();
                        //从相册选择后，需要有个路径存放剪裁过的路径
                        final String pickCropPath = TraveCamera.createCropFile().getPath();
                        UCrop.of(pickPath,Uri.parse(pickCropPath))
                                .withMaxResultSize(400,400)
                                .start(getContext(),this);
                    }
                    Log.d("123456","相册结束");
                    break;
                case RequestCode.CROP_PHOTO:
                    final Uri cropUri = UCrop.getOutput(data);
                    //拿到剪裁后的数据进行处理
                    final IGlobalCallback<Uri> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);
                    Log.d("123456","开始裁剪");
                    if(callback != null){
                        callback.executeCallback(cropUri);
                    }
                    break;
                case RequestCode.CROP_ERROR:
                    Toast.makeText(getContext(),"裁剪出错",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}

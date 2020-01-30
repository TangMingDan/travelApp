package com.example.travelapp.core.ui.camera;

import android.net.Uri;
import android.os.FileUtils;

import com.example.travelapp.core.delegates.PermissionCheckrDelegate;
import com.example.travelapp.core.utils.file.MyFileUtils;

public class TraveCamera {

    public static Uri createCropFile(){
        return Uri.parse(MyFileUtils.createFile("crop_image",
                MyFileUtils.getFileNameByTime("IMG","jpg")).getPath());
    }

    public static void start(PermissionCheckrDelegate delegate){
        new CameraHanlder(delegate).showCameraDialog();
    }
}

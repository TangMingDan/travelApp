package com.example.travelapp.core.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.net.callback.IRequest;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.utils.file.MyFileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import androidx.core.content.FileProvider;
import okhttp3.ResponseBody;

public class SaveFileTask extends AsyncTask<Object,Void, File> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    private String downloadDir;
    private String extension;
    private String name;

    public SaveFileTask(IRequest request, ISuccess success) {
        this.REQUEST = request;
        this.SUCCESS = success;
    }

    @Override
    protected File doInBackground(Object... objects) {
        downloadDir = (String) objects[0];
        extension = (String) objects[1];
        name = (String) objects[2];
        final ResponseBody body = (ResponseBody) objects[3];
        final InputStream is = body.byteStream();
        if(downloadDir == null || downloadDir.equals("")){
            downloadDir = "down_loads";
        }
        if(extension == null || extension.equals("")){
            extension = "";
        }
        if(name == null){
            return MyFileUtils.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        }else {
            return MyFileUtils.writeToDisk(is,downloadDir,name);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }
        if(REQUEST != null){
            REQUEST.onRequestEnd();
        }
        autoInstallApk(file);
    }

    private void autoInstallApk(File file){
        if(MyFileUtils.getExtension((file.getPath())).equals("apk")){
            final Intent install = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // SDK24以上
                install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(Travel.getApplicationContext(),
                        "com.example.travelapp.fileprovider",file);
                Log.d("123456", String.valueOf(contentUri));
                install.setDataAndType(contentUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Travel.getApplicationContext().startActivity(install);
            } else {
                // SDK24以下
                try {
                    Runtime.getRuntime().exec("chmod 755" + file.getCanonicalPath());
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setAction(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    Travel.getApplicationContext().startActivity(install);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

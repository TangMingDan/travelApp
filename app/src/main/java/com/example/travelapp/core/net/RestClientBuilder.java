package com.example.travelapp.core.net;

import android.content.Context;

import com.example.travelapp.core.net.callback.IError;
import com.example.travelapp.core.net.callback.IFailure;
import com.example.travelapp.core.net.callback.IRequest;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.loader.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RestClientBuilder {
    private String mUrl = null;
    private static final Map<String,Object> PARAMS = RestCreator.getParams();
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private RequestBody mBody = null;
    private Context mContext = null;
    private LoaderStyle mLoaderStyle = null;
    private File mFile = null;

    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;

    public RestClientBuilder() {
    }

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String,Object> params){
        PARAMS.putAll(params);
        return this;
    }
    public final RestClientBuilder param(String key,Object value){
        PARAMS.put(key,value);
        return this;
    }

    public final RestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess = iSuccess;
        return this;
    }
    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure = iFailure;
        return this;
    }
    public final RestClientBuilder request(IRequest iRequest){
        this.mIRequest = iRequest;
        return this;
    }
    public final RestClientBuilder error(IError iError){
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder loader(Context context,LoaderStyle loaderStyle){
        this.mContext = context;
        this.mLoaderStyle = loaderStyle;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public final RestClientBuilder file(File file){
        this.mFile = file;
        return this;
    }
    /**
     * 上传文件
     * @param filepath
     * @return
     */
    public final RestClientBuilder file(String filepath){
        this.mFile = new File(filepath);
        return this;
    }

    /**
     * 下载的文件存放的文件目录
     * @param extension
     * @return
     */
    public final RestClientBuilder extension(String extension){
        this.mExtension = extension;
        return this;
    }
    /**
     * 下载的文件存放时的后缀
     * @param dir
     * @return
     */
    public final RestClientBuilder dir(String dir){
        this.mDownloadDir = dir;
        return this;
    }
    /**
     * 下载的文件存放时的文件名
     * @param  name
     * @return
     */
    public final RestClientBuilder name(String name){
        this.mName = name;
        return this;
    }

    public final RestClient bulid(){
        return new RestClient(mUrl,PARAMS,mIRequest,mISuccess,mIFailure,
                mIError,mBody,mLoaderStyle,mContext,mFile,mDownloadDir,mExtension,mName);
    }
}

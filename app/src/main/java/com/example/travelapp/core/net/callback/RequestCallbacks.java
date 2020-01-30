package com.example.travelapp.core.net.callback;

import com.example.travelapp.core.ui.loader.LoaderStyle;
import com.example.travelapp.core.ui.loader.TravelLoader;

import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallbacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;
    //handler尽量声明为static类型，避免内存泄漏
    private static final Handler HANDLER = new Handler();

    public RequestCallbacks(IRequest request, ISuccess success,
                            IFailure failure, IError error,LoaderStyle loaderStyle) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.LOADER_STYLE = loaderStyle;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if(response.isSuccessful()){
            if(call.isExecuted()){
               if(SUCCESS != null){
                   SUCCESS.onSuccess(response.body());
               }
            }
        }else {
            if(ERROR != null){
                ERROR.onError(response.code(),response.message());
            }
        }
        if(REQUEST != null){
            REQUEST.onRequestEnd();
        }
        stopLoading();
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if(FAILURE != null){
            FAILURE.onFailure();
        }
        if(REQUEST != null){
            REQUEST.onRequestEnd();
        }
        stopLoading();
    }

    private void stopLoading(){
        if(LOADER_STYLE != null){
            TravelLoader.stopLoading();
//            HANDLER.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    TravelLoader.stopLoading();
//                }
//            },1000);
        }
    }
}

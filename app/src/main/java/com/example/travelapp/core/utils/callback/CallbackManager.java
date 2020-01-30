package com.example.travelapp.core.utils.callback;

import java.util.WeakHashMap;
import java.util.logging.Handler;

public class CallbackManager {
    private static final WeakHashMap<Object,IGlobalCallback> CALLBACKS = new WeakHashMap<>();

    private static class Holder{
        private static final CallbackManager INSTANCE = new CallbackManager();
    }

    public static CallbackManager getInstance(){
        return Holder.INSTANCE;
    }
    public CallbackManager addCallback(Object tag,IGlobalCallback callback){
        CALLBACKS.put(tag,callback);
        return this;
    }

    public IGlobalCallback getCallback(Object tag){
        return CALLBACKS.get(tag);
    }
}

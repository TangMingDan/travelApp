package com.example.travelapp.core.app;

import android.content.Context;
import android.os.Handler;

import java.util.HashMap;

public final class Travel {

    /**
     * 存放全局的ApplicationContext
     * @param context
     * @return
     */
    public static Configurator init(Context context){
        getConfigurations().put(ConfigType.APPLICATION_CONTEXT.name(),context.getApplicationContext());
        return Configurator.getInstance();
    }


    /**
     * 获取配置类
     * @return
     */
    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    /**
     * 获取全局配置的Map
     * @return
     */
    public static HashMap<String,Object> getConfigurations(){
        return getConfigurator().getTravelConfigs();
    }

    /**
     * 获取Applicationtext
     * @return
     */
    public static Context getApplicationContext(){
        return getConfigurator().getConfiguration(ConfigType.APPLICATION_CONTEXT);
//        return (Context) getConfigurations().get(ConfigType.APPLICATION_CONTEXT.name());
    }
    /**
     * 获取某个配置
     * @return
     */
    public static <T> T getConfiguration(Enum<ConfigType> key){
        return (T) getConfigurator().getConfiguration(key);
//        return (Context) getConfigurations().get(ConfigType.APPLICATION_CONTEXT.name());
    }

    public static Handler getHandler() {
        return getConfigurator().getConfiguration(ConfigType.HANDLER);
    }
}

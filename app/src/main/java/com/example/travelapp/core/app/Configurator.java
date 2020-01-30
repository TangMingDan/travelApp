package com.example.travelapp.core.app;

import android.app.Activity;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * 配置文件存储和获取
 */

public class Configurator {
    /**
     * 存放配置信息
     */
    private static final HashMap<String,Object> TRAVEL_CONFIGS = new HashMap<>();

    /**
     * 存放自己的字体图标
     */
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();

    private static final Handler HANDLER = new Handler();

    public Configurator() {
        TRAVEL_CONFIGS.put(ConfigType.CONFIG_READY.name(),false);
        TRAVEL_CONFIGS.put(ConfigType.HANDLER.name(),HANDLER);
    }

    /**
     * 获取配置类
     * @return
     */
    public static Configurator getInstance(){
        return ConfiguratorHoler.INSTANCE;
    }

    private static class ConfiguratorHoler{
        private final static Configurator INSTANCE = new Configurator();
    }

    /**
     * 获取存放配置信息的HashMap
     * @return
     */
    final HashMap<String,Object> getTravelConfigs(){
        return TRAVEL_CONFIGS;
    }

    /**
     * 完成配置
     */
    public final void configure(){
        initIcons();
        TRAVEL_CONFIGS.put(ConfigType.CONFIG_READY.name(),true);
        Utils.init(Travel.getApplicationContext());
        SDKInitializer.initialize(Travel.getApplicationContext());
    }

    /**
     * 配置ApiHost
     * @param host
     * @return
     */
    public final Configurator withApiHost(String host){
        TRAVEL_CONFIGS.put(ConfigType.API_HOST.name(),host);
        return this;
    }

    public final Configurator withActivity(Activity activity){
        TRAVEL_CONFIGS.put(ConfigType.ACTIVITY.name(),activity);
        return this;
    }

    /**
     * 检查配置是否完成
     */
    private void checkConfiguration(){
        final boolean isReady = (boolean) TRAVEL_CONFIGS.get(ConfigType.CONFIG_READY.name());
        if(!isReady){
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    /**
     * 获取配置信息
     * @param key
     * @param <T>
     * @return
     */
    public final <T> T getConfiguration(Enum<ConfigType> key){
        checkConfiguration();
        return (T) TRAVEL_CONFIGS.get(key.name());
    }

    /**
     * 初始化字体图标
     */
    private void initIcons(){
        if(ICONS.size() > 0){
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    /**
     * 添加字体图标
     * @param descriptor
     * @return
     */
    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return  this;
    }
}

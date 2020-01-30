package com.example.travelapp.core.ui.gridLayout;

/**
 * Created by 朱旭辉
 * Time  2019/9/2
 * Describe:分享碎片展示图片的信息
 */

public class ClassifyImage {
    private String url;
    private int width;
    private int height;

    public ClassifyImage(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;

    }
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return url;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    @Override
    public String toString() {
        return "image---->>url="+url+"width="+width+"height"+height;
    }
}

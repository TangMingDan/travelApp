package com.example.travelapp.core.ui.gridLayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;

public class CustomImageView extends ImageView {
    private String url;
    private boolean isAttachedToWindow;
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    //重写了onTouchEvent方法后，当屏幕有Touch事件时，此方法就会被调用。
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://屏幕检测到第一个触点按下之后就会触发到这个事件
                Drawable drawable=getDrawable();
                if(drawable!=null) {
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE://滑动
                break;
            case MotionEvent.ACTION_CANCEL://不是由用户直接触发，由系统在需要的时候触发，例如当父view通过使函数
            case MotionEvent.ACTION_UP://抬起
                Drawable drawableUp=getDrawable();
                if(drawableUp!=null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }

        return super.onTouchEvent(event);
    }
    //该方法在view与window绑定时被调用，且只会被调用一次，其在view的onDraw方法之前调用
    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url);
        super.onAttachedToWindow();
    }
    // 该方法在view被销毁时被调用
    @Override
    public void onDetachedFromWindow() {
        Picasso.with(getContext()).cancelRequest(this);
        isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }
    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {

                /**Picasso 是下载图片然后缓存完整的大小到本地，比如说图片的大小是1080p的，
                 * 之后如果我需要同一张图片，
                 * 就会返回这张 full size 的，如果我需要resize，
                 * 也是对这种 full size 的做 resize。
                 */
                Picasso.with(getContext()).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
            }
        }
    }

}

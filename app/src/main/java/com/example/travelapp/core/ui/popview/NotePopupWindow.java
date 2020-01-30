package com.example.travelapp.core.ui.popview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.travelapp.R;



public class NotePopupWindow extends PopupWindow {
    private Button delect_collect;
    private Button delect_collect_cancel_btn;
    private View mMenuView;
    private PopupWindow popupWindow;
    public NotePopupWindow(Context context, View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_delect_note, null);
        delect_collect = mMenuView.findViewById(R.id.delect_collect);
        delect_collect_cancel_btn = mMenuView.findViewById(R.id.delect_collect_cancel_btn);
        //设置按钮监听
        delect_collect.setOnClickListener(itemsOnClick);
        delect_collect_cancel_btn.setOnClickListener(itemsOnClick);
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // ☆ 注意： 必须要设置背景，播放动画有一个前提 就是窗体必须有背景
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(android.R.style.Animation_InputMethod);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}

package com.example.travelapp.ec.main.personal.note;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.travelapp.R;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.popview.NotePopupWindow;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;

import java.io.IOException;
import java.time.LocalDate;

import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoteItemClickListener extends SimpleClickListener {

    private TravelDelegate mDelegate;
    NotePopupWindow popupWindow;
    private int id = 1;
    private NoteAdapter adapter;
    private int mPosition;

    private NoteItemClickListener(TravelDelegate delegate,NoteAdapter adapter) {
        this.mDelegate = delegate;
        this.adapter = adapter;
    }

    public static SimpleClickListener create(TravelDelegate delegate,NoteAdapter adapter){
        return new NoteItemClickListener(delegate,adapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MultipleItemBean bean = (MultipleItemBean) adapter.getData().get(position);
        WriteDetialNoteDelegate detialNoteDelegate =
                WriteDetialNoteDelegate.newInstance(bean.getField(MultipleFields.TITLE),
                        bean.getField(MultipleFields.TIME),
                        bean.getField(MultipleFields.TEXT),
                        bean.getField(MultipleFields.NUM),
                        bean.getField(MultipleFields.ITEM_TYPE),
                        bean.getField(MultipleFields.CONTENT));
        mDelegate.start(detialNoteDelegate);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        MultipleItemBean bean = (MultipleItemBean) adapter.getData().get(position);
        id = bean.getField(MultipleFields.ID);
        //实例化SelectPicPopupWindow
        popupWindow = new NotePopupWindow(mDelegate.getContext(),itemsOnClick);
        //显示窗口
        popupWindow.showAtLocation(mDelegate.getActivity().getWindow().getDecorView(),
                Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        mPosition = position;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){
        public void onClick(View v) {
            popupWindow.dismiss();
            switch (v.getId()) {
                case R.id.delect_collect:
                    RestClient.builder()
                            .url("note/deleteNote")
                            .loader(mDelegate.getContext())
                            .param("id",id)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    popupWindow.dismiss();
                                    Toast.makeText(mDelegate.getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                                    adapter.remove(mPosition);
                                }
                            })
                            .bulid()
                            .get();
                    break;
                case R.id.delect_collect_cancel_btn:
                    popupWindow.dismiss();
                    break;
                default:
                    break;
            }


        }
    };
}

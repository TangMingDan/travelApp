package com.example.travelapp.ec.main.personal;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.travelapp.R;
import com.example.travelapp.core.app.AccountManger;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.ec.main.personal.help.HelpHandler;
import com.example.travelapp.ec.main.personal.list.ListBean;
import com.example.travelapp.ec.main.personal.list.ListTag;
import com.example.travelapp.ec.main.personal.note.NoteType;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import retrofit2.http.DELETE;

import static com.baidu.mapapi.BMapManager.getContext;

public class PersionalClickListener extends SimpleClickListener {
    private final TravelDelegate DELEGATE;

    public PersionalClickListener(TravelDelegate delegate) {
        this.DELEGATE = delegate;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        switch (bean.getmListTag()){
            case LIST_COLLECT:
                boolean loginStatus = AccountManger.isSignIn();
                if(loginStatus){
                    DELEGATE.getSupportDelegate().start(bean.getmDelegate());
                }else {
                    Toast.makeText(DELEGATE.getContext(),"您暂未登陆，请先登陆",Toast.LENGTH_SHORT).show();
                }
                break;
            case LIST_SETTING:
                DELEGATE.getSupportDelegate().start(bean.getmDelegate());
                break;
            case LIST_EXIT:
                DELEGATE.getSupportDelegate().start(bean.getmDelegate());
                AccountManger.setSignState(false);
                break;

        }
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

}

package com.example.travelapp.ec.main.personal.setting;

import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.travelapp.R;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.main.personal.list.ListBean;

import androidx.appcompat.widget.AppCompatButton;

public class SettingClickListener extends SimpleClickListener {

    private final TravelDelegate DELEGATE;

    public SettingClickListener(TravelDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        int id = bean.getmId();
        switch (id){
            case 1:
                getDialog(view);
                break;
            case 2:
                //设置消息推送的开关
                break;
            case 3:
                DELEGATE.getSupportDelegate().start(bean.getmDelegate());
                break;
            default:

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

    private void getDialog(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        View dialogView = View.inflate(DELEGATE.getContext(), R.layout.dialog_num_help,null);
        AlertDialog dialog = builder.setView(dialogView).create();

        AppCompatButton submit = dialogView.findViewById(R.id.btn_num_submit);
        EditText userNum= dialogView.findViewById(R.id.edit_input_num_help);
        userNum.setText(TravelPreference.getCustomAppProfile(UserInfoType.FRIEND_PHONE.name()));
        AppCompatButton cancel = dialogView.findViewById(R.id.btn_num_cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = view.findViewById(R.id.tv_arrow_value);
                textView.setText(userNum.getText());
                //本地保存textView.getText()
                TravelPreference.addCustomAppProfile(UserInfoType.FRIEND_PHONE.name(),userNum.getText().toString());
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

}

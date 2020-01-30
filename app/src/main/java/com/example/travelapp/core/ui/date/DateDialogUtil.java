package com.example.travelapp.core.ui.date;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;

public class DateDialogUtil {
    private String data = null;
    public interface IDateListener{
        void onDateChange(String date);
    }

    private IDateListener dateListener = null;

    public void setDateListener(IDateListener listener){
        this.dateListener = listener;
    }

    public void showDialog(Context context,String birth) throws Exception {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        final LinearLayout ll = new LinearLayout(context);
        final DatePicker picker = new DatePicker(context);
        final LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        picker.setLayoutParams(lp);
        data = birth;
        int year = 90;
        int month = 0;
        int day = 1;
        if(!birth.equals("未设置生日") && !birth.equals("暂无选择")){
            Date date = format.parse(birth);
            year = date.getYear();
            month = date.getMonth();
            day = date.getDate();
//            Log.d("1234560","brith:" +birth);
//            Log.d("1234560","date:" + date + "" +
//                    "year:" + year + "" +
//                    "month:" + month + "" +
//                    "day:" + day);
        }
        picker.init(year + 1900, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(year,monthOfYear,dayOfMonth);
                data = format.format(calendar.getTime());
            }
        });

        ll.addView(picker);
        new AlertDialog.Builder(context)
                .setTitle("选择日期")
                .setView(ll)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dateListener != null){
                            dateListener.onDateChange(data);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();


    }

}

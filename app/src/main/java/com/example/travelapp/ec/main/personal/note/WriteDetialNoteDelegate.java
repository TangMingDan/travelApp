package com.example.travelapp.ec.main.personal.note;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.IError;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.date.DateDialogUtil;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.main.personal.note.sceneryName.SceneryNameDelegate;
import com.example.travelapp.ec.main.sort.content.ContentDelegate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 写旅行记事的fragment
 */
public class WriteDetialNoteDelegate extends TravelDelegate {
    @BindView(R2.id.edit_note_input_title)
    EditText editNoteTitle = null;

    @BindView(R2.id.ll_note_detial_time)
    LinearLayout llTime = null;
    @BindView(R2.id.tv_note_detial_time)
    TextView tvNoteTime = null;

    @BindView(R2.id.ll_note_detial_attraction)
    LinearLayout llAttraction = null;
    @BindView(R2.id.tv_note_detial_attraction)
    TextView tvNoteAttraction = null;


    @BindView(R2.id.edit_note_input_content)
    EditText editNoteContent = null;

    private int sceneryId = -1;
    private int state = 1;   //判断note状态，1为想去玩，2为去玩过
    private String title = null;
    private String content = null;
    private String time = null;
    private String sceneryName = null;

    WeakHashMap<String,Object> noteDataMap = new WeakHashMap<>();

    @OnClick(R2.id.ll_note_detial_time)
    void onClickTime(){
        final DateDialogUtil dateDialogUtil = new DateDialogUtil();

        dateDialogUtil.setDateListener(new DateDialogUtil.IDateListener() {
            @Override
            public void onDateChange(String date) {
               tvNoteTime.setText(date);
            }
        });
        try {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year,month,day);
            String data = format.format(calendar.getTime());
            dateDialogUtil.showDialog(this.getContext(),data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R2.id.ll_note_detial_attraction)
    void onClickAttraction(){
        Toast.makeText(this.getContext(),"请选择景点",Toast.LENGTH_SHORT).show();
        CallbackManager.getInstance().addCallback(CallbackType.WRITE_SCENERY, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                String sceneryName = String.valueOf(args).substring(String.valueOf(args).lastIndexOf("/") + 1);
                tvNoteAttraction.setText(sceneryName);
                sceneryId = Integer.parseInt(String.valueOf(args).substring(0,String.valueOf(args).lastIndexOf("/")));

            }
        });
        SceneryNameDelegate delegate = new SceneryNameDelegate();
        start(delegate);
    }

    @OnClick(R2.id.btn_note_travel_canel)
    void onClickCancel(){
        pop();
    }
    @OnClick(R2.id.btn_note_travel_save)
    void onClickSave(){
        updateNote();
    }

    @BindView(R2.id.rg_note_play)
    RadioGroup radioGroup = null;

    public static WriteDetialNoteDelegate newInstance(String title,String time,String sceneryName,
                                                      int sceneryId,int state,String conetent){
        final Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("time",time);
        args.putString("sceneryName",sceneryName);
        args.putInt("sceneryId",sceneryId);
        args.putInt("state",state);
        args.putString("content",conetent);
        final WriteDetialNoteDelegate delegate = new WriteDetialNoteDelegate();
        delegate.setArguments(args);
        return delegate;
    }
    public static WriteDetialNoteDelegate newInstance(){
        final WriteDetialNoteDelegate delegate = new WriteDetialNoteDelegate();
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if(args != null){
            title = args.getString("title");
            content = args.getString("content");
            time = args.getString("time");
            sceneryName = args.getString("sceneryName");
            sceneryId = args.getInt("sceneryId");
            state = args.getInt("state");
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_note_detial;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        initData();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_note_attraction_want_to_play:
                        state = 1;
                        break;
                    case R.id.rb_note_attraction_played:
                        state = 2;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void initData(){
        editNoteTitle.setText(title);
        editNoteContent.setText(content);
        tvNoteTime.setText(time);
        tvNoteAttraction.setText(sceneryName);
        if(state == 1){
            radioGroup.check(R.id.rb_note_attraction_want_to_play);
        }else {
            radioGroup.check(R.id.rb_note_attraction_played);
        }
    }

    public void updateNote(){
        if(state == 0){
            state += 2;
        }
        noteDataMap.put("noteTitle",editNoteTitle.getText().toString());
        noteDataMap.put("noteContent",editNoteContent.getText().toString());
        noteDataMap.put("noteTime",tvNoteTime.getText().toString());
        noteDataMap.put("sceneryId",sceneryId);
        noteDataMap.put("state",state);
        noteDataMap.put("userId", TravelPreference.getCustomAppProfile(UserInfoType.USER_ID.name()));

        RestClient.builder()
                .url("note/saveNote")
                .params(noteDataMap)
                .loader(this.getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        pop();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .bulid()
                .post();

    }
}

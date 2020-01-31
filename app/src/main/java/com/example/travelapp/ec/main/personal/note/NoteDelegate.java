package com.example.travelapp.ec.main.personal.note;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.ec.main.index.IndexItemClickListener;
import com.example.travelapp.ec.main.personal.PersonalDelegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class NoteDelegate extends TravelDelegate {

    @BindView(R2.id.rv_note)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.srl_note)
    SwipeRefreshLayout mRefreshLayout = null;
    @OnClick(R2.id.fltbtn_add_note)
    void onClickAddNote(){
        WriteDetialNoteDelegate writeDetialNoteDelegate = WriteDetialNoteDelegate.newInstance();
        getSupportDelegate().start(writeDetialNoteDelegate);
    }


    private NoteRefreshHandler mHandler = null;
    private String mType = null;
    private String mUrl = null;
    private int state = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        mType = args.getString(PersonalDelegate.NOTe_TYPE);
        if(mType.equals(NoteType.ALL.name())){
            mUrl = "note/findNoteByUserId";
        }else if(mType.equals(NoteType.LIKE_TO_PLAY.name())){
            mUrl = "note/findNoteByState";
            state = 1;
        }else if(mType.equals(NoteType.PLAYED.name())){
            mUrl = "note/findNoteByState";
            state = 2;
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_note;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        mHandler = new NoteRefreshHandler(mRefreshLayout,mRecyclerView,this);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    private void initRecyclerView() {
        LinearLayoutManager manger = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manger);
//        mRecyclerView.addOnItemTouchListener(NoteItemClickListener.create(this));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
        mHandler.submit(mUrl,state);
    }
}

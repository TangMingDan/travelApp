package com.example.travelapp.ec.main.detial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.RestClientBuilder;
import com.example.travelapp.core.net.callback.IError;
import com.example.travelapp.core.net.callback.IFailure;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.loader.TravelLoader;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemarkDelegate extends TravelDelegate {


    @BindView(R2.id.tv_send)
    TextView send = null;

    @OnClick(R2.id.tv_cancle)
    void onCancel() {
        pop();
    }
    @BindView(R2.id.et_content)
    EditText editContent = null;
    @BindView(R2.id.et_title)
    EditText editTitle = null;
    @BindView(R2.id.publishGridView)
    GridView publishGridView = null;

    private int size = 0; //记录图片数量
    private String content = null;
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private SendShareImageAdapter sendShareImageAdapter = null;

    private static final String ARG_SCENERY_ID = "ARG_SCENERY_ID";
    private int mSceneryId = -1;

    public static RemarkDelegate create(@NonNull int goodsId){
        final Bundle args = new Bundle();
        args.putInt(ARG_SCENERY_ID,goodsId);
        final RemarkDelegate delegate = new RemarkDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if(args != null){
            mSceneryId = args.getInt(ARG_SCENERY_ID);
            Toast.makeText(getContext(),mSceneryId + "",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_share_remark;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        sendShareImageAdapter = new SendShareImageAdapter(imageItems, this);
        publishGridView.setAdapter(sendShareImageAdapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TravelLoader.showLoading(getContext());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRemark();
                    }
                }).start();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {

            if (data != null && requestCode == 200) {
                //用于转换
                ArrayList<ImageItem> imageItemsGet = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                imageItems.clear();
                for (int i = 0; i < imageItemsGet.size(); i++) {
                    imageItems.add(imageItemsGet.get(i));
                }
                sendShareImageAdapter.notifyDataSetChanged();
                size = imageItems.size();
            } else {
//                ToastUtils.ToastShortTime("没有选择图片");
            }
        }
    }


    public void sendRemark() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("remarkTitle", editTitle.getText().toString())
                .addFormDataPart("remarkContent", editContent.getText().toString())
                .addFormDataPart("remarkTime", time())
                .addFormDataPart("sceneryId", String.valueOf(mSceneryId))
                .addFormDataPart("userId", TravelPreference.getCustomAppProfile(UserInfoType.USER_ID.name()));
        if (imageItems != null && size > 0) {

            List<File> fileList = new ArrayList<>();//装文件
            int imageItemSize = imageItems.size();
            for (int i = 0; i < imageItemSize; i++) {
//                filePaths[i] = imageItems.get(i).path;

                File img = new File(imageItems.get(i).path);
                fileList.add(img);
            }

            int fileSize = fileList.size();
            for (int i = 0; i < fileSize; i++) {
                builder.addFormDataPart("file",fileList.get(i).getName(),
                        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                        RequestBody.create(MediaType.parse("image/*"), fileList.get(i)));
            }
        }
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url("http://49.232.144.185:8080/travelWeb_ssm/remark/saveRemark")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TravelLoader.stopLoading();
                        Toast.makeText(getContext(),e.getMessage() + "错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                TravelLoader.stopLoading();
                String responseData;
                responseData = response.body().string();
                pop();

            }
        });

    }

    public String time() {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day);
        String time = format.format(calendar.getTime());
        return time;
    }

}

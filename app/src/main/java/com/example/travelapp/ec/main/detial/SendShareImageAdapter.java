package com.example.travelapp.ec.main.detial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.travelapp.R;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.utils.dimen.CircleTransform;
import com.example.travelapp.core.utils.dimen.ShareImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;


public class SendShareImageAdapter extends BaseAdapter {
    private ArrayList<ImageItem> imageItems;
    private final int SUCCESSFUL = 200;
    private TravelDelegate delegate = null;
    public SendShareImageAdapter(ArrayList<ImageItem> imageItems,TravelDelegate delegate) {
        this.imageItems = imageItems;
        this.delegate = delegate;
    }
    @Override
    public int getCount() {
        if (imageItems == null){
            return 1;
        } else{
            return imageItems.size()+1;
        }
    }

    @Override
    public Object getItem(int position) {
        return imageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SendShareImageAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new SendShareImageAdapter.ViewHolder();
            convertView = LayoutInflater.from(Travel.getApplicationContext()).inflate(R.layout.layout_shareimage_grid, null);
            holder.image_voice = (ImageView) convertView.findViewById(R.id.gird_img);
            convertView.setTag(holder);
        } else {
            holder = (SendShareImageAdapter.ViewHolder) convertView.getTag();
        }
        if (imageItems == null) {//没有图片
            holder.image_voice.setImageResource(R.mipmap.add_icon);
        } else {//有图片
            if (position == imageItems.size()) {//最后一张
                holder.image_voice.setImageResource(R.mipmap.add_icon);
            } else {//不是最后一张
                File file = new File(imageItems.get(position).path);//得到路径
                if (file.exists()) {
                    /**
                     * Bitmap位图包括像素以及长、宽、颜色等描述信息。
                     * 长宽和像素位数是用来描述图片的，可以通过这些信息计算出图片的像素占用内存的大小。
                     *
                     * 位图可以理解为一个画架，把图放到上面然后可以对图片做一些列的处理。
                     * 位图文件图像显示效果好，但是非压缩格式，需要占用较大的存储空间。
                     */
                    Bitmap bm = BitmapFactory.decodeFile(imageItems.get(position).path);
                    //图片剪切
                    holder.image_voice.setImageBitmap(CircleTransform.centerSquareScaleBitmap(bm,100));
                }
            }
        }
        holder.image_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((imageItems != null && position == imageItems.size()) || imageItems == null) {
                    addImage();
                }
            }
        });
        return convertView;

    }

    private void addImage() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ShareImageLoader());
        imagePicker.setMultiMode(true);   //多选
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setSelectLimit(9);    //最多选择X张
        imagePicker.setCrop(false);       //不进行裁剪
        Intent intent = new Intent(delegate.getContext(), ImageGridActivity.class);
        delegate.startActivityForResult(intent, SUCCESSFUL);
    }

    class ViewHolder {
        private ImageView image_voice;
    }
}

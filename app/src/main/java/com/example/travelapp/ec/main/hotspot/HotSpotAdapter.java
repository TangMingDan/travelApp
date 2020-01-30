package com.example.travelapp.ec.main.hotspot;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.core.ui.gridLayout.ClassifyImage;
import com.example.travelapp.core.ui.gridLayout.CustomImageView;
import com.example.travelapp.core.ui.gridLayout.NineGridLayout;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.ui.recycler.MultipleviewHoloder;
import com.example.travelapp.core.utils.dimen.ScreenTools;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HotSpotAdapter extends MultipleRecyclerAdapter {

    private NineGridLayout ivMore = null;
    private CustomImageView ivOne = null;
    private Context context;

    protected HotSpotAdapter(List<MultipleItemBean> data,Context context) {
        super(data);
        addItemType(ItemType.SHARE_PART, R.layout.item_hot_spot);
        this.context = context;
    }

    public static HotSpotAdapter create(List<MultipleItemBean> data,Context context){
        return new HotSpotAdapter(data,context);
    }

    @Override
    protected void convert(@NonNull MultipleviewHoloder helper, MultipleItemBean item) {
        super.convert(helper, item);
        /**
         * imageList 只能在这里加载，因为如果在构造函数中加载那刷新过后imageList是不会更新的，导致图片于描述不对应
         */
        final ArrayList<ClassifyImage> classifyImages = new ArrayList<>();
        List<String> images = item.getField(MultipleFields.BANNERS);
        int size = images.size();

        for (int i = 0; i < size; i++) {
            classifyImages.add(new ClassifyImage(images.get(i),150,150));
        }

        helper.setText(R.id.tv_hot_spot_name,item.getField(MultipleFields.NAME));
        helper.setText(R.id.tv_hot_spot_scenery_name,item.getField(MultipleFields.TEXT));
        helper.setText(R.id.tv_hot_spot_title,item.getField(MultipleFields.TITLE));
        helper.setText(R.id.tv_hot_spot_content,item.getField(MultipleFields.CONTENT));
        helper.setText(R.id.tv_hot_spot_time,item.getField(MultipleFields.TIME));

        Glide.with(mContext)
                .load(item.getField(MultipleFields.IMAGE_URL) + "")
                .into((ImageView) helper.getView(R.id.deimg_head));
        ivMore = helper.getView(R.id.iv_ngrid_layout);
        ivOne = helper.getView(R.id.iv_oneimage);
        if (classifyImages.isEmpty()) {//没有图片，隐形
            ivMore.setVisibility(View.GONE);
            ivOne.setVisibility(View.GONE);
        } else if (classifyImages.size() == 1) {//有一张图片NineGridlayout隐形
            ivMore.setVisibility(View.GONE);
            ivOne.setVisibility(View.VISIBLE);
            ivOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String imgUrl = imageList.get(0).getUrl();
//                    DialogShow.dialogSetTheme(R.style.dialogT);
//                    DialogShow.showDialog(context,imgUrl);
                }
            });
            handlerOneImage(classifyImages.get(0));
        } else {//CustomImageView隐形
           ivMore.setVisibility(View.VISIBLE);
            ivOne.setVisibility(View.GONE);

            ivMore.setImagesData(classifyImages);
        }
    }
    private void handlerOneImage(ClassifyImage image) {
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(context);
        totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
        imageWidth = screentools.dip2px(image.getWidth());//应该是获取image宽度
        imageHeight = screentools.dip2px(image.getHeight());//应该是获取image高度
        if (image.getWidth() <= image.getHeight()) {//图片如果高度大于宽度
            if (imageHeight > totalWidth) {//图片如果高度大于总高度，调整缩放比例
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {//如过宽度大于总宽度调整缩放比例
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }
        //设置一张图片时的高度和宽度
        ViewGroup.LayoutParams layoutparams = ivOne.getLayoutParams();
        layoutparams.height = imageHeight;
        layoutparams.width = imageWidth;
        ivOne.setLayoutParams(layoutparams);
        ivOne.setClickable(true);
        ivOne.setScaleType(ImageView.ScaleType.FIT_XY);
        ivOne.setImageUrl(image.getUrl());

    }
}

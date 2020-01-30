package com.example.travelapp.ec.main.personal.list;

import android.widget.CompoundButton;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.travelapp.core.delegates.TravelDelegate;

public class ListBean implements MultiItemEntity {

    private int mItemType = 0;
    private String mImageUrl = null;
    private String mText = null;
    private String mValue = null;
    private int mId = 0;
    private ListTag mListTag = null;
    private TravelDelegate mDelegate = null;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = null;

    private ListBean(int mItemType, String mImageUrl, String mText, String mValue,
                     int mId, ListTag listTag,TravelDelegate mDelegate,
                     CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mItemType = mItemType;
        this.mImageUrl = mImageUrl;
        this.mText = mText;
        this.mValue = mValue;
        this.mId = mId;
        this.mListTag = listTag;
        this.mDelegate = mDelegate;
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    public ListTag getmListTag() {
        return mListTag;
    }

    public void setmListTag(ListTag mListTag) {
        this.mListTag = mListTag;
    }

    public int getmItemType() {
        return mItemType;
    }

    public void setmItemType(int mItemType) {
        this.mItemType = mItemType;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public TravelDelegate getmDelegate() {
        return mDelegate;
    }

    public void setmDelegate(TravelDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    public CompoundButton.OnCheckedChangeListener getmOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setmOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }



    public static final class Builder{
        private int id = 0;
        private int itemType = 0;
        private String imageUrl = null;
        private String text = null;
        private String value = null;
        private ListTag listTag = null;
        private CompoundButton.OnCheckedChangeListener checkedChangeListener = null;
        private TravelDelegate delegate = null;


        public Builder setId(int id) {
            this.id = id;
            return  this;
        }

        public Builder setItemType(int itemType) {
            this.itemType = itemType;
            return  this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return  this;
        }

        public Builder setText(String text) {
            this.text = text;
            return  this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return  this;
        }

        public Builder setCheckedChangeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener) {
            this.checkedChangeListener = checkedChangeListener;
            return  this;
        }

        public Builder setDelegate(TravelDelegate delegate) {
            this.delegate = delegate;
            return  this;
        }
        public Builder setListTag(ListTag listTag) {
            this.listTag = listTag;
            return  this;
        }
        public ListBean build(){
            return new ListBean(itemType,imageUrl,text,value,id,listTag,delegate,checkedChangeListener);
        }
    }
}

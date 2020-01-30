package com.example.travelapp.ec.main.sort.content;

import com.chad.library.adapter.base.entity.SectionEntity;

public class SectionBean extends SectionEntity<SectionContentItemBean> {

    private boolean mIsMore = false;
    private int mId = -1;

    public SectionBean(SectionContentItemBean sectionContentItemBean) {
        super(sectionContentItemBean);
    }

    public SectionBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public boolean isMore() {
        return mIsMore;
    }

    public void setIsMore(boolean isMore) {
        this.mIsMore = isMore;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}

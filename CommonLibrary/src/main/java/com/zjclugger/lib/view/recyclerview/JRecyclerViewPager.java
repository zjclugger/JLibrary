package com.zjclugger.lib.view.recyclerview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @title <br>
 * Created by King.Zi on 2020/7/16.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class JRecyclerViewPager implements Parcelable {
    int pageTotal;  //总页数
    int pageIndex = 1;  //当前页码
    int pageSize = 30;   //每页大小
    int recordTotal;    //记录总数
    int firstPageIndex = 1; //第1页的页码
    boolean mIsLastPageIndex;

    public JRecyclerViewPager() {
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }

    public int getFirstPageIndex() {
        return firstPageIndex;
    }

    public void setFirstPageIndex(int firstPageIndex) {
        this.firstPageIndex = firstPageIndex;
        pageIndex = firstPageIndex;
    }

    public boolean hasMore() {
        return pageTotal > 1 && (firstPageIndex == 0 ? pageIndex + 1 < pageTotal :
                pageIndex < pageTotal);
    }

    public boolean isFirstPage() {
        return pageIndex == firstPageIndex;
    }

    public int nextPageIndex() {
        return pageIndex + 1;
    }

    public int previousPageIndex() {
        return pageIndex - 1 < 0 ? 0 : pageIndex - 1;
    }

    public void setPagerParameter(int pageTotal, int pageIndex, int recordTotal) {
        this.pageTotal = pageTotal;
        this.pageIndex = pageIndex;
        this.recordTotal = recordTotal;
    }

    public String getPagerParameters() {
        return "page total is " + pageTotal + ",page current is " + pageIndex + ",record total is" +
                " " + recordTotal;
    }

    protected JRecyclerViewPager(Parcel in) {
        pageTotal = in.readInt();
        pageIndex = in.readInt();
        pageSize = in.readInt();
        recordTotal = in.readInt();
        firstPageIndex = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pageTotal);
        dest.writeInt(pageIndex);
        dest.writeInt(pageSize);
        dest.writeInt(recordTotal);
        dest.writeInt(firstPageIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JRecyclerViewPager> CREATOR = new Creator<JRecyclerViewPager>() {
        @Override
        public JRecyclerViewPager createFromParcel(Parcel in) {
            return new JRecyclerViewPager(in);
        }

        @Override
        public JRecyclerViewPager[] newArray(int size) {
            return new JRecyclerViewPager[size];
        }
    };
}

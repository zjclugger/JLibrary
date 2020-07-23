package com.zjclugger.buyer.webapi.response;

import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.annotations.SerializedName;
import com.zjclugger.lib.api.entity.BaseEntity;

/**
 * 性别【这里只是演示选项选择器如何使用】<br>
 * Created by King.Zi on 2020/7/23.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class SexCategoryResult extends BaseEntity implements IPickerViewData {
    @SerializedName("name")
    private String name;
    @SerializedName("Id")
    private int id;
    @SerializedName("description")
    private String description;

    private boolean isSelected;

    public SexCategoryResult() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int categoryId) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}

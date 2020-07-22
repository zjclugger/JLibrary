package com.zjclugger.demos;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zjclugger.buyer.R;
import com.zjclugger.lib.entity.JListItem;
import com.zjclugger.lib.ui.adapter.JBaseQuickAdapter;
import com.zjclugger.lib.utils.ViewUtils;
import com.zjclugger.lib.view.recyclerview.JBaseViewHolder;

import java.util.List;

/**
 * 示例列表<br>
 * Created by King.Zi on 2020/7/22.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class DemoItemAdapter extends JBaseQuickAdapter<JListItem<String>, JBaseViewHolder> {
    public DemoItemAdapter(int layoutResId, List<JListItem<String>> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull JBaseViewHolder holder, JListItem<String> result) {
        holder.setText(R.id.label_text, String.valueOf(result.getId()));
        TextView view = holder.getView(R.id.label_value);
        int padding = ViewUtils.dp2px(mContext, 10);
        view.setPadding(padding, padding, padding, padding);
        view.setGravity(Gravity.LEFT);
        view.setText(result.getText());
        ImageView rightView = holder.getView(R.id.label_image_right);
        rightView.setVisibility(View.VISIBLE);
        rightView.setImageResource(R.mipmap.ic_arrow_right);
    }
}

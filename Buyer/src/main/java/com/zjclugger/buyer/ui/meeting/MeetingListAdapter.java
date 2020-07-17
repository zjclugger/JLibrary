package com.zjclugger.buyer.ui.meeting;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.zjclugger.buyer.R;
import com.zjclugger.lib.ui.adapter.JBaseQuickAdapter;
import com.zjclugger.lib.utils.CommonUtils;
import com.zjclugger.lib.utils.DateTimeUtils;
import com.zjclugger.lib.utils.ViewUtils;
import com.zjclugger.lib.utils.WidgetUtils;
import com.zjclugger.lib.view.recyclerview.JBaseViewHolder;

import java.util.List;

/**
 * 会议列表<br>
 * Created by King.Zi on 2020/7/3.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class MeetingListAdapter extends JBaseQuickAdapter<ReminderMeeting, JBaseViewHolder> {

    public MeetingListAdapter(int layoutResId, List<ReminderMeeting> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull JBaseViewHolder holder, ReminderMeeting item) {
       /* holder.setTextView(R.id.meeting_name, item.getTitle());
        holder.setTextView(R.id.meeting_number, item.getConferenceNumber());
        holder.setTextView(R.id.meeting_detail_info, item.getDetails());
        holder.setTextView(R.id.meeting_start_time, DateTimeUtils.stampToDate(item.getStartTime()
                , DateTimeUtils.DATE_FORMAT_YMDHM));
        holder.setTextView(R.id.meeting_end_time, DateTimeUtils.stampToDate(item.getEndTime(),
                DateTimeUtils.DATE_FORMAT_YMDHM));

        CommonUtils.setOnMultipleHitsListener(holder.getView(R.id.meeting_number_layout), 2, 1,
                () -> {
                    ClipboardManager cm =
                            (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("MeetingNumber",
                            item.getConferenceNumber());
                    cm.setPrimaryClip(mClipData);
                    WidgetUtils.toast(mContext, "会议室号已复制到剪贴板!");
                });

        ImageView viewButton = holder.getView(R.id.meeting_view);
        viewButton.setOnClickListener(v -> {
            if (ViewUtils.isFastClick()) {
                setOnItemClickListener(viewButton, holder.getAdapterPosition());
            }
        });

        Button joinButton = holder.getView(R.id.meeting_action);
        if (System.currentTimeMillis() > item.getEndTime()) {
            joinButton.setText("会议已结束");
            ViewUtils.setReadOnly(true, joinButton);
        } else {
            joinButton.setText("加入会议");
            ViewUtils.setReadOnly(false, joinButton);
            joinButton.setOnClickListener(v -> {
                if (ViewUtils.isFastClick()) {
                    setOnItemClickListener(joinButton, holder.getAdapterPosition());
                }
            });
        }*/
    }
}
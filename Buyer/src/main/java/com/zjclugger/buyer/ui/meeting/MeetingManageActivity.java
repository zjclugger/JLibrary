package com.zjclugger.buyer.ui.meeting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.jview.DateTimePickerView;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.utils.DateTimeFormat;
import com.zjclugger.buyer.R;
import com.zjclugger.lib.base.BaseActivity;
import com.zjclugger.lib.utils.CommonUtils;
import com.zjclugger.lib.utils.Constants;
import com.zjclugger.lib.utils.DateTimeUtils;
import com.zjclugger.lib.utils.ViewUtils;
import com.zjclugger.lib.utils.WidgetUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 预约会议管理 <br>
 * Created by King.Zi on 2020/7/3.<br>
 */
public class MeetingManageActivity extends BaseActivity {
/*
    private static final String TAG = "MeetingManage";
    @BindView(R.id.edit_meeting_name)
    EditText mMeetingNameEditView;
    @BindView(R.id.edit_meeting_password)
    EditText mMeetingPasswordEditView;
    @BindView(R.id.edit_meeting_ctl_password)
    EditText mMeetingCtlPasswordEditView;
    @BindView(R.id.edit_start_time)
    EditText mMeetingStartTimeEditView;
    @BindView(R.id.edit_end_time)
    EditText mMeetingEndTimeEditView;
    @BindView(R.id.edit_meeting_detail)
    EditText mMeetingDetailEditView;
    @BindView(R.id.chk_auto_record)
    CheckBox mAutoRecordView;
    @BindView(R.id.meeting_delete)
    Button mMeetingDeleteView;
    @BindView(R.id.action_layout)
    LinearLayout mActionLayout;

    DateTimePickerView mStartDateDialog;
    private long mStartInitDateTime;
    DateTimePickerView mEndDateDialog;
    private long mEndInitDateTime;
    private long mCurrentTime;

    private ReminderMeeting mReminderMeeting;
    private boolean mIsUpdate;    //是否为更新
    private ReminderMeeting mReminderMeetingCache;
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_manage);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected boolean isDetailActivity() {
        return true;
    }

    private void initView() {
        mCurrentTime = System.currentTimeMillis();
        if (getIntent().hasExtra(Constants.Keywords.KEY_MEETING_DETAIL)) {
            mReminderMeeting =
                    (ReminderMeeting) getIntent().getSerializableExtra(Constants.Keywords
                    .KEY_MEETING_DETAIL);
            mMeetingNameEditView.setText(mReminderMeeting.getTitle());
            mStartInitDateTime = mReminderMeeting.getStartTime();
            mEndInitDateTime = mReminderMeeting.getEndTime();
            mMeetingPasswordEditView.setText(mReminderMeeting.getPassword());
            mMeetingCtlPasswordEditView.setText(mReminderMeeting.getConferenceControlPassword());
            mAutoRecordView.setChecked(mReminderMeeting.getAutoRecord() == 1);
            mMeetingDetailEditView.setText(mReminderMeeting.getDetails());

            if (!TextUtils.isEmpty(mReminderMeeting.getId()) && mCurrentTime > mReminderMeeting
            .getEndTime()) {
                //查看
                ViewUtils.setReadOnly(mMeetingNameEditView, mMeetingPasswordEditView,
                        mMeetingCtlPasswordEditView, mMeetingStartTimeEditView,
                        mMeetingEndTimeEditView, mAutoRecordView, mMeetingDetailEditView);
                mTitleRightView.setVisibility(View.GONE);
                mIsUpdate = false;
            } else {
                //可修改
                mIsUpdate = true;
            }
            mTitleView.setText("云会议详情");
        } else {
            mTitleView.setText("预约云会议");
            mStartInitDateTime = mCurrentTime + 5 * 60 * 1000;
            mEndInitDateTime = mStartInitDateTime + 30 * 60 * 1000l;
            //new meeting
            mReminderMeeting = new ReminderMeeting();
            mReminderMeeting.setMeetingRoomType(1);
        }

        mReminderMeetingCache = CommonUtils.copyObject(mReminderMeeting);
        mTitleRightView.setText(mIsUpdate ? "修改" : "预约");

        mTitleRightView.setOnClickListener(v -> {
            CommonUtils.hideSoftInput(MeetingManageActivity.this);
            save();
        });

        //delete view,开始前+结束后
        if (!TextUtils.isEmpty(mReminderMeeting.getId())) {
            if (mCurrentTime < mReminderMeeting.getStartTime()) {
                //会议未开始
                ViewUtils.setVisibility(true, mActionLayout);
            } else {
                if (mCurrentTime > mReminderMeeting.getEndTime()) {
                    //会议已结束
                    ViewUtils.setVisibility(true, mActionLayout);
                }
            }
        }

        mAlertDialog = new AlertDialog.Builder(mContext)
                .setTitle("您确认要删除这个会议吗？")
                .setNeutralButton(R.string.text_cancel, (dialog, which) -> mAlertDialog.dismiss())
                .setPositiveButton(R.string.text_confirm, (dialog, which) -> {
                    mAlertDialog.dismiss();
                    deleteMeeting(mReminderMeeting.getId());
                }).create();

        mMeetingDeleteView.setOnClickListener(v -> {
            if (null != mAlertDialog) {
                mAlertDialog.show();
            }
        });

        ViewUtils.setEditViewReadOnly(mMeetingStartTimeEditView, mMeetingEndTimeEditView);
        //start time
        mMeetingStartTimeEditView.setOnClickListener(v -> {
            CommonUtils.hideSoftInput(MeetingManageActivity.this);
            if (mStartDateDialog == null) {
                mStartDateDialog = new DateTimePickerView(mContext,
                        initOnTimeSelectListener(mMeetingStartTimeEditView), DateTimeFormat.YMDHM);
            }
            mStartDateDialog.getBuilder()
                    .setSubmitText("确认")
                    .setCancelText("取消");
            mStartDateDialog.setInitDateTime(mMeetingStartTimeEditView.getText().toString(),
                    DateTimeUtils.DATE_FORMAT_YMDHM);
            mStartDateDialog.show();
        });
        mMeetingStartTimeEditView.setText(DateTimeUtils.stampToDate(mStartInitDateTime,
                DateTimeUtils.DATE_FORMAT_YMDHM));

        //end time
        mMeetingEndTimeEditView.setOnClickListener(v -> {
            CommonUtils.hideSoftInput(MeetingManageActivity.this);
            if (mEndDateDialog == null) {
                mEndDateDialog = new DateTimePickerView(mContext,
                        initOnTimeSelectListener(mMeetingEndTimeEditView), DateTimeFormat.YMDHM);
            }
            mEndDateDialog.getBuilder()
                    .setSubmitText("确认")
                    .setCancelText("取消");
            mEndDateDialog.setInitDateTime(mMeetingEndTimeEditView.getText().toString(),
                    DateTimeUtils.DATE_FORMAT_YMDHM);
            mEndDateDialog.show();
        });
        mMeetingEndTimeEditView.setText(DateTimeUtils.stampToDate(mEndInitDateTime,
                DateTimeUtils.DATE_FORMAT_YMDHM));

        mAutoRecordView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mReminderMeeting.setAutoRecord(isChecked ? 1 : 0);
        });
    }

    private OnTimeSelectListener initOnTimeSelectListener(final EditText dateTimeView) {
        return (date, v) -> dateTimeView.setText(DateTimeUtils.getDateTime(date,
                DateTimeUtils.DATE_FORMAT_YMDHM));
    }

    private void save() {
        if (!TextUtils.isEmpty(mMeetingNameEditView.getText())) {
            mReminderMeeting.setTitle(mMeetingNameEditView.getText().toString());
        } else {
            mMeetingNameEditView.requestFocus();
            WidgetUtils.toast(mContext, "请输入会议名称", false);
            return;
        }

        if (!TextUtils.isEmpty(mMeetingPasswordEditView.getText())) {
            mReminderMeeting.setPassword(mMeetingPasswordEditView.getText().toString());
        }

        if (!TextUtils.isEmpty(mMeetingCtlPasswordEditView.getText())) {
            mReminderMeeting.setConferenceControlPassword(mMeetingCtlPasswordEditView.getText()
            .toString());
        }

        mReminderMeeting.setDetails(mMeetingDetailEditView.getText().toString());

        //时间判断
        long currentTime = System.currentTimeMillis();
        long startTime = getInputDateTime(mMeetingStartTimeEditView.getText().toString());
        long endTime = getInputDateTime(mMeetingEndTimeEditView.getText().toString());
        boolean changeAllTime;  //更新开始和结束时间

        if (mIsUpdate) {
            if (currentTime >= mReminderMeetingCache.getStartTime()) {
                changeAllTime = false;
                if (startTime != mReminderMeetingCache.getStartTime()) {
                    mMeetingStartTimeEditView.setText(DateTimeUtils.stampToDate
                    (mReminderMeetingCache.getStartTime(),
                            DateTimeUtils.DATE_FORMAT_YMDHM));
                    WidgetUtils.toast(mContext, "会议已开始，不能修改开始时间", false);
                    return;
                }

                //已经开始，只能修改结束时间
                if (currentTime >= mReminderMeetingCache.getEndTime()) {
                    //已结束，不可修改
                    WidgetUtils.toast(mContext, "会议已结束，不能修改结束时间", false);
                    return;
                } else {
                    //修改结束时间
                    if (endTime > mCurrentTime) {
                        if (endTime > mReminderMeeting.getStartTime()) {
                            mReminderMeeting.setEndTime(endTime);
                        } else {
                            WidgetUtils.toast(mContext, "结束时间必须大于开始时间", false);
                            return;
                        }
                    } else {
                        WidgetUtils.toast(mContext, "结束时间必须大于当前时间", false);
                        return;
                    }
                }
            } else {
                changeAllTime = true;
            }
        } else {
            changeAllTime = true;
        }

        if (changeAllTime) {
            //new
            if (startTime >= (currentTime + 1 * 60 * 1000)) {
                mReminderMeeting.setStartTime(startTime);
            } else {
                WidgetUtils.toast(mContext, "开始时间必须大于当前时间，且至少1分钟", false);
                return;
            }

            if (endTime > mReminderMeeting.getStartTime()) {
                mReminderMeeting.setEndTime(endTime);
            } else {
                WidgetUtils.toast(mContext, "结束时间必须大于开始时间", false);
                return;
            }
        }

        if (!CommonUtils.object2Json(mReminderMeeting).equalsIgnoreCase(CommonUtils.object2Json
        (mReminderMeetingCache))) {
            reminderMeeting();
            Log.d(TAG, "start time " + DateTimeUtils.stampToDate(mReminderMeeting.getStartTime(),
                    DateTimeUtils.DATE_FORMAT_YMDHMS)
                    + ",end time=" + DateTimeUtils.stampToDate(mReminderMeeting.getEndTime(),
                    DateTimeUtils.DATE_FORMAT_YMDHMS));
        } else {
            finish();
        }
    }

    private long getInputDateTime(String date) {
        long dateTime = DateTimeUtils.dateToStamp(date + ":01", DateTimeUtils.DATE_FORMAT_YMDHMS);
        String dateString = DateTimeUtils.stampToDate(dateTime, DateTimeUtils.DATE_FORMAT_YMDHMS);
        Log.d(TAG, "convert date time is " + dateString);
        return dateTime;
    }

    private void reminderMeeting() {
        showWaiting();
        Observable<String> observable = Observable.create(emitter -> {
            Result result = mIsUpdate ? ReminderMeetingApi.updateMeeting(mReminderMeeting.getId(),
                    mReminderMeeting) : ReminderMeetingApi.remindMeeting(mReminderMeeting);

            if (result == null || !result.isSuccess()) {
                Log.d(TAG, mIsUpdate ? "修改失败," : "预约会议室失败," + (result == null ? "result is null" :
                        result.getErrorMsg()));
                emitter.onError(new Exception(mIsUpdate ? "修改失败" : "预约会议室失败"));
            } else {
                if (!mIsUpdate) {
                    Map data = (Map) result.getData();
                    String meetingId = data.get(MeetingUtils.ParameterNames.MEETING_ID).toString();
                    String meetingRoomNumber =
                            data.get(MeetingUtils.ParameterNames.MEETING_ROOM_NUMBER).toString();
                    mReminderMeeting.setId(meetingId);
                    mReminderMeeting.setConferenceNumber(meetingRoomNumber);
                    Log.d(TAG,
                            "reminder meeting is successful, the meeting id is " + meetingId + "," +
                                    "meeting room number is " + meetingRoomNumber);
                } else {
                    Log.d(TAG,
                            "modify reminder meeting is successful");
                }
                emitter.onComplete();
            }
        });
        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String value) {
            }

            @Override
            public void onError(Throwable e) {
                closeProgressDialog();
                WidgetUtils.toastErrorMessage(mContext, e.getMessage());
            }

            @Override
            public void onComplete() {
                closeProgressDialog();
                WidgetUtils.toast(mContext, mIsUpdate ? "更新成功" : "添加成功");
                finishWithResult(true, mReminderMeeting);
                Log.d(TAG, mIsUpdate ? "更新成功" : "添加成功");
            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }

    private void deleteMeeting(String meetingId) {
        showWaiting();
        Observable<String> observable = Observable.create(emitter -> {
            Result<String> result = ReminderMeetingApi.deleteMeeting(meetingId);
            if (result == null || !result.isSuccess()) {
                emitter.onError(new Exception("删除会议失败" + (result == null ? "result is null" :
                        result.getErrorMsg())));
            } else {
                emitter.onComplete();
            }
        });
        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String value) {
            }

            @Override
            public void onError(Throwable e) {
                closeProgressDialog();
                Log.e(TAG, "error: " + e.getMessage());
                WidgetUtils.toastErrorMessage(mContext, e.getMessage());
            }

            @Override
            public void onComplete() {
                closeProgressDialog();
                WidgetUtils.toast(mContext, "删除成功");
                finishWithResult(true, null);
                Log.d(TAG, "删除成功");
            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }

    //todo: 根据业务逻辑，调用此方法，返回修改后的对象
    private void finishWithResult(boolean isChanged, ReminderMeeting reminderMeeting) {
        Intent intent = getIntent();
        intent.putExtra(MeetingUtils.KeyWord.IS_CHANGED, isChanged);
        intent.putExtra(MeetingUtils.KeyWord.ITEM_DATA, reminderMeeting);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
*/
}
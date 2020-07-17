package com.zjclugger.buyer.ui.meeting;

import com.zjclugger.lib.base.BaseActivity;

/**
 * 会议列表<br>
 * Created by King.Zi on 2020/6/11.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class MeetingActivity extends BaseActivity {
/*
    private static final String TAG = "MeetingList";
    private static final long WEEK_DATE_TIME = 7 * 24 * 60 * 60 * 1000l;
    @BindView(R.id.edit_query_start_time)
    EditText mQueryStartTimeEditView;
    @BindView(R.id.meetings_recycler_view)
    ExtendsRecyclerView mRecyclerView;
    JRecyclerViewPager mRecyclerViewPager;
    MeetingListAdapter mMeetingAdapter;
    List<ReminderMeeting> mReminderMeetingList = new ArrayList<>();

    //datetime
    DateTimePickerView mStartDateDialog;

    private boolean mIsTurnPage;    //是否为翻页查询

    //for query
    boolean mIsSuccess = false;
    boolean mIsHasChange = false;
    boolean mNeedRefresh = false;
    private ReminderMeeting mTempReminderMeeting;
    private SharedPreferencesUtils mPreferencesUtils;
    private int mPosition;  //被点击的item position

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.bind(this);

        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(result -> {
                    if (result) {
                        mPreferencesUtils = new SharedPreferencesUtils(mContext);
                        initView();
                    } else {
                        Log.e(TAG, "request permission is failed!");
                        finish();
                    }
                });
    }

    @Override
    protected boolean isDetailActivity() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "meeting resume need refresh " + mNeedRefresh + ",position " + mPosition);
        if (mNeedRefresh) {
            if (null == mTempReminderMeeting) {
                //delete
                mMeetingAdapter.remove(mPosition);
            } else {
                if (ExtendsRecyclerView.NEW_MEETING_POSITION == mPosition) {
                    mMeetingAdapter.addData(mTempReminderMeeting);
                    //add
                } else {
                    //update
                    mMeetingAdapter.setData(mPosition, mTempReminderMeeting);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mNeedRefresh = false;
        mTempReminderMeeting = null;
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            mNeedRefresh = data.getBooleanExtra(MeetingUtils.KeyWord.IS_CHANGED, false);
            mTempReminderMeeting =
                    (ReminderMeeting) data.getSerializableExtra(MeetingUtils.KeyWord.ITEM_DATA);
        }
    }

    private void initView() {
        mTitleView.setText("我的云会议");
        ViewUtils.setEditViewReadOnly(mQueryStartTimeEditView);
        mQueryStartTimeEditView.setOnClickListener(v -> {
            if (mStartDateDialog == null) {
                mStartDateDialog = new DateTimePickerView(mContext,
                        initOnTimeSelectListener(mQueryStartTimeEditView), DateTimeFormat.YMDHM);
            }
            mStartDateDialog.getBuilder()
                    .setSubmitText("确认")
                    .setCancelText("取消");
            mStartDateDialog.setInitDateTime(mQueryStartTimeEditView.getText().toString(),
                    DateTimeUtils.DATE_FORMAT_YMDHM);
            mStartDateDialog.show();
        });

        mQueryStartTimeEditView.setText(DateTimeUtils.stampToDate(System.currentTimeMillis() - WEEK_DATE_TIME, DateTimeUtils.DATE_FORMAT_YMDHM));

        mTitleRightView.setText("预约会议");
        mTitleRightView.setOnClickListener(v -> {
            //打开新建会议室界面
            mPosition = ExtendsRecyclerView.NEW_MEETING_POSITION;
            startActivityForResult(new Intent(this, MeetingManageActivity.class), 0);
        });

        //TODO: 1.初始化适配器
        //list adapter
        mMeetingAdapter = new MeetingListAdapter(R.layout.item_meeting_list, mReminderMeetingList);
        mMeetingAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "the position is" + position);
            mPosition = position;
            if (R.id.meeting_action == view.getId()) {
                MeetingParameter parameter = new MeetingParameter();
                parameter.setUserName((String) mPreferencesUtils.get("userName", "未知"));
                parameter.setUserId((String) mPreferencesUtils.get("userId", "JC001"));
                parameter.setPassword(mReminderMeetingList.get(position).getPassword());
                parameter.setMeetingNumber(mReminderMeetingList.get(position).getConferenceNumber());
                joinMeeting(parameter);
            } else if (R.id.meeting_view == view.getId()) {
                Intent intent = new Intent(mContext, MeetingManageActivity.class);
                intent.putExtra(Constants.Keywords.KEY_MEETING_DETAIL,
                        mReminderMeetingList.get(position));
                startActivityForResult(intent, 0);
            }
        });

        //TODO: 2.初始化分页对象、以及Recycler View
        //list view
        mRecyclerViewPager = mRecyclerView.getPager();
        mRecyclerViewPager.setFirstPageIndex(0);
        mRecyclerView.setLayoutParameter(true, 3, true);
        //TODO: 3.设置下拉刷新，上拉加载更多的监听
        mRecyclerView.setRefreshLayoutListener(refreshLayout -> {
            Log.d(TAG, mRecyclerViewPager.getPagerParameters());
            //上滑底部加载更多
            if (mRecyclerViewPager.hasMore()) {
                getReminderMeetingList(true, mRecyclerViewPager.nextPageIndex());
            } else {
                WidgetUtils.toast(mContext, getString(R.string.info_last_page));
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }).create(mMeetingAdapter, errorView -> getReminderMeetingList(mIsTurnPage,
                mRecyclerViewPager.getPageIndex()));
        //TODO: 4.获取数据
        getReminderMeetingList(false, mRecyclerViewPager.getFirstPageIndex());
    }

    private OnTimeSelectListener initOnTimeSelectListener(final EditText dateTimeView) {
        return (date, v) -> {
            dateTimeView.setText(DateTimeUtils.getDateTime(date, DateTimeUtils.DATE_FORMAT_YMDHM));
            getReminderMeetingList(false, mRecyclerViewPager.getFirstPageIndex());
        };
    }

    //TODO:获取数据
    private void getReminderMeetingList(boolean isTurnPage, int pageIndex) {
        mIsTurnPage = isTurnPage;
        showWaiting();
        mIsHasChange = false;
        mIsSuccess = false;

        //TODO: 5.非翻页查询，先清空数据
        if (!mIsTurnPage) {
            mRecyclerView.getRefreshLayout().resetNoMoreData();
            //非分页查询先清空数据
            if (mReminderMeetingList != null) {
                mReminderMeetingList.clear();
            }
            mIsHasChange = true;
        }

        Observable<String> observable = Observable.create(emitter -> {
            long queryTime =
                    DateTimeUtils.dateToStamp(mQueryStartTimeEditView.getText().toString(),
                            DateTimeUtils.DATE_FORMAT_YMDHM);

            Result<String> result = ReminderMeetingApi.getReminderMeetingList(pageIndex,
                    mRecyclerViewPager.getPageSize(), queryTime);
            if (result == null || !result.isSuccess()) {
                emitter.onError(new Exception("获取预约会议列表失败" + (result == null ? "result is null" :
                        result.getErrorMsg())));
            } else {
                Pager<ReminderMeeting> pagerData = CommonUtils.json2Object(result.getData(),
                        new TypeToken<Pager<ReminderMeeting>>() {
                        });
                //TODO: 6.记录分页信息
                mRecyclerViewPager.setPagerParameter(pagerData.getPageTotal(),
                        pagerData.getPage(), pagerData.getRowsTotal());

                //TODO: 7.根据获取数据的结果，完成相应的业务逻辑
                if (null != pagerData.getList() && pagerData.getList().size() > 0) {
                    if (mRecyclerViewPager.isFirstPage()) {
                        //第1页，可能是条件过滤查询
                        if (null != mReminderMeetingList) {
                            mReminderMeetingList.clear();
                        }
                    }

                    mReminderMeetingList.addAll(pagerData.getList());
                    mIsHasChange = true;
                } else {
                    if (mRecyclerViewPager.getPageIndex() != mRecyclerViewPager.getFirstPageIndex()
                            || (pagerData.getRowsTotal() != 0 && pagerData.getRowsTotal() <= mRecyclerViewPager.getPageSize())) {
                        emitter.onNext(getString(R.string.info_last_page));
                    }
                }

                Log.d(TAG, mRecyclerViewPager.getPagerParameters()
                        + ",list size is " + mReminderMeetingList.size());

                mIsSuccess = true;
                emitter.onComplete();
            }
        });
        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String value) {
                WidgetUtils.toast(mContext, value);
            }

            @Override
            public void onError(Throwable e) {
                closeProgressDialog();
                Log.e(TAG, "error: " + e.getMessage());
                WidgetUtils.toastErrorMessage(mContext, e.getMessage());
            }

            @Override
            public void onComplete() {
                //TODO: 8.UI线程中刷新
                mRecyclerView.refreshList(mIsSuccess, false, mIsHasChange);
                closeProgressDialog();
                Log.d(TAG, "get data is successful");
            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }

    private void joinMeeting(MeetingParameter parameter) {
        showWaiting();
        NemoSDK.getInstance().loginExternalAccount(parameter.getUserName(),
                parameter.getUserId(),
                new ConnectNemoCallback() {
                    @Override
                    public void onFailed(final int errorCode) {
                        closeProgressDialog();
                        Log.e(TAG, "Anonymous login failed, the error code is " + errorCode);
                        WidgetUtils.toastErrorMessage(mContext, "云会议SDK登录失败，错误码：" + errorCode);
                    }

                    @Override
                    public void onSuccess(LoginResponseData data,
                                          boolean isDetectingNetworkTopology) {
                        Log.e(TAG, "login success data" + data);
                        //mack call
                        NemoSDK.getInstance().makeCall(parameter.getMeetingNumber(),
                                parameter.getPassword(),
                                new MakeCallResponse() {
                                    @Override
                                    public void onCallSuccess() {
                                        closeProgressDialog();
                                        // 查询号码成功, 进入通话界面
                                        Intent callIntent = new Intent(mContext,
                                                XyCallActivity.class);
                                        callIntent.putExtra("number",
                                                parameter.getMeetingNumber());
                                        // 如果需要初始化默认这是关闭摄像头或者麦克风, 将callPresenter.start()
                                        // 移至XyCallActivity#onCreate()下
                                        callIntent.putExtra("muteAudio", false);
                                        callIntent.putExtra("muteVideo", false);
                                        mContext.startActivity(callIntent);
                                    }

                                    @Override
                                    public void onCallFail(int error, String msg) {
                                        closeProgressDialog();
                                        Observable.just(0).observeOn(AndroidSchedulers
                                                .mainThread()).subscribe(integer -> {
                                            Log.e(TAG,
                                                    "call failed the error code is " + error + "," +
                                                            "the mssage is " + msg);
                                            WidgetUtils.toastErrorMessage(mContext,
                                                    "操作失败：" + msg);
                                        });
                                    }
                                });
                    }

                    @Override
                    public void onNetworkTopologyDetectionFinished(LoginResponseData resp) {
                        Log.i(TAG, "net detect onNetworkTopologyDetectionFinished 1");
                    }
                });
        // query record permission: 如果要使用录制功能 请务必调此接口
        if (parameter.isNeedRecording()) {
            NemoSDK.getInstance().getRecordingUri(parameter.getMeetingNumber());
        }
    }
*/
}

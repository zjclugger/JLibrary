package com.zjclugger.buyer.ui.uc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.jview.OptionsPickerView;
import com.zjclugger.buyer.R;
import com.zjclugger.buyer.databinding.FragmentUserInfoManagerBinding;
import com.zjclugger.buyer.utils.BuyerConstants;
import com.zjclugger.buyer.webapi.response.SexCategoryResult;
import com.zjclugger.buyer.webapi.response.UserInfoResult;
import com.zjclugger.lib.base.BaseFragment;
import com.zjclugger.lib.entity.JMediaItem;
import com.zjclugger.lib.ui.widget.JAlertDialog;
import com.zjclugger.lib.utils.CommonUtils;
import com.zjclugger.lib.utils.FragmentUtils;
import com.zjclugger.lib.utils.SoftKeyboardUtils;
import com.zjclugger.lib.utils.ViewUtils;
import com.zjclugger.router.ARouterConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息<br>
 * Created by King.Zi on 2020/7/23.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class UserInfoFragment extends BaseFragment {
    public static final String TAG = "UserInfo";
    private UserInfoResult mUserInfoResult;
    private UserInfoResult mUserInfoResultCache;
    private FragmentUserInfoManagerBinding mBinding;
    private TextView mTitleView;
    private TextView mTitleRightView;
    private BaseFragment mSelectedMediaFragment;
    private ArrayList<JMediaItem> mUserImageList = new ArrayList<>();

    private JAlertDialog mAlertDialog;
    private OptionsPickerView mSexPickerView;
    private List<SexCategoryResult> mSexCategoryList = new ArrayList<>();
    private boolean mIsReadOnly = false;

    @Override
    public boolean isPublicPermission() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_manager, container,
                false);
        mView = mBinding.getRoot();
        if (mArguments != null && mArguments.containsKey(BuyerConstants.Keywords.KEY_USER_INFO)) {
            mUserInfoResult = mArguments.getParcelable(BuyerConstants.Keywords.KEY_USER_INFO);
        }

        if (mUserInfoResult != null) {
            mIsReadOnly = mArguments.getBoolean(BuyerConstants.Keywords.KEY_USER_INFO_READ_ONLY
                    , false);
            mUserInfoResultCache = CommonUtils.copyObject(mUserInfoResult);
            initView();
            bindData();
        } else {
            close();
        }
        return mView;
    }

    private void initView() {
        //title
        mBinding.navTitleLayout.findViewById(R.id.iv_title_back).setOnClickListener(v -> close());

        mTitleView = mBinding.navTitleLayout.findViewById(R.id.tv_title_text);
        mTitleView.setText("个人信息");
        mTitleRightView = mBinding.navTitleLayout.findViewById(R.id.tv_right_text);
        if (TextUtils.isEmpty(mUserInfoResult.getId())) {
            mTitleRightView.setText("保存");
        } else {
            mTitleRightView.setText(R.string.complete);
        }

        mTitleRightView.setOnClickListener(v -> {
            String cacheValue = CommonUtils.object2Json(mUserInfoResultCache);
            String currentValue = CommonUtils.object2Json(mUserInfoResult);

            if (!cacheValue.equalsIgnoreCase(currentValue)) {
                updateOriginal(currentValue);
            } else {
                close();
            }
            //todo:获取要上传的图片
            /*if (mSelectedMediaFragment != null) {
                Bundle data = mSelectedMediaFragment.getArguments();
                mUserImageList =
                        data.getParcelableArrayList(Constants.Keywords.KEY_SELECTED_PICTURE);
                Log.d("kz", "the result media list is " + (mUserImageList == null ? "0" :
                        mUserImageList.size()));
            } else {
                Log.d("kz", "mSelectedMediaFragment is null");
            }*/
        });

        mAlertDialog = new JAlertDialog(mContext);

        //mBinding.letvGoodsName.getEditTextView()
        ViewUtils.setReadOnly(mBinding.elvvUserSex.getValueView());
        mSexPickerView = new OptionsPickerView(mContext, (options1, options2, options3, v) -> {
            if (!mSexCategoryList.get(options1).getName().equalsIgnoreCase(mUserInfoResultCache.getSexName())) {
                mUserInfoResult.setSexName(mSexCategoryList.get(options1).getName());
            }

            mBinding.elvvUserSex.setValueText(mSexCategoryList.get(options1).getName());
        });
        mSexPickerView.displayClassicLayout("性别选择", null, getString(R.string.text_confirm));

        //mBinding.elvvGoodsCategory.getLeftImageView().setImageResource(R.);
        mBinding.elvvUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsReadOnly) {
                    mSexPickerView.show();
                }
            }
        });

        mBinding.elvvUserSex.setEditTextReadOnly();

        if (mIsReadOnly) {
            mBinding.letvUserName.setEditTextReadOnly();
            mBinding.letvUserPhone.setEditTextReadOnly();
            mBinding.letvAddress.setEditTextReadOnly();
            mTitleRightView.setVisibility(View.INVISIBLE);
        }

        // image
        initImageList();
        Bundle selectedBundle = new Bundle();
        selectedBundle.putParcelableArrayList(ARouterConstants.KeyWord.KEY_MEDIA_SELECTED_LIST,
                mUserImageList);
        mSelectedMediaFragment =
                (BaseFragment) ARouter.getInstance().build(ARouterConstants.Path.COM_SELECT_MEDIA)
                        .withBundle(ARouterConstants.KeyWord.KEY_MEDIA_PARAMETERS, selectedBundle)
                        .withBoolean(ARouterConstants.KeyWord.KEY_MEDIA_IS_PICTURE_SMALL, false)
                        .withBoolean(ARouterConstants.KeyWord.KEY_MEDIA_IS_SINGLE_LAYOUT, false)
                        .withBoolean(ARouterConstants.KeyWord.KEY_MEDIA_IS_READ_ONLY, mIsReadOnly)
                        .withInt(ARouterConstants.KeyWord.KEY_MEDIA_GRID_SPAN_COUNT, 3)
                        .withInt(ARouterConstants.KeyWord.KEY_MEDIA_MAX_NUM, 6)
                        .withInt(ARouterConstants.KeyWord.KEY_MEDIA_BACKGROUND_COLOR,
                                R.color.bg_white)
                        .navigation();

        //在部分低端手机，调用单独拍照时可能产生内存不足的问题，导致activity被回收，所以不重复创建fragment
        FragmentUtils.addFirstFragment(getActivity(), mSelectedMediaFragment, R.id.picture_content);

    }

    private void initImageList() {
        if (null != mUserInfoResult.getImageUrl() && mUserInfoResult.getImageUrl().size() > 0) {
            for (int i = 0; i < mUserInfoResult.getImageUrl().size(); i++) {
                JMediaItem mediaItem = new JMediaItem();
                mediaItem.setMimeType(JMediaItem.TYPE_IMAGE);
                mediaItem.setFilePath(mUserInfoResult.getImageUrl().get(i));
                mUserImageList.add(mediaItem);
            }
        } else {
          /*  ERPMediaItem mediaItem = new ERPMediaItem();
            mediaItem.setMimeType(ERPMediaItem.TYPE_IMAGE);
            mediaItem.setFilePath("http://img.qqzhi.com/uploads/2018-11-29/120313953.jpg");
            mUserImageList.add(mediaItem);

            mediaItem = new ERPMediaItem();
            mediaItem.setMimeType(ERPMediaItem.TYPE_IMAGE);
            mediaItem.setFilePath("http://img.qqzhi.com/uploads/2019-04-13/135309415.jpg");
            mUserImageList.add(mediaItem);

            //http://http://img.qqzhi.com/uploads/2019-04-05/043646862.jpg
            mediaItem = new ERPMediaItem();
            mediaItem.setMimeType(ERPMediaItem.TYPE_IMAGE);
            mediaItem.setFilePath("http://img.qqzhi.com/uploads/2019-04-13/115041745.jpg");
            mUserImageList.add(mediaItem);

            mediaItem = new ERPMediaItem();
            mediaItem.setMimeType(ERPMediaItem.TYPE_IMAGE);
            mediaItem.setFilePath("http://img.qqzhi.com/uploads/2019-04-05/043646862.jpg");
            mUserImageList.add(mediaItem);*/
        }

    }

    private void bindData() {
        mBinding.setUserInfo(mUserInfoResult);
        getSexList();
        if (!TextUtils.isEmpty(mUserInfoResult.getId())) {
            mBinding.letvUserName.setEditTextValue(mUserInfoResult.getName());
            mBinding.letvUserPhone.setEditTextValue(mUserInfoResult.getPhoneNumber());
            mBinding.letvAddress.setEditTextValue(mUserInfoResult.getAddress());
        }
    }

    private void updateOriginal(String originalJson) {
       /* showWaiting();
        mViewModel.updateOriginalBill(originalJson).observe(this,
                new Observer<ApiResponse<BaseWrapper<String>>>() {
                    @Override
                    public void onChanged(ApiResponse<BaseWrapper<String>> response) {
                        boolean success = false;
                        closeProgressDialog();
                        if (response != null && response.isSuccess()) {
                            BaseWrapper<String> result = response.body;
                            if (result != null) {
                                success = true;
                                WidgetUtils.toastMessage(mContext, result.getResult());
                            }
                            Log.D(TAG, "upload original is " + response.isSuccess() + " the " +
                                    "message is " + response.body);
                        }

                        if (!success) {
                            WidgetUtils.toastMessage(mContext, R.string.upload_failed);
                            Log.E(TAG, "upload original is failed the result is " + response);
                        }
                        close();
                    }
                });*/
        close();
    }

    private void getSexList() {
        SexCategoryResult sexCategoryResult=new SexCategoryResult();
        sexCategoryResult.setId(2580);
        sexCategoryResult.setName("男");
        mSexCategoryList.add(sexCategoryResult);

        sexCategoryResult=new SexCategoryResult();
        sexCategoryResult.setId(2581);
        sexCategoryResult.setName("女");
        mSexCategoryList.add(sexCategoryResult);

        mSexPickerView.bindData(mSexCategoryList);
    }

    @Override
    public void closeFloatView() {
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public void initViews() {

    }

    @Override
    public boolean doBackPress() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("KZ", "data=" + data);
    }

}
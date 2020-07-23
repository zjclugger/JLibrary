package com.zjclugger.buyer.ui.uc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zjclugger.buyer.R;
import com.zjclugger.buyer.utils.BuyerConstants;
import com.zjclugger.buyer.webapi.response.UserInfoResult;
import com.zjclugger.lib.base.BaseApplication;
import com.zjclugger.lib.base.BaseFragment;
import com.zjclugger.lib.business.UserManager;
import com.zjclugger.lib.view.CircleImageView;
import com.zjclugger.router.ARouterConstants;
import com.zjclugger.router.utils.ARouterUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 个人中心<br>
 * Created by King.Zi on 2020/4/26.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class UserProfileFragment extends BaseFragment {
    private final static String TAG = "UserProfile";

    @BindView(R.id.user_name)
    TextView mUserNameView;
    @BindView(R.id.citv_user_photo)
    CircleImageView mUserPhotoView;
    @BindView(R.id.user_info_layout)
    LinearLayout mUserInfoLayout;
    @BindView(R.id.user_logout_button)
    Button mLogoutView;

    @BindView(R.id.user_pay_mode)
    TextView mPayModeView;
    @BindView(R.id.user_info)
    TextView mUserInfoView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarStyle(R.color.bg_white, false);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user_profile;
    }

    @Override
    public void initViews() {
        initDetailTitleViews("个人中心", null, null, false);
        mPayModeView.setOnClickListener(v -> ARouterUtils.openFragment(getActivity(),
                PayModeFragment.class.getName(), null, R.color.bg_white, false));


        if (UserManager.getInstance().getCurrentUser() != null) {
            mUserNameView.setText(UserManager.getInstance().getCurrentUser().getUserName());
            //mUserPostView.setText(UserManager.getInstance().getCurrentUser().getUid());
        }

        View.OnClickListener openUserInfoListener = v -> {
            UserInfoResult result = new UserInfoResult();
            result.setName("");

            Bundle params = new Bundle();
            params.putParcelable(BuyerConstants.Keywords.KEY_USER_INFO, result);
            ARouterUtils.openFragment(getActivity(),
                    UserInfoFragment.class.getName(), params, R.color.bg_white, false);
        };

        mUserInfoLayout.setOnClickListener(openUserInfoListener);
        mUserPhotoView.setOnClickListener(openUserInfoListener);

        mUserInfoView.setOnClickListener(v -> {
            List<String> imageList = new ArrayList<>();
            imageList.add("http://img.qqzhi.com/uploads/2019-04-06/103454404.jpg");
            imageList.add("http://img.qqzhi.com/uploads/2019-03-26/203922802.jpg");
            imageList.add("http://img.qqzhi.com/uploads/2019-03-25/153128925.jpg");
            imageList.add("http://img.qqzhi.com/uploads/2019-05-23/184503893.jpg");
            imageList.add("http://img.qqzhi.com/uploads/2019-04-19/181211409.jpg");
            UserInfoResult result = new UserInfoResult();
            result.setId("102");
            result.setSexName("男");
            result.setName("光翼蜗牛");
            result.setImageUrl(imageList);
            result.setAddress("大宇宙Z星际X星系J星座C恒星系统Y星球F高地");
            result.setPhoneNumber("19988886666");

            Bundle params = new Bundle();
            params.putParcelable(BuyerConstants.Keywords.KEY_USER_INFO, result);
            ARouterUtils.openFragment(getActivity(),
                    UserInfoFragment.class.getName(), params, R.color.bg_white, false);
        });

     /*   mPasswordView.setOnClickListener(v -> ARouterUtils.openFragment(getActivity(),
                UserPasswordFragment.class.getName(), null));
        mCheckUpgradeView.setOnClickListener(v -> WidgetUtils.toastMessage(mContext,
                R.string.developing));*/
        mLogoutView.setOnClickListener(v -> {
            //WidgetUtils.toastMessage(mContext, "正在退出...");
            showProgressDialog("正在退出...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //todo:模拟注销-没接口
                        Thread.sleep(1000);
                        closeProgressDialog();
                        ARouter.getInstance()
                                .build(ARouterConstants.Path.USER_LOGIN)
                                .navigation(BaseApplication.getInstance());
                        BaseApplication.getInstance().killAllActivity();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }

    @Override
    public void closeFloatView() {
    }

    @Override
    public boolean doBackPress() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
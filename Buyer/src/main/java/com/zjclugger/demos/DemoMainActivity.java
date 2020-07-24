package com.zjclugger.demos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zjclugger.buyer.R;
import com.zjclugger.buyer.ui.BuyerMainActivity;
import com.zjclugger.buyer.ui.HomeFragment;
import com.zjclugger.buyer.ui.uc.UserProfileFragment;
import com.zjclugger.lib.base.BaseActivity;
import com.zjclugger.lib.entity.JListItem;
import com.zjclugger.lib.utils.WidgetUtils;
import com.zjclugger.lib.view.ExtendRecyclerView;
import com.zjclugger.router.utils.ARouterUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 示例列表<br>
 * Created by King.Zi on 2020/7/22.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class DemoMainActivity extends BaseActivity {

    @BindView(R.id.demo_recycler_view)
    ExtendRecyclerView mRecyclerView;
    private int mIndex = 1;
    private DemoItemAdapter mItemAdapter;
    private List<JListItem<String>> mListItem = new ArrayList<>();
    private JListItem<String> mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);
        ButterKnife.bind(this);

        initListItem();
        mItemAdapter = new DemoItemAdapter(R.layout.item_common_list, mListItem);
        mItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            mItem = mListItem.get(position);
            if (!TextUtils.isEmpty(mItem.getValue())) {
                //fragment
                ARouterUtils.openFragment(DemoMainActivity.this,
                        mItem.getValue(), null, R.color.bg_white, false);
            } else {
                //activity
                if (null != mItem.getTag() && mItem.getTag() instanceof Intent) {
                    startActivity((Intent) mItem.getTag());
                } else {
                    WidgetUtils.toastErrorMessage(mContext, "未能找到：" + mItem.getText());
                }
            }
        });
        mRecyclerView.getPager().setRecordTotal(mListItem.size());
        mRecyclerView.setLayoutParameter(true, 3, true);
        mRecyclerView.setSpan(5f);
        mRecyclerView.setRefreshLayoutListener(refreshLayout -> {
            WidgetUtils.toast(mContext, getString(R.string.info_last_page));
            refreshLayout.finishLoadMoreWithNoMoreData();
        }).create(mItemAdapter, null);
    }

    private void initListItem() {
        mListItem.add(new JListItem<>(mIndex++, "首页", HomeFragment.class.getName()));
        mListItem.add(new JListItem<>(mIndex++, "电商主页", "", new Intent(mContext,
                BuyerMainActivity.class)));
        mListItem.add(new JListItem<>(mIndex++, "数据绑定-个人信息（新建和更新）",
                UserProfileFragment.class.getName()));
        mListItem.add(new JListItem<>(mIndex++, "轮播图", HomeFragment.class.getName()));
    }

    @Override
    protected boolean isDetailActivity() {
        return true;
    }

    @Override
    protected String getDetailActivityTitle() {
        return "示例列表";
    }
}
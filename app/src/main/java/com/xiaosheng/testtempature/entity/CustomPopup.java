package com.xiaosheng.testtempature.entity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TableLayout;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.xiaosheng.testtempature.R;
import com.xiaosheng.testtempature.constants.Constants;
import com.xiaosheng.testtempature.service.ThreadPool;
import com.xiaosheng.testtempature.utils.TableUtils;

import java.util.List;

public class CustomPopup extends CenterPopupView {
    private FragmentManager fragmentManager;
    private Fragment targetFragment;
    private TableLayout tableLayout;
    private OnFragmentSwitchListener listener;

    // 自定义构造函数，接收FragmentManager和目标Fragment
    public CustomPopup(@NonNull Context context, FragmentManager fragmentManager, Fragment targetFragment, OnFragmentSwitchListener listener) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.targetFragment = targetFragment;
        this.listener = listener;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_popup;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        tableLayout = findViewById(R.id.read_table);
        findViewById(R.id.tc_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadPool.tempMsg.remove(Constants.SELECTED_JSON);
                dismiss(); // 关闭弹窗
            }
        });
        findViewById(R.id.tc_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                            ToastUtils.showLong("内容为空");
                Constants.GOTO_MAIN = true;
                dismiss();
                // 使用传入的FragmentManager替换Fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, targetFragment)
                        .commitAllowingStateLoss();
                if (listener != null) {
                    listener.onFragmentSwitched();
                }
            }
        });
        Task task = () -> initTable();
        TableUtils.loadDataWithLoading(task,CustomPopup.super.getContext());
    }

    private void initTable() {
        String selected = ThreadPool.tempMsg.get(Constants.SELECTED_JSON);
        if (StringUtils.isEmpty(selected)) {
            ToastUtils.showLong("内容为空");
            return;
        }
        List<List<String>> temperatureList = JSON.parseObject(selected, new TypeReference<List<List<String>>>() {
        });

        TableUtils.addColumnHeaders(tableLayout, CustomPopup.super.getContext());
        TableUtils.addDataToTable(tableLayout, temperatureList, CustomPopup.super.getContext());
    }

    // 设置最大宽度，看需要而定，
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }

    public interface OnFragmentSwitchListener {
        void onFragmentSwitched();
    }
}
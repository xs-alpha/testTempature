package com.xiaosheng.testtempature;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.constants.Constants;
import com.xiaosheng.testtempature.dao.MyApplication;
import com.xiaosheng.testtempature.dao.mapper.TempatureHistoryMapper;
import com.xiaosheng.testtempature.entity.CustomPopup;
import com.xiaosheng.testtempature.entity.Task;
import com.xiaosheng.testtempature.entity.TempatureHistory;
import com.xiaosheng.testtempature.service.ThreadPool;
import com.xiaosheng.testtempature.utils.TableUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private TestTempatureAdapter adapterListView;
    private View view;
    private List<TempatureHistory> allData;
    private Button deleteAllLogsBtn;
    private TextView settingTitle;
    private Button deleteOldLogsBtn;

    @Override
    public void onViewCreated(@NonNull View viewc, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewc, savedInstanceState);
        view=viewc;
        // 进行初始化操作
        initBtn();
    }

    private void initBtn() {
        deleteAllLogsBtn = view.findViewById(R.id.setting_delete_all);
        settingTitle = view.findViewById(R.id.setting_title);
        deleteOldLogsBtn = view.findViewById(R.id.setting_delete_old);
        deleteAllLogsBtn.setOnClickListener(this);
        deleteOldLogsBtn.setOnClickListener(this);
        settingTitle.setOnClickListener(this);
        settingTitle.setTextColor(Color.WHITE);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_delete_all){
            new XPopup.Builder(SettingFragment.super.getActivity()).asConfirm("清空日志", "确认清空吗,清空会清空当前所有日志",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    TempatureHistoryMapper hmapper = MyApplication.tempatureHistoryMapper;
                                    hmapper.deleteAll();
                                    ToastUtils.showLong("删除成功");
                                }
                            })
                    .show();
        }
        if (view.getId() == R.id.setting_delete_old){
            new XPopup.Builder(SettingFragment.super.getActivity()).asConfirm("清空日志", "确认清空吗,清空会清空当前所有日志",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    deleteOlderThanFiveDays();
                                }
                            })
                    .show();
        }
    }


    private void deleteAllData() {
        ThreadPool.executor.submit(new Runnable() {
            @Override
            public void run() {
                TempatureHistoryMapper tempatureHistoryMapper = MyApplication.tempatureHistoryMapper;;
                tempatureHistoryMapper.deleteAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showLong("所有数据已删除");
                    }
                });
            }
        });
    }

    private void deleteOlderThanFiveDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -5);
        String fiveDaysAgo = sdf.format(cal.getTime());

        ThreadPool.executor.submit(new Runnable() {
            @Override
            public void run() {
                TempatureHistoryMapper tempatureHistoryMapper = MyApplication.tempatureHistoryMapper;;
                tempatureHistoryMapper.deleteOlderThan(fiveDaysAgo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showLong("数据删除成功");
                    }
                });
            }
        });
    }
}
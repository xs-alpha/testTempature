package com.xiaosheng.testtempature;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.constants.Constants;
import com.xiaosheng.testtempature.dao.MyApplication;
import com.xiaosheng.testtempature.dao.mapper.TempatureHistoryMapper;
import com.xiaosheng.testtempature.entity.CustomPopup;
import com.xiaosheng.testtempature.entity.Task;
import com.xiaosheng.testtempature.entity.TempatureHistory;
import com.xiaosheng.testtempature.service.ThreadPool;
import com.xiaosheng.testtempature.utils.LogUtils;
import com.xiaosheng.testtempature.utils.TableUtils;

import java.util.List;

public class LogViewActivity extends Fragment {
    private TestTempatureAdapter adapterListView;
    private ListView listView;
    private View view;
    private List<TempatureHistory> allData;
    LoadingPopupView loadingPopup;

    @Override
    public void onViewCreated(@NonNull View viewc, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewc, savedInstanceState);
        view=viewc;
        // 进行初始化操作
        Task task = new Task() {
            @Override
            public void execute() {
                initData();
            }
        };
        TableUtils.loadDataWithLoading(task,getContext());

//        initData();

    }

    private void initData() {
        TempatureHistoryMapper tmapper = MyApplication.tempatureHistoryMapper;
        allData = tmapper.getAll();

//        LogUtils.log(JSON.toJSONString(allData));
        adapterListView = new TestTempatureAdapter(LogViewActivity.super.getContext(), R.layout.activity_item_layout, allData);
        listView = (ListView) view.findViewById(R.id.log_list);
        listView.setAdapter(adapterListView);
        //设置listview点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        TempatureHistory log = allData.get(i);
                        Toast.makeText(LogViewActivity.super.getContext(), "正在加载", Toast.LENGTH_LONG).show();
                        ThreadPool.tempMsg.put(Constants.SELECTED_JSON,log.getJson());
                        new XPopup.Builder(getContext())
                                .asCustom(new CustomPopup(getContext(),getFragmentManager(), new MainWorkActivity(),(CustomPopup.OnFragmentSwitchListener) getActivity()))
                                .show();
//                    }
//                });
//
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToastUtils.showLong("长按了");
                return true;
            }
        });
//        getFragmentManager().beginTransaction().replace(R.id.frame_container,mainFragment).commitAllowingStateLoss();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_view, container, false);
        return view;
    }



}
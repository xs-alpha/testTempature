package com.xiaosheng.testtempature;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.util.DateUtils;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.constants.Constants;
import com.xiaosheng.testtempature.dao.MyApplication;
import com.xiaosheng.testtempature.dao.TempatureDataBase;
import com.xiaosheng.testtempature.dao.UserDataBase;
import com.xiaosheng.testtempature.dao.mapper.TempatureHistoryMapper;
import com.xiaosheng.testtempature.dao.mapper.UserMapper;
import com.xiaosheng.testtempature.entity.Tempature;
import com.xiaosheng.testtempature.entity.TempatureHistory;
import com.xiaosheng.testtempature.entity.TestTemConfig;
import com.xiaosheng.testtempature.entity.TestTemEntity;
import com.xiaosheng.testtempature.service.ThreadPool;
import com.xiaosheng.testtempature.utils.ListUtils;
import com.xiaosheng.testtempature.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainWorkActivity extends Fragment implements View.OnClickListener {

    private TestTempatureAdapter adapterListView;
    private ListView listView;
    private ArrayList<TestTemEntity> datas;
    private TableLayout tableLayout;
    private Integer rows = 0;
    private List<List<String>> tableData;
    private View view;
    LoadingPopupView loadingPopup;

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    private TextView editShow;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_work);
//
//        tableLayout = view.findViewById(R.id.tableLayout);
//        addColumnHeaders();
//        addRow(null);
//
//        initBtn();
//        initMapper();
//    }

    @Override
    public void onViewCreated(@NonNull View viewc, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewc, savedInstanceState);

        tableLayout = viewc.findViewById(R.id.tableLayout);
        view = viewc;
        if (ThreadPool.tempMsg.containsKey(Constants.TEMP_JSON)){
            loadDataWithLoading(true);
        }else {
            addColumnHeaders();
            addRow(null);
        }

        initBtn();
        // 初始化日志记录线程j
        initLog();
    }

    private void loadDataWithLoading(boolean fromMap) {
        if (loadingPopup == null) {
            loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                    .dismissOnBackPressed(false)
                    .isDarkTheme(true)
                    .hasShadowBg(true)
                    .asLoading("加载中")
                    .show();
        } else {
            loadingPopup.show();
        }
        loadingPopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong("渲染引擎正在渲染");
                loadData(fromMap);
            }
        }, 1000);
        loadingPopup.delayDismissWith(2000, new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_work, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.log("onDestroy");
        String json = getJsonString();
        ThreadPool.tempMsg.put(Constants.TEMP_JSON, json);
    }

    private void initBtn() {
        Button btnCp = view.findViewById(R.id.btn_cp);
        btnCp.setTextColor(Color.BLACK);
        Button btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setTextColor(Color.parseColor("#3f72af"));
        btnAdd.setBackgroundColor(Color.parseColor("#cadefc"));
        Button btnClear = view.findViewById(R.id.btn_clear);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnLoad = view.findViewById(R.id.btn_load);
        btnSave.setTextColor(Color.parseColor("#3f72af"));
        btnSave.setBackgroundColor(Color.parseColor("#dbe2ef"));
        btnClear.setTextColor(Color.parseColor("#ff9a00"));
        btnClear.setBackgroundColor(Color.parseColor("#ff165d"));
        btnLoad.setTextColor(Color.parseColor("#9896f1"));
        btnLoad.setBackgroundColor(Color.parseColor("#8ef6e4"));
        Button btnDel = view.findViewById(R.id.btn_delete);
        btnDel.setBackgroundColor(Color.RED);
        btnDel.setTextColor(Color.parseColor("#3ec1d3"));
        editShow = view.findViewById(R.id.edit_show);
        editShow.setBackgroundColor(Color.parseColor("#303841"));
        editShow.setTextColor(Color.RED);
        editShow.setTextSize(18);
//        editShow.setBackgroundResource(R.drawable.round_corner);
        btnDel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnCp.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnClear.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_cp) {
//            Toast.makeText(MainActivity.this, "复制成功", Toast.LENGTH_LONG).show();
            ToastUtils.showLong("复制成功");
            List<List<String>> tableData = getTableData();
            System.out.println(tableData);
            String res = formatTable(tableData);
            System.out.println(res);
            ClipboardUtils.copyText(res);
        }
        if (view.getId() == R.id.btn_add) {
            addRow(null);
        }
        if (view.getId() == R.id.btn_clear) {
            new XPopup.Builder(MainWorkActivity.super.getActivity()).asConfirm("清空列表", "确认清空吗,清空会清空当前所有数据,但不会删除数据库的数据",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    clearTableExceptFirstRow(tableLayout);
                                }
                            })
                    .show();
        }
        if (view.getId() == R.id.btn_load) {
            new XPopup.Builder(MainWorkActivity.super.getActivity()).asConfirm("加载", "确认加载吗,加载会覆盖当前所有内容",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    loadDataWithLoading(false);
                                }
                            })
                    .show();
        }
        if (view.getId() == R.id.btn_save) {
            new XPopup.Builder(MainWorkActivity.super.getActivity()).asConfirm("确认", "确认保存吗",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    saveJson();
                                }
                            })
                    .show();

        }
        if (view.getId() == R.id.btn_delete) {
            new XPopup.Builder(MainWorkActivity.super.getActivity()).asConfirm("删除", "确认删除数据库保存的内容吗",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    deleteData();
                                }
                            })
                    .show();
        }
    }

    private void deleteData() {
        UserMapper mapper = MyApplication.userMapper;
        Tempature tempature = new Tempature();
        tempature.setId(1);
        mapper.delete(tempature);
//        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
        ToastUtils.showLong("删除成功");
    }

    private void loadData(boolean fromMap) {
        String json = "";
        if (fromMap) {
            String selected = ThreadPool.tempMsg.get(Constants.SELECTED_JSON);
            if (!StringUtils.isTrimEmpty(selected)){
                json = selected;
                ThreadPool.tempMsg.remove(Constants.SELECTED_JSON);
            }else{
                if (ThreadPool.tempMsg.containsKey(Constants.TEMP_JSON)){
                    json = ThreadPool.tempMsg.get(Constants.TEMP_JSON);
                }else{
                    ToastUtils.showLong("加载失败");
                }
            }
        } else {
            UserMapper mapper = MyApplication.userMapper;
            Tempature tempature = mapper.getById(1);
            if (null != tempature) {
                json = tempature.getJson();
            } else {
                ToastUtils.showLong("加载失败");
            }
        }
        List<List<String>> temperatureList = JSON.parseObject(json, new TypeReference<List<List<String>>>() {
        });
//        LogUtils.log(temperatureList);
        ToastUtils.showLong("加载成功");

        addDataToTable(temperatureList);
    }


    private void addDeleteBtn(TableRow tableRow) {
        Button btn = new Button(MainWorkActivity.super.getActivity());
        btn.setText("删除");
        btn.setTextColor(Color.RED);
        tableRow.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(MainWorkActivity.super.getActivity()).asConfirm("删除", "确认删除当前行吗",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        // 删除按钮所在的行
                                        TableRow parentRow = (TableRow) v.getParent();
                                        tableLayout.removeView(parentRow);
                                        ToastUtils.showLong("删除行成功");
                                        // 刷新行号
                                        refreshNumberRows();
                                    }
                                })
                        .show();

            }
        });
    }


    /*
   刷新行号
     */
    private void refreshNumberRows() {
        // 遍历 TableLayout 中的每一行
        boolean flag = false;
        List<List<String>> tbs = getTableData();
        for (int i = 1; i <= tbs.size() + 1; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView prefixTextView = (TextView) row.getChildAt(0);
                if (null == prefixTextView) {
                    flag = true;
                }
                if (prefixTextView != null) {
                    // 更新组号
                    if (flag) {
                        prefixTextView.setText("第" + (i - 1) + "床 ");
                    } else {
//                        LogUtils.log("xxxxxx:" + i + "  table:" + tbs.size() + "preText:" + prefixTextView.getText());
                        prefixTextView.setText("第" + (i) + "床 ");
                    }
                }

            }
        }
        rows = tbs.size();
//        LogUtils.log("xxxxx-x:" + rows + "  table:" + tableLayout.getChildCount() + " tbs:" + tbs.size());
    }


    private void addColumnHeaders() {
        TableRow headerRow = new TableRow(MainWorkActivity.super.getActivity());
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        for (int i = 0; i < TestTemConfig.headers_trim.size(); i++) {
            TextView textView = new TextView(MainWorkActivity.super.getActivity());
            textView.setText(TestTemConfig.headers_trim.get(i));
            textView.setTextColor(Color.WHITE);
            textView.setWidth(TestTemConfig.columnWidths.get(i)); // Set width for the column
            headerRow.addView(textView);
        }


        tableLayout.addView(headerRow, 0); // Add header row as the first row in the table
    }


    private List<List<String>> getTableData() {
        tableData = new ArrayList<>();

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View view = tableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                List<String> rowData = new ArrayList<>();
                TableRow tableRow = (TableRow) view;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View childView = tableRow.getChildAt(j);
                    if (childView instanceof EditText) {
                        EditText editText = (EditText) childView;
                        String text = editText.getText().toString();
                        rowData.add(text);
                    }
                }
                if (rowData.size() != 0) {
                    tableData.add(rowData);
                }
            }
        }

        return tableData;
    }

    public static String formatTable(List<List<String>> tableData) {
        if (tableData == null || tableData.isEmpty()) {
            System.out.println("Table is empty.");
            return "";
        }

        int rowNum = 1;
        String res = "";
        for (List<String> row : tableData) {
            StringBuilder formattedRow = new StringBuilder(rowNum + "# ");
            for (int i = 0; i < row.size(); i++) {
                String header = TestTemConfig.headers.get(i).replace(":", "");
                String data = row.get(i).trim();
                formattedRow.append(header).append(data).append("   ");
            }
            String lineBreak = StringUtils.isEmpty(res) ? "" : "\n";
            res = res + lineBreak + formattedRow.toString();
            rowNum++;
        }
        return res;
    }




    public void clearTableExceptFirstRow(TableLayout tableLayout) {
        int childCount = tableLayout.getChildCount();
        // 保留第一行，从第二行开始删除
        for (int i = childCount - 1; i > 0; i--) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                tableLayout.removeViewAt(i);
            }
            refreshNumberRows();
        }
        rows = 0;
        addRow(null);
    }

    public void saveJson() {
        String json = getJsonString();

        FutureTask<Void> saveTask = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                saveOrUpdate(json);
                return null;
            }
        });
        executorService.submit(saveTask);
        ThreadPool.executor.submit(new Runnable() {
            @Override
            public void run() {
                saveLogs();
            }
        });
//        Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_LONG).show();
        ToastUtils.showLong("保存成功");
    }

    private String getJsonString() {
        List<List<String>> tableData = getTableData();
        String json = GsonUtils.toJson(tableData);
        return json;
    }


    private void addRow(List<String> temperatureList) {
        TableRow tableRow = new TableRow(MainWorkActivity.super.getActivity());
        rows += 1;
        TextView prefixTextView = new TextView(MainWorkActivity.super.getActivity());
        prefixTextView.setText("第" + rows + "床 ");

        // 添加单元格之间的间距
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        // 设置 prefixTextView 的外边距
        prefixTextView.setLayoutParams(cellParams);

        tableRow.addView(prefixTextView);
        // 添加每个单元格
        for (int i = 1; i <= TestTemConfig.headers.size(); i++) {
            EditText editText = new EditText(MainWorkActivity.super.getActivity());
            TextView pc = new TextView(MainWorkActivity.super.getActivity());
            pc.setText(TestTemConfig.headers.get(i - 1));
            pc.setTextColor(Color.YELLOW);

            // 设置 editText 的外边距
            editText.setLayoutParams(cellParams);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (CollectionUtils.isNotEmpty(temperatureList)) {
                editText.setText(temperatureList.get(i - 1));
            }
            editText.setBackgroundResource(R.drawable.cell_backgroud);
            // 设置光标颜色,选中颜色,选中行颜色
            editTextListener(editText, tableRow);
            // 设置 TextView 的外边距
            pc.setLayoutParams(cellParams);
            // 添加单元格到 TableRow
            tableRow.addView(pc);
            tableRow.addView(editText);
        }
        ///添加删除按钮
        addDeleteBtn(tableRow);
        // 将 TableRow 添加到 TableLayout
        tableLayout.addView(tableRow);
    }

    /**
     * 设置光标颜色,选中颜色,选中行颜色
     *
     * @param editText
     */
    private void editTextListener(EditText editText, TableRow tableRow) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    Log.e("xiaosheng", "EditText获得焦点");
                    editText.setBackgroundResource(R.drawable.cursor_color);
                    // 获取TableRow
                    TableRow tableRow = (TableRow) editText.getParent();
                    if (tableRow != null) {
                        // 获取TableLayout
                        TableLayout tableLayout = (TableLayout) tableRow.getParent();
                        if (tableLayout != null) {
                            // 获取行数
                            int rowIndex = tableLayout.indexOfChild(tableRow);
                            // 获取列数
                            int colIndex = tableRow.indexOfChild(editText) / 2; // 因为每个EditText后面都有一个TextView，所以除以2
                            // 输出行数和列数
                            editShow.setText("正在编辑:" + rowIndex + ":" + TestTemConfig.headers.get(colIndex - 1).replace(":", ""));
                            // 这里你可以根据需要进行其他操作
                        }
                    }
                } else {
//                    Log.e("xiaosheng", "EditText2失去焦点");
                    editText.setBackgroundResource(R.drawable.cell_backgroud);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 当EditText文本改变时，改变行的背景颜色
                changeRowColor(tableLayout.indexOfChild(tableRow));
            }
        });
    }

    private void changeRowColor(int rowIndex) {
        // 遍历表格中的每一行，根据索引改变背景颜色
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            if (i == rowIndex) {
                // 选中的行改变背景颜色
                row.setBackgroundColor(Color.RED);
            } else {
                // 其他行恢复原始背景颜色
                row.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private void addDataToTable(List<List<String>> temperatureList) {
        // 清空表格
        tableLayout.removeAllViews();
        rows = 0;

        // 添加列标题
        addColumnHeaders();

        // 添加数据行
        for (int i = 0; i < temperatureList.size(); i++) {
            addRow(temperatureList.get(i));
        }
    }

    private void initLog() {
        ThreadPool.submitScheduleThreadPool(Constants.LOG_TEMPATURE,new Runnable() {
            @Override
            public void run() {
                saveLogs();
            }
        });
    }

    private void saveLogs() {
        List<List<String>> tableData = getTableData();
        boolean listOfListsEmpty = ListUtils.isNestedListEmpty(tableData);
        if (listOfListsEmpty){
            // 为空不需要保存
            return;
        }
        // 和上次暂存的一样,也不需要
        String json = GsonUtils.toJson(tableData);
        String latestJson = ThreadPool.tempMsg.get(Constants.LATEST_MSG);
        if (null!=latestJson && !StringUtils.isTrimEmpty(latestJson)){
            if (json.equals(latestJson)) {
                LogUtils.log("信息一样,return");
                return;
            }else{
                ThreadPool.tempMsg.put(Constants.LATEST_MSG,json);
            }
        }else {
            ThreadPool.tempMsg.put(Constants.LATEST_MSG,json);
        }
        String curDate = DateUtils.format(new Date(), Constants.YYYY_MM_DD_HH_MM_SS);
        TempatureHistory th = new TempatureHistory();
        th.setLine(tableData.size());
        th.setTimestamp(curDate);
        th.setJson(json);
        TempatureHistoryMapper tmapper = MyApplication.tempatureHistoryMapper;
        tmapper.insert(th);

        LogUtils.log(""+listOfListsEmpty);
    }


    private void saveOrUpdate(String json) {
        Tempature tempature = new Tempature();
        UserMapper mapper = MyApplication.userMapper;
        Tempature tm = mapper.getById(1);
        tempature.setId(1);
        tempature.setJson(json);
        if (ObjectUtils.isEmpty(tm)) {
            mapper.insert(tempature);
        } else {
            mapper.update(tempature);
        }
    }


}
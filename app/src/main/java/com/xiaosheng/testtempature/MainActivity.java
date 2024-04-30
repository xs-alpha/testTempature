package com.xiaosheng.testtempature;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.health.connect.datatypes.units.Temperature;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.dao.MyApplication;
import com.xiaosheng.testtempature.dao.UserDataBase;
import com.xiaosheng.testtempature.dao.mapper.UserMapper;
import com.xiaosheng.testtempature.entity.Tempature;
import com.xiaosheng.testtempature.entity.TemperatureData;
import com.xiaosheng.testtempature.entity.TestTemConfig;
import com.xiaosheng.testtempature.entity.TestTemEntity;
import com.xiaosheng.testtempature.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TestTempatureAdapter adapterListView;
    private ListView listView;
    private ArrayList<TestTemEntity> datas;
    private TableLayout tableLayout;
    private Integer rows = 0;
    private List<List<String>> tableData;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        addColumnHeaders();
        addRow(null);

        datas = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            TestTemEntity testTemEntity = new TestTemEntity();
            testTemEntity.setAt1("A:");
            testTemEntity.setAt2("B:");
            testTemEntity.setAt3("C:");
            testTemEntity.setAt4("D:");
            testTemEntity.setAt5("E:");
            testTemEntity.setAt6("F:");
            testTemEntity.setAt7("G:");
            testTemEntity.setAt8("H:");
            testTemEntity.setGroup(i + "床");
            datas.add(testTemEntity);
        }

        initBtn();
        initMapper();
    }

    private void initBtn() {
        Button btnCp = findViewById(R.id.btn_cp);
        btnCp.setTextColor(Color.BLACK);
        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setTextColor(Color.parseColor("#3f72af"));
        btnAdd.setBackgroundColor(Color.parseColor("#cadefc"));
        Button btnClear = findViewById(R.id.btn_clear);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnLoad = findViewById(R.id.btn_load);
        btnSave.setTextColor(Color.parseColor("#3f72af"));
        btnSave.setBackgroundColor(Color.parseColor("#dbe2ef"));
        btnClear.setTextColor(Color.parseColor("#ff9a00"));
        btnClear.setBackgroundColor(Color.parseColor("#ff165d"));
        btnLoad.setTextColor(Color.parseColor("#9896f1"));
        btnLoad.setBackgroundColor(Color.parseColor("#8ef6e4"));
        Button btnDel = findViewById(R.id.btn_delete);
        btnDel.setBackgroundColor(Color.RED);
        btnDel.setTextColor(Color.parseColor("#3ec1d3"));
        btnDel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnCp.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }


    public String toChar(int num) {
        String tcMsg = "";
        char sl = 0;
        if (-1 < num && num < 10) {
            tcMsg = "" + num;
        } else if (9 < num && num < 36) {
            sl = (char) (num - 10 + (int) 'A');
            tcMsg = "" + sl;
        } else if (35 < num && num < 62) {
            sl = (char) (num - 36 + (int) 'a');
            tcMsg = "" + sl;
        }
        return tcMsg;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_cp) {
            Toast.makeText(MainActivity.this, "复制成功", Toast.LENGTH_LONG).show();
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
            new XPopup.Builder(MainActivity.this).asConfirm("清空列表", "确认清空吗,清空会清空当前所有数据,但不会删除数据库的数据",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    clearTableExceptFirstRow(tableLayout);
                                }
                            })
                    .show();
        }
        if (view.getId() == R.id.btn_load) {
            new XPopup.Builder(MainActivity.this).asConfirm("加载", "确认加载吗,加载会覆盖当前所有内容",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    loadData();
                                }
                            })
                    .show();
        }
        if (view.getId() == R.id.btn_save) {
            new XPopup.Builder(MainActivity.this).asConfirm("确认", "确认保存吗",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    saveJson();
                                }
                            })
                    .show();

        }
        if (view.getId() == R.id.btn_delete){
            new XPopup.Builder(MainActivity.this).asConfirm("删除", "确认删除数据库保存的内容吗",
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
        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
    }

    private void loadData() {
        UserMapper mapper = MyApplication.userMapper;
        Tempature tempature = mapper.getById(1);
        if (null != tempature){
            String json = tempature.getJson();
            List<List<String>> temperatureList = JSON.parseObject(json, new TypeReference<List<List<String>>>(){});
            System.out.println(temperatureList);
            ToastUtils.showLong("加载成功");

            addDataToTable(temperatureList );
        }else{
            ToastUtils.showLong("加载失败");
        }
    }


    private void addDeleteBtn(TableRow tableRow) {
        Button btn = new Button(this);
        btn.setText("删除");
        btn.setTextColor(Color.RED);
        tableRow.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(MainActivity.this).asConfirm("删除", "确认删除当前行吗",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        // 删除按钮所在的行
                                        TableRow parentRow = (TableRow) v.getParent();
                                        tableLayout.removeView(parentRow);
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
        for (int i = 1; i <= tbs.size()+1; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView prefixTextView = (TextView) row.getChildAt(0);
                if (null == prefixTextView){
                    flag = true;
                }
                if (prefixTextView != null) {
                    // 更新组号
                    if (flag){
                        prefixTextView.setText("第" + (i-1) + "床 ");
                    }else{
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
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        for (int i = 0; i < TestTemConfig.headers_trim.size(); i++) {
            TextView textView = new TextView(this);
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
            res = res + lineBreak+formattedRow.toString();
            rowNum++;
        }
        return res;
    }


    private void initMapper() {
        MyApplication.userDataBase = Room.databaseBuilder(this, UserDataBase.class, "user")
                // 允许迁移数据库,(发生数据变更时,room会默认删除源数据库再创建数据库,数据会丢失.)
                .addMigrations()
                .allowMainThreadQueries()
                .build();
        MyApplication.userMapper = MyApplication.getDb().userMapper();
        LogUtils.log("created");
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

    public void saveJson(){
        List<List<String>> tableData = getTableData();
        String json = GsonUtils.toJson(tableData);

        FutureTask<Void> saveTask = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                saveOrUpdate(json);
                return null;
            }
        });
        executorService.submit(saveTask);
        Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_LONG).show();
    }




















    private void addRow(List<String> temperatureList) {
        TableRow tableRow = new TableRow(this);
        rows += 1;
        TextView prefixTextView = new TextView(this);
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
            EditText editText = new EditText(this);
            TextView pc = new TextView(this);
            pc.setText(TestTemConfig.headers.get(i - 1));
            pc.setTextColor(Color.YELLOW);

            // 设置 editText 的外边距
            editText.setLayoutParams(cellParams);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (CollectionUtils.isNotEmpty(temperatureList)){
                editText.setText(temperatureList.get(i-1));
            }
            editText.setBackgroundResource(R.drawable.cell_backgroud);
            // 设置光标颜色,选中颜色,选中行颜色
            editTextListener(editText,tableRow);
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
     * @param editText
     */
    private void editTextListener(EditText editText,TableRow tableRow){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("xiaosheng", "EditText2获得焦点");
                    editText.setBackgroundResource(R.drawable.cursor_color);

                } else {
                    Log.e("xiaosheng", "EditText2失去焦点");
                    editText.setBackgroundResource(R.drawable.cell_backgroud);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
            addRow( temperatureList.get(i));
        }
    }


    private void saveOrUpdate(String json){
        Tempature tempature = new Tempature();
        UserMapper mapper = MyApplication.userMapper;
        Tempature tm = mapper.getById(1);
        tempature.setId(1);
        tempature.setJson(json);
        if (ObjectUtils.isEmpty(tm)){
            mapper.insert(tempature);
        }else{
            mapper.update(tempature);
        }
    }

    //两次滑动返回桌面，不结束当前activity
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        ToastUtils.showLong("再按1次返回桌面");
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis();
        }else {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
    }


}
package com.xiaosheng.testtempature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.dao.MyApplication;
import com.xiaosheng.testtempature.dao.UserDataBase;
import com.xiaosheng.testtempature.dao.mapper.UserMapper;
import com.xiaosheng.testtempature.entity.TestTemConfig;
import com.xiaosheng.testtempature.entity.TestTemEntity;
import com.xiaosheng.testtempature.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TestTempatureAdapter adapterListView;
    private ListView listView;
    private ArrayList<TestTemEntity> datas;
    private TableLayout tableLayout;
    private Integer rows = 0;
    private List<List<String>> tableData;
    UserMapper mapper = MyApplication.userMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        addColumnHeaders();
        addRow();

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
            testTemEntity.setGroup(i + "组");
            datas.add(testTemEntity);
        }
        adapterListView = new TestTempatureAdapter(MainActivity.this, R.layout.list_item, datas);
        listView = (ListView) findViewById(R.id.tm_list);
        listView.setAdapter(adapterListView);
        //设置listview点击事件


        initBtn();
        initMapper();
    }

    private void initBtn() {
        Button btnCp = findViewById(R.id.btn_cp);
        btnCp.setTextColor(Color.CYAN);
        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setTextColor(Color.parseColor("#defcf9"));
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
            addRow();
        }
        if (view.getId() == R.id.btn_clear) {
            clearTableExceptFirstRow(tableLayout);
        }
        if (view.getId() == R.id.btn_load) {

        }
        if (view.getId() == R.id.btn_save) {

        }
    }


    private void addRow() {
        TableRow tableRow = new TableRow(this);
        rows += 1;
        TextView prefixTextView = new TextView(this);
        prefixTextView.setText("第" + rows + "组 ");

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
            editText.setBackgroundResource(R.drawable.cell_backgroud);
//            editText.setPadding(8, 20, 8, 20);

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

    private void addDeleteBtn(TableRow tableRow) {
        Button btn = new Button(this);
        btn.setText("删除");
        btn.setTextColor(Color.RED);
        tableRow.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除按钮所在的行
                TableRow parentRow = (TableRow) v.getParent();
                tableLayout.removeView(parentRow);
                // 刷新行号
                refreshNumberRows();
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
                        prefixTextView.setText("第" + (i-1) + "组 ");
                    }else{
                        LogUtils.log("xxxxxx:" + i + "  table:" + tbs.size() + "preText:" + prefixTextView.getText());
                        prefixTextView.setText("第" + (i) + "组 ");
                    }
                }

            }
        }
        rows = tbs.size();
        LogUtils.log("xxxxx-x:" + rows + "  table:" + tableLayout.getChildCount() + " tbs:" + tbs.size());
    }


    private void addColumnHeaders() {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add your column headers here
        int[] columnWidths = {150, 0, 150, 0, 150, 0, 150, 0, 150, 0, 150, 0, 250, 200};
        // Add column headers with widths
        for (int i = 0; i < TestTemConfig.headers_trim.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(TestTemConfig.headers_trim.get(i));
//            textView.setPadding(8, 8, 8, 8);
            textView.setTextColor(Color.WHITE);
            textView.setWidth(columnWidths[i]); // Set width for the column
//            textView.setBackgroundResource(R.drawable.cell_shape); // Optional: Set background drawable for cell
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
            res += formattedRow.toString();
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
        addRow();
    }

}
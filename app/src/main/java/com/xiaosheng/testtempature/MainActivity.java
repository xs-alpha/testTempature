package com.xiaosheng.testtempature;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.entity.TestTemConfig;
import com.xiaosheng.testtempature.entity.TestTemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TestTempatureAdapter adapterListView;
    private ListView listView;
    private ArrayList<TestTemEntity> datas;
    private TableLayout tableLayout;
    private Integer rows = 0;
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
            testTemEntity.setGroup(i+"组");
            datas.add(testTemEntity);
        }
        adapterListView = new TestTempatureAdapter(MainActivity.this, R.layout.list_item, datas);
        listView = (ListView) findViewById(R.id.tm_list);
        listView.setAdapter(adapterListView);
        //设置listview点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                System.out.println("点击");

            }
        });


        findViewById(R.id.btn_cp).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        tableView();

    }

    private void tableView() {
        EditableTableLayout tableLayout = new EditableTableLayout(this);
        String[] rowData1 = {"Cell 1", "Cell 2", "Cell 3"};
        String[] rowData2 = {"Cell 4", "Cell 5", "Cell 6"};
        tableLayout.addEditableRow(rowData1);
        tableLayout.addEditableRow(rowData2);


    }


    public String toChar(int num){
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
            System.out.println("打印");
            List<ArrayList<String>> tableData = getTableData();
            System.out.println(tableData);
        }
        if (view.getId() == R.id.btn_add) {
            addRow();
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
        cellParams.setMargins(10, 10, 10, 10);

        // 设置 prefixTextView 的外边距
        prefixTextView.setLayoutParams(cellParams);

        tableRow.addView(prefixTextView);

        // 添加每个单元格
        for (int i = 1; i <= TestTemConfig.headers.size(); i++) {
            EditText editText = new EditText(this);
            TextView pc = new TextView(this);
            pc.setText(TestTemConfig.headers.get(i - 1));

            // 设置 editText 的外边距
            editText.setLayoutParams(cellParams);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);

            // 设置 TextView 的外边距
            pc.setLayoutParams(cellParams);

            // 添加单元格到 TableRow
            tableRow.addView(pc);
            tableRow.addView(editText);
        }

        // 将 TableRow 添加到 TableLayout
        tableLayout.addView(tableRow);
    }



    private ArrayList<ArrayList<String>> getTableData() {
        ArrayList<ArrayList<String>> tableData = new ArrayList<>();

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View view = tableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                ArrayList<String> rowData = new ArrayList<>();
                TableRow tableRow = (TableRow) view;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View childView = tableRow.getChildAt(j);
                    if (childView instanceof EditText) {
                        EditText editText = (EditText) childView;
                        String text = editText.getText().toString();
                        rowData.add(text);
                    }
                }
                if (rowData.size() != 0){
                    tableData.add(rowData);
                }
            }
        }

        return tableData;
    }
    private void addColumnHeaders() {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add your column headers here
        int[] columnWidths = {100, 150, 150,150,150,150};
        // Add column headers with widths
        for (int i = 0; i < TestTemConfig.headers.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(TestTemConfig.headers.get(i));
            textView.setPadding(8, 8, 8, 8);
            textView.setTextColor(Color.WHITE);
            textView.setWidth(columnWidths[i]); // Set width for the column
//            textView.setBackgroundResource(R.drawable.cell_shape); // Optional: Set background drawable for cell
            headerRow.addView(textView);
        }


        tableLayout.addView(headerRow, 0); // Add header row as the first row in the table
    }

}
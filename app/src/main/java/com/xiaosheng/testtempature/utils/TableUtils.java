package com.xiaosheng.testtempature.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.xiaosheng.testtempature.MainWorkActivity;
import com.xiaosheng.testtempature.R;
import com.xiaosheng.testtempature.entity.Task;
import com.xiaosheng.testtempature.entity.TestTemConfig;

import java.util.List;

public class TableUtils {

    public static void addColumnHeaders(TableLayout tableLayout,Context context) {
        TableRow headerRow = new TableRow(context);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        for (int i = 0; i < TestTemConfig.headers_trim.size(); i++) {
            TextView textView = new TextView(context);
            textView.setText(TestTemConfig.headers_trim.get(i));
            textView.setTextColor(Color.WHITE);
            textView.setWidth(TestTemConfig.columnWidths.get(i)); // Set width for the column
            headerRow.addView(textView);
        }


        tableLayout.addView(headerRow, 0); // Add header row as the first row in the table
    }

//    public static void addRow(TableLayout tableLayout,List<String> temperatureList, Context context,int rows) {
//        TableRow tableRow = new TableRow(context);
//        rows += 1;
//        TextView prefixTextView = new TextView(context);
//        prefixTextView.setText("第" + rows + "床 ");
//
//        // 添加单元格之间的间距
//        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT
//        );
//
//        // 设置 prefixTextView 的外边距
//        prefixTextView.setLayoutParams(cellParams);
//
//        tableRow.addView(prefixTextView);
//        // 添加每个单元格
//        for (int i = 1; i <= TestTemConfig.headers.size(); i++) {
//            EditText editText = new EditText(context);
//            TextView pc = new TextView(context);
//            pc.setText(TestTemConfig.headers.get(i - 1));
//            pc.setTextColor(Color.YELLOW);
//
//            // 设置 editText 的外边距
//            editText.setLayoutParams(cellParams);
//            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            if (CollectionUtils.isNotEmpty(temperatureList)) {
//                editText.setText(temperatureList.get(i - 1));
//            }
//            editText.setBackgroundResource(R.drawable.cell_backgroud);
//            // 设置光标颜色,选中颜色,选中行颜色
////            editTextListener(editText, tableRow);
//            // 设置 TextView 的外边距
//            pc.setLayoutParams(cellParams);
//            // 添加单元格到 TableRow
//            tableRow.addView(pc);
//            tableRow.addView(editText);
//        }
//        ///添加删除按钮
////        addDeleteBtn(tableRow);
//        // 将 TableRow 添加到 TableLayout
//        tableLayout.addView(tableRow);
//    }

    public static void addDataToTable(TableLayout tableLayout,List<List<String>> temperatureList,Context context) {
        // 清空表格
        tableLayout.removeAllViews();
        int rows = 0;

        // 添加列标题
        addColumnHeaders(tableLayout,context);

        // 添加数据行
        for (int i = 0; i < temperatureList.size(); i++) {
            addRow(tableLayout,temperatureList.get(i),context, i+1);
        }
    }

    public static void addRow(TableLayout tableLayout,List<String> temperatureList, Context context,int rows) {
        TableRow tableRow = new TableRow(context);
        TextView prefixTextView = new TextView(context);
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
            TextView editText = new TextView(context);
            TextView pc = new TextView(context);
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
//            editTextListener(editText, tableRow);
            // 设置 TextView 的外边距
            pc.setLayoutParams(cellParams);
            // 添加单元格到 TableRow
            tableRow.addView(pc);
            tableRow.addView(editText);
        }
        ///添加删除按钮
//        addDeleteBtn(tableRow);
        // 将 TableRow 添加到 TableLayout
        tableLayout.addView(tableRow);
    }

    public static void loadDataWithLoading(Task task,Context context) {
        LoadingPopupView    loadingPopup = (LoadingPopupView) new XPopup.Builder(context)
                    .dismissOnBackPressed(false)
                    .isDarkTheme(true)
                    .hasShadowBg(true)
                    .asLoading("加载中")
                    .show();
        loadingPopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong("渲染引擎正在渲染");
                if (null !=task){
                    task.execute();
                }

            }
        }, 1000);
        loadingPopup.delayDismissWith(2000, new Runnable() {
            @Override
            public void run() {
            }
        });
    }
}

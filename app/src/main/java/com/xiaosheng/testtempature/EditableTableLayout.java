package com.xiaosheng.testtempature;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class EditableTableLayout extends TableLayout {

    public EditableTableLayout(Context context) {
        super(context);
    }

    public void addEditableRow(String[] rowData) {
        TableRow row = new TableRow(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(params);

        for (String data : rowData) {
            EditText editText = new EditText(getContext());
            editText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            editText.setText(data);
            editText.setGravity(Gravity.CENTER);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            row.addView(editText);
        }

        addView(row);
    }

    public String[][] getData() {
        String[][] data = new String[getChildCount()][];
        for (int i = 0; i < getChildCount(); i++) {
            TableRow row = (TableRow) getChildAt(i);
            String[] rowData = new String[row.getChildCount()];
            for (int j = 0; j < row.getChildCount(); j++) {
                EditText editText = (EditText) row.getChildAt(j);
                rowData[j] = editText.getText().toString();
            }
            data[i] = rowData;
        }
        return data;
    }
}

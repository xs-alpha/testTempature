<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:orientation="vertical"
tools:context=".MainActivity">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_horizontal">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center_horizontal"
            android:text="温度测量"
            android:textColor="#1968DD"
            android:textSize="30sp"
            android:textStyle="bold" />
    </LinearLayout>

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <HorizontalScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:scrollbars="horizontal">

            <TableLayout
                android:id="@+id/tableLayout"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="5sp"
                android:layout_marginRight="50dp"
                android:divider="@drawable/line_h"
                android:showDividers="beginning|middle|end">
                <!-- Add TableRows and cells dynamically here -->
            </TableLayout>
        </HorizontalScrollView>
    </ScrollView>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:orientation="horizontal">

            <TextView
                android:layout_width="122sp"
                android:id="@+id/edit_show"
                android:layout_marginRight="10sp"
                android:textStyle="bold"
                android:gravity="center"
                android:layout_marginLeft="10sp"
                android:layout_height="44sp"></TextView>
            <Button
                android:id="@+id/btn_add"
                android:layout_width="160sp"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:text="添加一行"
                android:textStyle="bold" />
        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center_horizontal">

            <Button
                android:id="@+id/btn_save"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:text="保存"
                android:textStyle="bold" />

            <Button
                android:id="@+id/btn_clear"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:text="清空"
                android:textStyle="bold" />

            <Button
                android:id="@+id/btn_load"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:text="加载"
                android:textStyle="bold" />

            <Button
                android:id="@+id/btn_delete"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:text="删除"
                android:textStyle="bold" />
        </LinearLayout>

        <Button
            android:id="@+id/btn_cp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="复制" />
    </LinearLayout>
</LinearLayout>
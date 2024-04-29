package com.xiaosheng.testtempature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.entity.TestTemEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TestTempatureAdapter adapterListView;
    private ListView listView;
    private ArrayList<TestTemEntity> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        if (view.getId()==R.id.btn_cp){
            System.out.println("打印");
            for (TestTemEntity data : datas) {
                System.out.print(data.getAe1());
                System.out.print(data.getAe2());
                System.out.print(data.getAe3());
                System.out.print(data.getAe4());
                System.out.println();
            }
        }
    }
}
package com.xiaosheng.testtempature;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.health.connect.datatypes.units.Temperature;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.google.android.material.navigation.NavigationView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.xiaosheng.testtempature.adapter.TestTempatureAdapter;
import com.xiaosheng.testtempature.constants.Constants;
import com.xiaosheng.testtempature.dao.MyApplication;
import com.xiaosheng.testtempature.dao.TempatureDataBase;
import com.xiaosheng.testtempature.dao.UserDataBase;
import com.xiaosheng.testtempature.dao.mapper.UserMapper;
import com.xiaosheng.testtempature.entity.CustomPopup;
import com.xiaosheng.testtempature.entity.Tempature;
import com.xiaosheng.testtempature.entity.TemperatureData;
import com.xiaosheng.testtempature.entity.TestTemConfig;
import com.xiaosheng.testtempature.entity.TestTemEntity;
import com.xiaosheng.testtempature.service.ThreadPool;
import com.xiaosheng.testtempature.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomPopup.OnFragmentSwitchListener {
    private BroadcastReceiver updateBtnColorReceiver;
    private TestTempatureAdapter adapterListView;
    private ListView listView;
    private ArrayList<TestTemEntity> datas;
    private TableLayout tableLayout;
    private Integer rows = 0;
    private List<List<String>> tableData;

    private TextView editShow;
    private Fragment mainFragment;
    private Fragment logFragment;
    private TextView mainBtn;
    private TextView logBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtn();
        initMapper();
        initView();
    }



    private void initView() {
        mainFragment = new MainWorkActivity();
        logFragment = new LogViewActivity();
        getFragmentManager().beginTransaction().add(R.id.frame_container, mainFragment).commitAllowingStateLoss();
    }

    private void initBtn() {
        mainBtn = findViewById(R.id.nav_main);
        logBtn = findViewById(R.id.nav_log);
        mainBtn.setOnClickListener(this);
        logBtn.setOnClickListener(this);
        mainBtn.setTextColor(Color.BLUE);
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.nav_main){
            mainBtn.setTextColor(Color.BLUE);
            logBtn.setTextColor(Color.WHITE);
            getFragmentManager().beginTransaction().replace(R.id.frame_container,mainFragment).commitAllowingStateLoss();
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,mainFragment).commitAllowingStateLoss();
        }
        if (view.getId()==R.id.nav_log){
            logBtn.setTextColor(Color.BLUE);
            mainBtn.setTextColor(Color.WHITE);
            getFragmentManager().beginTransaction().replace(R.id.frame_container,logFragment).commitAllowingStateLoss();
        }
    }

    private void initMapper() {
        MyApplication.userDataBase = Room.databaseBuilder(this, UserDataBase.class, "user")
                // 允许迁移数据库,(发生数据变更时,room会默认删除源数据库再创建数据库,数据会丢失.)
                .addMigrations()
                .allowMainThreadQueries()
                .build();
        MyApplication.tempatureDataBase = Room.databaseBuilder(this, TempatureDataBase.class, "temp_log")
                // 允许迁移数据库,(发生数据变更时,room会默认删除源数据库再创建数据库,数据会丢失.)
                .addMigrations()
                .allowMainThreadQueries()
                .build();
        MyApplication.userMapper = MyApplication.getDb().userMapper();
        MyApplication.tempatureHistoryMapper = MyApplication.getLogDb().tempatureHistoryMapper();
        LogUtils.log("created");
    }

//    private void registerReceiver() {
//        updateBtnColorReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if ("UPDATE_BUTTON_COLOR".equals(intent.getAction())) {
//                    mainBtn.setTextColor(Color.BLUE);
//                    logBtn.setTextColor(Color.WHITE);
//                }
//            }
//        };
//        IntentFilter filter = new IntentFilter("UPDATE_BUTTON_COLOR");
//        registerReceiver(updateBtnColorReceiver, filter);
//    }

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

    @Override
    public void onFragmentSwitched() {
        LogUtils.log("switched");
        mainBtn.setTextColor(Color.BLUE);
        logBtn.setTextColor(Color.WHITE);
    }
}
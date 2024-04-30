package com.xiaosheng.testtempature.utils;

import android.util.Log;

import com.xiaosheng.testtempature.constants.UserConstants;


public class LogUtils {
    public static void log(String msg){
        Log.d(UserConstants.TAG,msg);
    }
    public static void logi(String msg){
        Log.i(UserConstants.TAG,msg);
    }
    public static void loge(String msg){
        Log.e(UserConstants.TAG,msg);
    }
}

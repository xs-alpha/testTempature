package com.xiaosheng.testtempature.utils;

import java.util.List;

public class ListUtils {
    // 判断List<List<String>>是否为空
// 检查一个List<List<String>>是否为空
    public static boolean isNestedListEmpty(List<List<String>> nestedList) {
        // 如果最外层List为空，直接返回true
        if (nestedList == null || nestedList.isEmpty()) {
            return true;
        }

        // 遍历最外层List的每一个内层List
        for (List<String> innerList : nestedList) {
            // 如果内层List不为空且至少有一个非空字符串，返回false
            if (innerList != null && !innerList.isEmpty()) {
                for (String str : innerList) {
                    if (str != null && !str.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // 如果所有的内层List都为空或者仅包含空字符串，返回true
        return true;
    }
}


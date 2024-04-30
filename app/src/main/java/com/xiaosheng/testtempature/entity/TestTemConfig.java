package com.xiaosheng.testtempature.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestTemConfig {
    public static final List<String> headers = Arrays.asList("A:", "B:", "C:", "D:", "E:", "F:");
    public static final List<String> headers_trim = Arrays.asList("","A:","", "B:","", "C:","", "D:","", "E:","", "F:","","   ");
    public static final List<Integer> columnWidths = Arrays.asList(150, 0, 150, 0, 150, 0, 150, 0, 150, 0, 150, 0, 250, 200);
}

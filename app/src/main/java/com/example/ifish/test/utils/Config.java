package com.example.ifish.test.utils;

import android.os.Environment;

public class Config {

    private static final String TAG = "CONFIG";
    // 存储网络状态，由监听器去监听网路连接情况改变此变量的值，然后其他应用读取该值即可 0-无网络连接，1-wifi，2-不是wifi的网络连接
    // TODO 默认值设置为1，避免出现app未请求到获取网络状态的权限而出现的问题。理论上来说，如果用户未授予权限的话则该值一直是1.是否连接网络通过请求结果判断
    private static volatile int NETWORK = 1;

    /**
     * 设置网络连接
     *
     * @param i
     */
    public static synchronized void setNETWORK(int i) {
        NETWORK = i;
    }

    /**
     * 获取当前网络连接状态
     *
     * @return
     */
    public static int getNETWORK() {
        return NETWORK;
    }
}
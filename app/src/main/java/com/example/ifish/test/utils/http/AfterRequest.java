package com.example.ifish.test.utils.http;

public interface AfterRequest {
        /**
         * 请求成功
         */
        void success();

        /**
         * 请求失败
         * @param code 错误码，-1表示网络请求错误，即无法连接到服务器
         * @param msg 提示信息
         */
        void failure(int code, String msg);

    }

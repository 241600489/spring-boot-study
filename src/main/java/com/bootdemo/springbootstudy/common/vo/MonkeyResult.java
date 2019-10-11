package com.bootdemo.springbootstudy.common.vo;

import lombok.Data;

@Data
public class MonkeyResult<T> {
    private T data;
    private int code;
    private String message;

    public MonkeyResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MonkeyResult(int code, String message,T data) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> MonkeyResult<T>  success(String message,T data) {
        return new MonkeyResult<>(0, message, data);
    }

    public static <T> MonkeyResult<T> fail(String message) {
        return new MonkeyResult<>(1, message);
    }

    public static MonkeyResult success(String message) {
        return new MonkeyResult(0, message);
    }
}

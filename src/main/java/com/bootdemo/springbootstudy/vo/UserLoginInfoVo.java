package com.bootdemo.springbootstudy.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserLoginInfoVo {
    private String username;
    private List<String> roleList;
}

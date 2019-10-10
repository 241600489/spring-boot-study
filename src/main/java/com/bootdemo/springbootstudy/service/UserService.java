package com.bootdemo.springbootstudy.service;

import com.bootdemo.springbootstudy.repostitry.UserInfo;

public interface UserService  {

    UserInfo findByUserName(String username);
}

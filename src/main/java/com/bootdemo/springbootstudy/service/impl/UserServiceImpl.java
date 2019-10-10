package com.bootdemo.springbootstudy.service.impl;

import com.bootdemo.springbootstudy.dao.UserInfoDao;
import com.bootdemo.springbootstudy.repostitry.UserInfo;
import com.bootdemo.springbootstudy.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo findByUserName(String username) {
        return userInfoDao.findUserInfoByUsername(username);
    }
}

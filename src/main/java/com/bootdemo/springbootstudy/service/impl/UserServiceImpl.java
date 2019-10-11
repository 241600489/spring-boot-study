package com.bootdemo.springbootstudy.service.impl;

import com.bootdemo.springbootstudy.common.vo.MonkeyResult;
import com.bootdemo.springbootstudy.dao.UserInfoDao;
import com.bootdemo.springbootstudy.repostitry.UserInfo;
import com.bootdemo.springbootstudy.security.PasswordHelper;
import com.bootdemo.springbootstudy.service.UserService;
import com.bootdemo.springbootstudy.vo.UserLoginInfoVo;
import com.bootdemo.springbootstudy.vo.UserLoginVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private PasswordHelper passwordHelper;

    @Override
    public UserInfo findByUserName(String username) {
        return userInfoDao.findUserInfoByUsername(username);
    }

    @Override
    public MonkeyResult<UserLoginInfoVo> login(UsernamePasswordToken usernamePasswordToken) {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
            UserInfo userInfo = (UserInfo)subject.getPrincipal();
            UserLoginInfoVo userLoginInfoVo = new UserLoginInfoVo();
            BeanUtils.copyProperties(userInfo,userLoginInfoVo);
            return MonkeyResult.success("success", userLoginInfoVo);
        } catch (AuthenticationException e) {
            return MonkeyResult.fail(e.getMessage());
        }

    }

    @Override
    public MonkeyResult register(UserLoginVo loginVo) {
        Assert.notNull(loginVo,"loginVo can not be null");
        // query by username
        UserInfo userInfo = userInfoDao.findUserInfoByUsername(loginVo.getUsername());
        if (Objects.nonNull(userInfo)) {
            return MonkeyResult.fail("用户名已存在");
        }
        passwordHelper.encryptPassword(loginVo);

        userInfo = new UserInfo();
        BeanUtils.copyProperties(loginVo, userInfo);
        userInfoDao.save(userInfo);
        return MonkeyResult.success("register success");
    }

}

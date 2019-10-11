package com.bootdemo.springbootstudy.service;

import com.bootdemo.springbootstudy.common.vo.MonkeyResult;
import com.bootdemo.springbootstudy.repostitry.UserInfo;
import com.bootdemo.springbootstudy.vo.UserLoginInfoVo;
import com.bootdemo.springbootstudy.vo.UserLoginVo;
import org.apache.shiro.authc.UsernamePasswordToken;

public interface UserService  {

    UserInfo findByUserName(String username);


    MonkeyResult<UserLoginInfoVo> login(UsernamePasswordToken usernamePasswordToken);

    MonkeyResult register(UserLoginVo loginVo);


}

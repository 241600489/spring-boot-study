package com.bootdemo.springbootstudy.controller;


import com.bootdemo.springbootstudy.common.vo.MonkeyResult;
import com.bootdemo.springbootstudy.service.UserService;
import com.bootdemo.springbootstudy.vo.UserLoginVo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monkey")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public MonkeyResult login(@RequestBody UserLoginVo userloginVo) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
        usernamePasswordToken.setPassword(userloginVo.getPassword().toCharArray());
        usernamePasswordToken.setUsername(userloginVo.getUsername());

        return userService.login(usernamePasswordToken);

    }

    @RequestMapping("/register")
    public MonkeyResult register(@RequestBody UserLoginVo userLoginVo) {
        return userService.register(userLoginVo);
    }

    @GetMapping("/query")
    public MonkeyResult query() {
        return MonkeyResult.success("ok");
    }
}

package com.bootdemo.springbootstudy.security;

import com.bootdemo.springbootstudy.vo.UserLoginVo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * password
 */
public class PasswordHelper {
    //Algorithm
    public static final String HASH_ALGORITHM = "MD5";
    public static final int HASH_ITERATIONS = 1024;


    public void encryptPassword(UserLoginVo userLoginVo) {
        String newPs = new SimpleHash(HASH_ALGORITHM,
                userLoginVo.getPassword(), ByteSource.Util.bytes(userLoginVo.getUsername()), HASH_ITERATIONS).toHex();
        userLoginVo.setSalt(userLoginVo.getUsername());
        userLoginVo.setPassword(newPs);
    }

}

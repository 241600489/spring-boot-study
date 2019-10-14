package com.bootdemo.springbootstudy.security;

import com.bootdemo.springbootstudy.repostitry.UserInfo;
import com.bootdemo.springbootstudy.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * 继承AuthorizingRealm 实现认证和授权
 */
public class PermissionShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principalCollection.getPrimaryPrincipal();
        userInfo.getRoleList().forEach(sysRole -> {
            authorizationInfo.addRole(sysRole.getRole());
            sysRole.getPermissions().forEach(sysPermission ->
                authorizationInfo.addStringPermission(sysPermission.getPermission())
            );
        });
        return authorizationInfo;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //get user
        String username = (String) authenticationToken.getPrincipal();
        UserInfo userInfo = userService.findByUserName(username);
        if (Objects.isNull(userInfo)) {
            throw new AuthenticationException("username " + username + " is  not exist");
        }
        SecurityUtils.getSubject().getSession().setAttribute("monkey_user", userInfo);

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userInfo,
                userInfo.getPassword(), ByteSource.Util.bytes(userInfo.getSalt()),
                getName());
        return authenticationInfo;
    }
}

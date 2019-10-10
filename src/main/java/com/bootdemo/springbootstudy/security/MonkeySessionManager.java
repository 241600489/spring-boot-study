package com.bootdemo.springbootstudy.security;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class MonkeySessionManager extends DefaultWebSessionManager {

    public MonkeySessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String id = WebUtils.toHttp(httpServletRequest).getHeader( "token");
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "stateless request");
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        return id;
    }
}

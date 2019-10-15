package com.bootdemo.springbootstudy.security;

import com.bootdemo.springbootstudy.common.vo.MonkeyResult;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 *
 * @author pengxu
 * @date 2018/5/12
 */
public class ShiroPermissionCheckFilter extends PermissionsAuthorizationFilter {

    public static final String SLASH = "/";

    private Logger logger = org.apache.log4j.Logger.getLogger(ShiroPermissionCheckFilter.class);


    @Override
    public boolean isAccessAllowed(ServletRequest request,
                                   ServletResponse response, Object mappedValue) throws
            IOException {
        Subject subject = SecurityUtils.getSubject();

        HttpServletRequest req = (HttpServletRequest) request;
        //get shiro upm
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        int i = uri.indexOf(contextPath);
        if (i > -1) {
            uri = uri.substring(i + contextPath.length());
        }

        if (StringUtils.isEmpty(uri)) {
            uri = SLASH;
        }

        boolean permitted;
        if (SLASH.equals(uri)) {
            permitted = true;
        } else {
            permitted = subject.isPermitted(uri);
        }
        return permitted;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        if(isAjaxRequest((HttpServletRequest)request)){
            MonkeyResult fail = MonkeyResult.fail("没有权限");
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.println(fail);
            out.flush();
            out.close();
        }else {
            super.onAccessDenied(request,response);
        }
        return false;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader("x-requested-with");
        if (Objects.nonNull(requestHeader)) {
            return true;
        }
        return false;
    }

}
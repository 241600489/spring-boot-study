package com.bootdemo.springbootstudy.security;


import org.apache.shiro.session.Session;

public class SessionWrapper {
    private Session session;
    private long updateTime;

    public SessionWrapper(Session session) {
        this.session = session;
        this.updateTime = System.currentTimeMillis();
    }

    public Session getSession() {
        return session;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}

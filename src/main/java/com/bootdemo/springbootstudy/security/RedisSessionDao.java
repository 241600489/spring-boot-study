package com.bootdemo.springbootstudy.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class RedisSessionDao extends AbstractSessionDAO {

    private static final Map<Serializable,SessionWrapper> sessionCache=new ConcurrentHashMap<>();
    @Override
    protected Serializable doCreate(Session session) throws UnknownSessionException{
        Assert.notNull(session, "RedisSessionDao.doCreate's argument session is null");

        generateSessionForNewSession(session);

        String sessionId = session.getId().toString();
        if (sessionExist(sessionId)) {
            throw new UnknownSessionException("session " + sessionId + "already exist");
        }

        sessionCache.put(sessionId, new SessionWrapper(session));
        return sessionId;
    }

    private void generateSessionForNewSession(Session session) {
        if (Objects.isNull(session.getId())) {
            Serializable sessionId = generateSessionId(session);
            assignSessionId(session,sessionId);
        }
    }

    private boolean sessionExist(String sessionId) {
        SessionWrapper sessionWrapper = sessionCache.get(sessionId);

        if (Objects.isNull(sessionWrapper)) {
            return false;
        }

        //判断是否超过会话时间
        if (!sessionWrapper.isValid()) {
            sessionCache.remove(sessionId);
            return false;
        }

        return true;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        SessionWrapper sessionWrapper = sessionCache.get(sessionId);

        if (Objects.isNull(sessionWrapper)) {
            return null;
        }

        sessionWrapper.setUpdateTime(System.currentTimeMillis());

        return sessionWrapper.getSession();
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        generateSessionForNewSession(session);

        String sessionId = session.getId().toString();
        if (!sessionExist(sessionId)) {
            throw new UnknownSessionException("sessionId:" + sessionId + "have not  exist");
        }

        sessionCache.put(sessionId, new SessionWrapper(session));
    }

    @Override
    public void delete(Session session) {
        sessionCache.remove(session.getId().toString());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return sessionCache.values().stream()
                .filter(Objects::nonNull)
                //filter invalid
                .filter(SessionWrapper::isValid)
                .map(SessionWrapper::getSession)
                .collect(Collectors.toList());
    }

}

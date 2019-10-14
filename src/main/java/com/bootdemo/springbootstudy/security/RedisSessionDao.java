package com.bootdemo.springbootstudy.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RedisSessionDao extends AbstractSessionDAO {

    private static final Map<Serializable,SessionWrapper> sessionCache=new ConcurrentHashMap<>();
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    protected Serializable doCreate(Session session) throws UnknownSessionException{
        Assert.notNull(session, "RedisSessionDao.doCreate's argument session is null");

        generateSessionForNewSession(session);

        String sessionId = session.getId().toString();
        if (sessionExist(sessionId)) {
            throw new UnknownSessionException("session " + sessionId + "already exist");
        }

        redisTemplate.opsForValue().set(sessionId, session, 1800, TimeUnit.SECONDS);
        return sessionId;
    }

    private void generateSessionForNewSession(Session session) {
        if (Objects.isNull(session.getId())) {
            Serializable sessionId = generateSessionId(session);
            assignSessionId(session,sessionId);
        }
    }

    private boolean sessionExist( String sessionId) {
        Assert.notNull(sessionId, "RedisSessionDao.sessionExist's argument can not be null");
        return redisTemplate.hasKey(sessionId);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Object obj = redisTemplate.opsForValue().get(sessionId);
        if (obj == null) {
            return null;
        }
        redisTemplate.expire(sessionId.toString(), 1800, TimeUnit.SECONDS);
        return (Session) obj;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        generateSessionForNewSession(session);

        String sessionId = session.getId().toString();
        if (!sessionExist(sessionId)) {
            throw new UnknownSessionException("sessionId:" + sessionId + "have not  exist");
        }

        redisTemplate.opsForValue().set(sessionId,session, 1800, TimeUnit.SECONDS);
    }

    @Override
    public void delete(Session session) {
        redisTemplate.delete(session.getId().toString());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return Collections.EMPTY_LIST;
    }

}

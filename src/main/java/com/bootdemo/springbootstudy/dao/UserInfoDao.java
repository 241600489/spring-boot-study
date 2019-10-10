package com.bootdemo.springbootstudy.dao;

import com.bootdemo.springbootstudy.repostitry.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao extends CrudRepository<UserInfo,Long> {
    UserInfo findUserInfoByUsername(String userName);
}

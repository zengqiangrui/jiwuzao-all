package com.kauuze.manager.domain.mongo.repository;

import com.kauuze.manager.domain.mongo.entity.userBastic.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-07 22:10
 */
@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo,String> {
    List<UserInfo> findByUidIn(List<Integer> uids);
    UserInfo findByUid(int uid);
    UserInfo findByNickName(String nickName);
    Page<UserInfo> findByNickNameLike(String nickName, Pageable pageable);
    Page<UserInfo> findByCreateTimeLessThanEqualAndNickNameLike(Long createTime, String nickName, Pageable pageable);
}

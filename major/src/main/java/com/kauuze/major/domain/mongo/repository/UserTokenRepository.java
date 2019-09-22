package com.kauuze.major.domain.mongo.repository;


import com.jiwuzao.common.domain.enumType.BackRoleEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-15 02:11
 */
@Repository
public interface UserTokenRepository extends MongoRepository<UserToken,String> {
    UserToken findByAccessToken(String accessToken);
    UserToken findByUid(int uid);

    List<UserToken> findAllByBackRole(BackRoleEnum backRole);
}

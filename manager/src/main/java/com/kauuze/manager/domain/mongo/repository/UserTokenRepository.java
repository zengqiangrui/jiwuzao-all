package com.kauuze.manager.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.BackRoleEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-15 02:11
 */
@Repository
public interface UserTokenRepository extends MongoRepository<UserToken,String> {
    List<UserToken> findByBackRole(BackRoleEnum backRoleEnum);

    @Query("{'backRole': {$in:['root', 'cms']}}")
    Page<UserToken> findCms(Pageable pageable);
    UserToken findByAccessToken(String accessToken);
    UserToken findByUid(int uid);
}

package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import com.jiwuzao.common.domain.mongo.entity.ReceiverAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressRepository extends MongoRepository<ReceiverAddress, String> {
    List<ReceiverAddress> findAllByUid(Integer uid);

//    Optional<ReceiverAddress> findByUidAndAddressStatus(Integer uid,AddressEnum addressEnum);

    /**
     * 查询用户不是某状态的所有地址
     * @param uid
     * @param addressEnum
     * @return
     */
    List<ReceiverAddress> findAllByUidAndAddressStatusIsNot(Integer uid,AddressEnum addressEnum);

    List<ReceiverAddress> findAllByUidAndAddressStatus(Integer uid, AddressEnum addressEnum);


}

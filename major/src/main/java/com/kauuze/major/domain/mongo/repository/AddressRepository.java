package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import com.jiwuzao.common.domain.mongo.entity.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressRepository extends MongoRepository<Address, String> {
    List<Address> findAllByUid(Integer uid);

//    Optional<ReceiverAddress> findByUidAndAddressStatus(Integer uid,AddressEnum addressEnum);

    /**
     * 查询用户不是某状态的所有地址
     * @param uid
     * @param addressEnum
     * @return
     */
    List<Address> findAllByUidAndAddressStatusIsNot(Integer uid, AddressEnum addressEnum);

    List<Address> findAllByUidAndAddressStatus(Integer uid, AddressEnum addressEnum);


}

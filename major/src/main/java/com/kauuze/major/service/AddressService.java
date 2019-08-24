package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import com.jiwuzao.common.domain.mongo.entity.Address;
import com.jiwuzao.common.domain.mongo.entity.SenderAddress;
import com.kauuze.major.domain.mongo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    /**
     * 用户增加地址
     *
     * @param uid
     * @param provinces
     * @param defaultStatus
     * @param defaultStatus
     * @param trueName
     * @return
     */
    public Address addAddress(int uid, String provinces, String addressDetail, String phone, String trueName, AddressEnum defaultStatus) {
        //查询用户所有未被删除的地址
        List<Address> list = addressRepository.findAllByUidAndAddressStatusIsNot(uid, AddressEnum.DELETE);
        Address address = new Address(null, uid, provinces, addressDetail, phone, trueName, null, System.currentTimeMillis(), null);
        if (list.isEmpty()) {
            //如果是初次添加就是添加默认的地址
            address.setAddressStatus(AddressEnum.DEFAULT);
        } else {
            //否则是普通地址
            address.setAddressStatus(AddressEnum.USUAL);
        }
        return addressRepository.save(address);
    }

    /**
     * 用户删除地址
     *
     * @param addressId
     * @return
     */
    public Address userDelById(String addressId) {
        Optional<Address> opt = addressRepository.findById(addressId);
        if (opt.isPresent()) {
            Address address = opt.get();
            address.setAddressStatus(AddressEnum.DELETE).setUpdateTime(System.currentTimeMillis());
            return addressRepository.save(address);
        } else {
            throw new RuntimeException("没找到地址");
        }

    }

    /**
     * 用户设置默认地址
     *
     * @param uid
     * @param addressId
     * @return
     */
    public Address setDefaultAddress(int uid, String addressId) {
        //查询用户所有未被删除的地址
        List<Address> list = getAllByUserNotDel(uid);
        if (list.isEmpty()) {
            throw new RuntimeException("没找到地址");
        }
        int reIdx = 0;
        for (Address address : list) {
            int idx = list.indexOf(address);
            if (address.getId().equals(addressId)) {
                address.setAddressStatus(AddressEnum.DEFAULT).setUpdateTime(System.currentTimeMillis());
                reIdx = idx;
            }
            if (address.getAddressStatus() == AddressEnum.DEFAULT) {
                address.setAddressStatus(AddressEnum.USUAL).setUpdateTime(System.currentTimeMillis());
            }
            list.set(idx, address);
        }
        addressRepository.saveAll(list);
        return list.get(reIdx);
    }

    /**
     * 查询用户所有未被删除的地址
     *
     * @param uid
     * @return
     */
    public List<Address> getAllByUserNotDel(int uid) {
        return addressRepository.findAllByUidAndAddressStatusIsNot(uid, AddressEnum.DELETE);
    }

    /**
     * 查询用户默认地址
     *
     * @param uid
     * @return
     */
    public List<Address> getAddressByDelault(int uid) {
        List<Address> list = addressRepository.findAllByUidAndAddressStatus(uid, AddressEnum.DEFAULT);
        for (Address address : list) {
            System.out.println(address);
        }
        return list;
    }

    /**
     * 修改用户地址
     *
     * @param addressInfo
     * @return
     */
    public void saveAddress(@RequestAttribute int uid, @RequestBody Address addressInfo) {
        List<Address> allByUid = addressRepository.findAllByUid(uid);

        for (Address address : allByUid) {
            System.out.println(address);
            System.out.println(address.getId().equals(addressInfo.getId()));;
            if(address.getId().equals(addressInfo.getId())){
                addressInfo.setCreateTime(address.getCreateTime());
                addressInfo.setUpdateTime(System.currentTimeMillis());
                addressInfo.setUid(uid);

                addressRepository.save(addressInfo);
            }
        }


    }

}



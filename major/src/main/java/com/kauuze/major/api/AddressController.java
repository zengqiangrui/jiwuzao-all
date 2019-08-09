package com.kauuze.major.api;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import com.jiwuzao.common.domain.mongo.entity.Address;
import com.jiwuzao.common.pojo.address.AddressIdPojo;
import com.jiwuzao.common.pojo.address.AddressPojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.include.JsonResult;
import com.kauuze.major.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @RequestMapping("/addUserAddress")
    @Authorization
    public JsonResult addUserAddress(@RequestAttribute int uid, @Valid @RequestBody AddressPojo addressPojo) {
        Address address = addressService.addAddress(uid, addressPojo.getProvinces(), addressPojo.getAddressDetail(), addressPojo.getPhone(), addressPojo.getTrueName(), addressPojo.getAddressStatus());
        if (null != address) {
            return JsonResult.success(address);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/defaultAddress")
    @Authorization
    public JsonResult defaultAddress(@RequestAttribute int uid) {
        System.out.println("defaultAddress" + uid);
        List<Address> listAddress = addressService.getAddressByDelault(uid);
        if(listAddress.size() == 0){
            return JsonResult.success();
        }
        return JsonResult.success(listAddress.get(0));
    }

    @RequestMapping("/undeleteAddress")
    @Authorization
    public JsonResult undeleteAddress(@RequestAttribute int uid) {
        System.out.println("undeleteAddress" + uid);
        List<Address> allByUserNotDel = addressService.getAllByUserNotDel(uid);
        if(allByUserNotDel!=null){
            return JsonResult.success(allByUserNotDel);
        }
        return JsonResult.failure();
    }

    @RequestMapping("/senderAddress")
    @Merchant
    public JsonResult senderAddress(@RequestAttribute int uid) {
        List<Address> allByUserNotDel = addressService.getAllByUserNotDel(uid);
        if(allByUserNotDel!=null){
            return JsonResult.success(allByUserNotDel);
        }
        return JsonResult.failure();
    }

    @RequestMapping("/deleteAddress")
    @Authorization
    public JsonResult deleteAddress(@RequestAttribute int uid, @RequestBody AddressIdPojo addressPojo) {
        addressService.userDelById(addressPojo.getAddressId());
        System.out.println("addressId" + addressPojo.getAddressId());
        //如果删除的是缓存中的地址，则返回一个默认地址回去，以显示
        List<Address> listAddress = addressService.getAddressByDelault(uid);
        for (Address address : listAddress) {
            System.out.println(address);
        }
        return JsonResult.success(listAddress);
    }

    @RequestMapping("/saveUserAddress")
    @Authorization
    private JsonResult saveUserAddress(@RequestAttribute int uid, @RequestBody Address recAddress) {
        List<Address> defaultAddress = addressService.getAddressByDelault(uid);
        for (Address deAddress : defaultAddress) {
            if (deAddress != null) {
                deAddress.setAddressStatus(AddressEnum.USUAL);
                addressService.saveAddress(uid, deAddress);
            }
        }
        addressService.saveAddress(uid, recAddress);
        //保存现有地址外，还应将原默认地址设为usual
        return  JsonResult.success();
    }
}

package com.kauuze.major.api;

import com.jiwuzao.common.domain.mongo.entity.ReceiverAddress;
import com.jiwuzao.common.pojo.userBasic.AddressPojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.include.JsonResult;
import com.kauuze.major.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @RequestMapping("/addUserAddress")
    @Authorization
    public JsonResult addUserAddress(@RequestAttribute int uid, @RequestBody AddressPojo addressPojo) {
        ReceiverAddress address = addressService.addAddress(uid, addressPojo.getReceiveProvinces(), addressPojo.getReceiverAddress(), addressPojo.getReceiverPhone(), addressPojo.getReceiverTrueName());
        if (null != address) {
            return JsonResult.success(address);
        } else {
            return JsonResult.failure();
        }
    }

//    @RequestMapping("/delteUserAddress")
//    @Authorization
//    private JsonResult delUserAddress()
}

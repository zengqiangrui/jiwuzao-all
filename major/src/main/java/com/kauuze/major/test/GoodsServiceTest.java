package com.kauuze.major.test;


import com.jiwuzao.common.domain.enumType.AddressEnum;
import com.jiwuzao.common.domain.mongo.entity.Category;
import com.jiwuzao.common.domain.mongo.entity.ReceiverAddress;
import com.kauuze.major.domain.mongo.entity.Goods;
import com.kauuze.major.domain.mongo.repository.AddressRepository;
import com.kauuze.major.domain.mongo.repository.CategoryRepository;
import com.kauuze.major.include.StateModel;
import com.kauuze.major.service.AddressService;
import com.kauuze.major.service.GoodsService;
import com.kauuze.major.service.UserBasicService;
import com.kauuze.major.service.dto.goods.GoodsOpenDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserBasicService userBasicService;


    @Test
    public void showGoods() {
        System.out.println(goodsService.getGoodsOpenDto("5d2bd7a92197fd1f143895f4"));
    }

    @Test
    public void showPage() {


    }

    @Test
    public void getAllByUserNotDel(){
//        List<ReceiverAddress> list = addressRepository.findAllByUidAndAddressStatusIsNot(3, AddressEnum.DEFAULT);
        List<ReceiverAddress> list = addressService.getAllByUserNotDel(7);
        for (ReceiverAddress receiverAddress : list) {
            System.out.println(receiverAddress);
        }

    }

    @Test
    public void add(){
        ReceiverAddress receiverAddress = addressService.addAddress(7, "福建", "厦门", "18223327253", "陈", AddressEnum.DEFAULT);
        System.out.println(receiverAddress);
    }

    @Test
    public void defaultAddress(){
        List<ReceiverAddress> addressByDelault = addressService.getAddressByDelault(7);
        for (ReceiverAddress address : addressByDelault) {
            System.out.println(address);
        }

    }

    @Test
    public void register(){
         int msCode=userBasicService.sendSms("18223327253");
        StateModel register = userBasicService.register("18223327253", "qq1365070580", "LiHua", msCode);
        System.out.println(register);
    }

    @Test
public void saveUserAddress() {
        //保存现有地址外，还应将原默认地址设为usual
        List<ReceiverAddress> defaultAddress = addressService.getAddressByDelault(7);
        for (ReceiverAddress deAddress : defaultAddress) {
            if(deAddress!=null){
                deAddress.setAddressStatus(AddressEnum.USUAL);
                addressService.saveAddress(7,deAddress);
            }
        }
        ReceiverAddress recAddress=new ReceiverAddress();
        recAddress.setAddressStatus(AddressEnum.DEFAULT);
        recAddress.setReceiverTrueName("曾强");
        recAddress.setId("5d36c260a3276308c41bad4e");
    System.out.println("saveUserAddress" + recAddress);
    addressService.saveAddress(7, recAddress);

}
}

package com.kauuze.major.api;

import com.jiwuzao.common.dto.shopCart.ShopCartDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import com.jiwuzao.common.pojo.shopcart.DelItemPojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shopcart")
@Slf4j
public class ShopCartController {
    @Autowired
    ShopCartService shopCartService;
    /**
     * 商品上架
     *
     * @param uid
     * @param addItemPojo
     * @return
     */
    @RequestMapping("/addItem")
    @Authorization
    public JsonResult addItem(@RequestAttribute int uid, @Valid @RequestBody AddItemPojo addItemPojo) {
        String result = shopCartService.addItem(uid, addItemPojo.getGid(), addItemPojo.getSpecId(), addItemPojo.getNum());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    @RequestMapping("/getItems")
    @Authorization
    public JsonResult getItems(@RequestAttribute int uid){
        List<ShopCartDto> result = shopCartService.getItems(uid);
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    @RequestMapping("/delItems")
    @Authorization
    public JsonResult delItems(@RequestAttribute int uid, @Valid @RequestBody DelItemPojo delItemPojo) {
        String result = shopCartService.delItems(delItemPojo.getCidList());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }
}

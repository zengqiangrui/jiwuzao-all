package com.kauuze.major.api;

import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.order.GenOrderPojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/genOrder")
    @Authorization
    public JsonResult genOrder(@RequestAttribute int uid, @Valid @RequestBody GenOrderPojo genOrderPojo){
        String result = orderService.genOrder(uid, genOrderPojo.getItemList(),
                genOrderPojo.getCity(), genOrderPojo.getAddress(),
                genOrderPojo.getPhone(),genOrderPojo.getTrueName());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }
}

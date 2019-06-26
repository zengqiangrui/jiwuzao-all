package com.kauuze.order.service;

import com.kauuze.order.domain.enumType.PayChannelEnum;
import com.kauuze.order.domain.mongo.entity.SystemGoods;
import com.kauuze.order.domain.mongo.repository.SystemGoodsRepository;
import com.kauuze.order.domain.mysql.entity.PayOrder;
import com.kauuze.order.domain.mysql.entity.User;
import com.kauuze.order.domain.mysql.repository.PayOrderRepository;
import com.kauuze.order.domain.mysql.repository.UserRepository;
import com.kauuze.order.domain.mysql.repository.WithdrawOrderRepository;
import com.kauuze.order.include.yun.WxPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 13:28
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class CallBackNoticeService {
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemGoodsRepository systemGoodsRepository;
    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;
    /**
     * 系统商品微信支付订单的回调通知
     */
    public void systemGoodsWxNotice(String payOrderNo){
        PayOrder payOrder = payOrderRepository.findByPayOrderNo(payOrderNo);
        if(payOrder == null || !payOrder.getSystemGoods() ){
            return;
        }
        //锁订单防重通知
        payOrder = payOrderRepository.findByIdForUpdate(payOrder.getId());
        if(payOrder.getPay()){
            return;
        }
        Map<String,String> map = WxPayUtil.queryOrder(payOrderNo);
        if(map == null){
            return;
        }
        String transactionId = map.get("transactionId");
        if(payOrder.getPay()){
            return;
        }
        payOrder.setPay(true);
        payOrder.setPayTime(System.currentTimeMillis());
        payOrder.setTransactionId(transactionId);
        payOrder.setPayChannel(PayChannelEnum.wxPay);
        payOrderRepository.save(payOrder);
        SystemGoods systemGoods = systemGoodsRepository.findById(payOrder.getSystemGoodsId()).get();
        Integer uid = payOrder.getUid();
        switch (systemGoods.getName()){
            case deposit:
                User user = userRepository.findByIdForUpdate(uid);
                if(user == null){
                    return;
                }
                user.setDeposit(user.getDeposit().add(payOrder.getFinalPay()));
                userRepository.save(user);
            default:
                break;
        }
    }
}

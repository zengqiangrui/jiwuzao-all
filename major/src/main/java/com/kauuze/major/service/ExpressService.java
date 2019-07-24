package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.Express;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.dto.order.ExpressRequestDataDto;
import com.jiwuzao.common.dto.order.ExpressRequestMainDto;
import com.jiwuzao.common.dto.order.ExpressResultDto;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.include.yun.KdniaoUtil;
import com.kauuze.major.config.contain.properties.KdniaoProperties;
import com.kauuze.major.domain.mongo.repository.ExpressRepository;
import com.kauuze.major.domain.mongo.repository.GoodsSpecRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ExpressService {

    @Autowired
    private KdniaoProperties properties;
    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsSpecRepository goodsSpecRepository;

    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    public ExpressResultDto getOrderTracesByJson(String expCode, String expNo, String orderCode) throws Exception {
        KdniaoUtil kdniaoUtil = new KdniaoUtil();
        String requestData = "{'OrderCode':'" + orderCode + "','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdniaoUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getEBusinessID());
        params.put("RequestType", "1002");
        String dataSign = kdniaoUtil.encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", kdniaoUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = kdniaoUtil.sendPost(properties.getTraceUrl(), params);
        log.info("kdniaoTraceResult", result);
        return JsonUtil.parseJsonString(result, ExpressResultDto.class);
    }

    public String orderTracesSubByJson(ExpressRequestDataDto requestDataDto,String expressOrder) throws Exception {
        KdniaoUtil kdniaoUtil = new KdniaoUtil();
        //todo 传入收货人和发货人信息
        String requestData = JsonUtil.toJsonString(requestDataDto);
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdniaoUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getEBusinessID());
        params.put("RequestType", "1008");
        String dataSign = kdniaoUtil.encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", kdniaoUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = kdniaoUtil.sendPost(properties.getSubscriptionUrl(), params);
        return result;
    }

    /**
     * 根据快递公司编号获取快递公司
     *
     * @param expCode
     * @return
     */
    public Optional<Express> getOneByExpCode(String expCode) {
        return expressRepository.findByCode(expCode.toUpperCase());
    }

    /**
     * 发货一件商品：业务流程是商家在快递公司进行发货，完成后对于发货的商品输入快递公司编码和快递单号
     *
     * @param expCode
     * @param orderNo
     * @param expNo
     * @return
     */
    public GoodsOrder addExpressOrder(String expCode, String expNo, String orderNo) {
        Optional<Express> code = expressRepository.findByCode(expCode);
        if (!code.isPresent()) {
            throw new OrderException(OrderExceptionEnum.NOT_SUPPORT_EXPRESS_CODE);
        }
        try {
            if (getOrderTracesByJson(expCode, expNo, orderNo).getSuccess()) {
                Optional<GoodsOrder> orderOptional = goodsOrderRepository.findByGoodsOrderNo(orderNo);
                if (!orderOptional.isPresent()) {
                    throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
                } else {
                    GoodsOrder goodsOrder = orderOptional.get();
                    if (goodsOrder.getOrderStatus() != OrderStatusEnum.waitDeliver || goodsOrder.getOrderExStatus() != OrderExStatusEnum.normal)
                        throw new OrderException(OrderExceptionEnum.EXCEPTION_ORDER);
                    //查找商品库存（规格信息）
                    if (StringUtil.isBlank(goodsOrder.getGsid())) {
                        throw new OrderException(OrderExceptionEnum.SPEC_NOT_FOUND);//规格信息没有找到
                    }
                    Optional<GoodsSpec> specOptional = goodsSpecRepository.findById(goodsOrder.getGsid());
                    if (!specOptional.isPresent()) {
                        throw new OrderException(OrderExceptionEnum.SPEC_NOT_FOUND);
                    }
                    GoodsSpec goodsSpec = specOptional.get();
                    //扣除库存
                    if (goodsSpec.getSpecInventory() < goodsOrder.getBuyCount()) {
                        throw new OrderException(OrderExceptionEnum.NOT_ENOUGH_STOCK);
                    }
                    goodsSpec.setSpecInventory(goodsSpec.getSpecInventory() - goodsOrder.getBuyCount());
                    GoodsSpec save = goodsSpecRepository.save(goodsSpec);
                    if (null == save) {
                        throw new OrderException(OrderExceptionEnum.DEDUCTION_STOCK_ERROR);
                    }
                    goodsOrder.setDeliverTime(System.currentTimeMillis())//用户系统处理快递时间
                            .setOrderStatus(OrderStatusEnum.waitReceive);//更改订单状态为待收货
                    return goodsOrderRepository.save(goodsOrder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.ExpressEnum;
import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.*;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import com.jiwuzao.common.dto.express.*;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.include.yun.KdniaoUtil;
import com.jiwuzao.common.pojo.express.ExpressPushDataPojo;
import com.jiwuzao.common.pojo.express.ExpressPushPojo;
import com.kauuze.major.config.contain.properties.KdniaoProperties;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Transactional
public class ExpressService {

    @Autowired
    private KdniaoProperties properties;
    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private GoodsSpecRepository goodsSpecRepository;
    @Autowired
    private ExpressResultRepository resultRepository;
    @Autowired
    private AddressRepository addressRepository;

    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    public ExpressResult getOrderTracesByJson(String expCode, String expNo, String orderCode) throws Exception {
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
        //获取并转换对象
        ExpressResultDto expressResultDto = JsonUtil.parseJsonString(result, ExpressResultDto.class);
        Optional<ExpressResult> byOrderCode = resultRepository.findByOrderCode(orderCode);
        ExpressResult expressResult = byOrderCode.orElse(new ExpressResult());
        BeanUtils.copyProperties(expressResultDto, expressResult);
        log.info("快递物流信息expressResult", expressResult);
        //保存或更新快递信息
        return resultRepository.save(expressResult);
    }

    /**
     * 物流轨迹订阅
     *
     * @param expCode
     * @param expNo
     * @param orderCode
     * @return
     * @throws Exception
     */
    public ExpressRequestReturnDto orderTracesSubByJson(String expCode, String expNo, String orderCode, String addressId) throws Exception {
        Optional<GoodsOrderDetail> byGoodsOrderNo = goodsOrderDetailRepository.findByGoodsOrderNo(orderCode);//查找订单详情
        if (!byGoodsOrderNo.isPresent()) {
            throw new OrderException(OrderExceptionEnum.ORDER_DETAIL_NOT_FOUND);
        }
        GoodsOrderDetail goodsOrderDetail = byGoodsOrderNo.get();
        String[] provinces = goodsOrderDetail.getReceiverCity().split("-");//省市区分隔
        ExpressUserDto expressUserDto = new ExpressUserDto().
                setProvinceName(provinces[0]).setCityName(provinces[1]).setExpAreaName(provinces[2]).setAddress(goodsOrderDetail.getReceiverAddress())
                .setMobile(goodsOrderDetail.getReceiverPhone()).setName(goodsOrderDetail.getReceiverTrueName());
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if (!addressOptional.isPresent()) {
            throw new RuntimeException("寄件人务必完善地址信息");
        }
        Address address = addressOptional.get();
        String[] split = address.getProvinces().split("-");
        ExpressUserDto sender = new ExpressUserDto().setProvinceName(split[0]).setCityName(split[1]).setExpAreaName(split[2]).setAddress(address.getAddressDetail())
                .setName(address.getTrueName()).setMobile(address.getPhone()).setTel(address.getPhone());//电话还是暂定手机号
        ExpressRequestDataDto requestDataDto = new ExpressRequestDataDto()
                .setShipperCode(expCode).setLogisticCode(expNo).setReceiver(expressUserDto).setSender(sender);
        String requestData = JsonUtil.toJsonString(requestDataDto);
        KdniaoUtil kdniaoUtil = new KdniaoUtil();
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdniaoUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getEBusinessID());
        params.put("RequestType", "1008");
        String dataSign = kdniaoUtil.encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", kdniaoUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String s = kdniaoUtil.sendPost(properties.getSubscriptionUrl(), params);
        return JsonUtil.parseJsonString(s, ExpressRequestReturnDto.class);
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
    public GoodsOrder addExpressOrder(String expCode, String expNo, String orderNo, Boolean isSub) {
        Optional<Express> code = expressRepository.findByCode(expCode);
        if (!code.isPresent()) {
            throw new OrderException(OrderExceptionEnum.NOT_SUPPORT_EXPRESS_CODE);
        }
        try {
            Optional<GoodsOrderDetail> byGoodsOrderNo = goodsOrderDetailRepository.findByGoodsOrderNo(orderNo);
            if (!byGoodsOrderNo.isPresent()) {
                throw new OrderException(OrderExceptionEnum.DELIVER_FAIL);
            } else {
                GoodsOrderDetail goodsOrderDetail = byGoodsOrderNo.get();
                goodsOrderDetail.setExpressNo(expNo).setExpCode(expCode);
                if (!isSub) {//先处理订阅失败
                    //首先查询物流信息是否追踪到
                    if (getOrderTracesByJson(expCode, expNo, orderNo).getSuccess()) {
                        goodsOrderDetailRepository.save(goodsOrderDetail.setIsSubscribe(false));
                        return createDeliver(orderNo);
                    } else {
                        //未找到信息则发货失败
                        throw new OrderException(OrderExceptionEnum.DELIVER_FAIL);
                    }
                } else {//订阅成功
                    goodsOrderDetailRepository.save(goodsOrderDetail.setIsSubscribe(true));
                    return createDeliver(orderNo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成发货订单
     *
     * @param orderNo
     * @return
     */
    private GoodsOrder createDeliver(String orderNo) {
        Optional<GoodsOrder> orderOptional = goodsOrderRepository.findByGoodsOrderNo(orderNo);
        if (!orderOptional.isPresent()) {
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        } else {
            GoodsOrder goodsOrder = orderOptional.get();
            if (goodsOrder.getOrderStatus() != OrderStatusEnum.waitDeliver)
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
            goodsSpec.setSpecInventory(goodsSpec.getSpecInventory() - goodsOrder.getBuyCount());
            GoodsSpec save = goodsSpecRepository.save(goodsSpec);
            if (null == save) {
                throw new OrderException(OrderExceptionEnum.DEDUCTION_STOCK_ERROR);
            }
            if (save.getSpecInventory() < 0) {
                throw new OrderException(OrderExceptionEnum.NOT_ENOUGH_STOCK);
            }
            goodsOrder.setDeliverTime(System.currentTimeMillis())//用户系统处理快递时间
                    .setOrderStatus(OrderStatusEnum.waitReceive);//更改订单状态为待收货
            return goodsOrderRepository.save(goodsOrder);
        }
    }


    /**
     * 根据单一订单查询物流轨迹
     *
     * @param goodsOrderNo
     * @return
     */
    public ExpressShowDto getExpressOneByOrder(String goodsOrderNo) {
        Optional<GoodsOrderDetail> optional = goodsOrderDetailRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!optional.isPresent()) {
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        } else {
            GoodsOrderDetail goodsOrderDetail = optional.get();
            return getExpressOneByLogistic(goodsOrderDetail.getExpressNo());
        }
    }

    /**
     * 查询内部的数据库物流订单数据信息
     *
     * @return
     */
    public ExpressShowDto getExpressOneByLogistic(String logisticNo) {
        Optional<ExpressResult> byLogisticCode = resultRepository.findByLogisticCode(logisticNo);
        if (byLogisticCode.isPresent()) {
            ExpressResult expressResult = byLogisticCode.get();
            return new ExpressShowDto().setExpNo(expressResult.getLogisticCode()).setOrderNo(expressResult.getOrderCode()).setTraces(expressResult.getTraces());
        } else {
            throw new RuntimeException("物流信息未找到");
        }
    }

    /**
     * 处理物流回调
     *
     * @param requestData
     * @param dataSign
     * @param requestType
     * @return
     */
    public ExpressNotifySendDto handleNotify(String requestData, String dataSign, String requestType) {
        ExpressNotifySendDto notifySendDto = new ExpressNotifySendDto().setEBusinessID(properties.getEBusinessID()).setUpdateTime(new Date());
        if (StringUtil.isBlank(dataSign)) {
            log.info("未获取签名信息");
            return notifySendDto.setSuccess(false).setReason("未获取签名信息");
        }
        if (!"101".equals(requestType)) {
            log.info("不支持的请求类型");
            return notifySendDto.setSuccess(false).setReason("不支持的请求类型");
        }
        ExpressPushPojo expressPushPojo = JsonUtil.parseJsonString(requestData, ExpressPushPojo.class);
        if (expressPushPojo.getCount() > 0 && !expressPushPojo.getData().isEmpty()) {
            for (ExpressPushDataPojo dataPojo : expressPushPojo.getData()) {
                Optional<ExpressResult> expressResultOptional = resultRepository.findByLogisticCode(dataPojo.getLogisticCode());
                if (expressResultOptional.isPresent()) {
                    ExpressResult expressResult = new ExpressResult();
                    BeanUtils.copyProperties(dataPojo, expressResult);
                    expressResult.setId(expressResultOptional.get().getId());
                    ExpressResult save = resultRepository.save(expressResult);//更新物流订单信息
                    log.info("更新物流订单信息:", save);
                } else {
                    return notifySendDto.setSuccess(false).setReason("该运单物流跟踪未订阅");
                }
            }
            return notifySendDto.setSuccess(true);
        } else {
            log.info("未接收到轨迹信息");
            return notifySendDto.setSuccess(false).setReason("未接收到轨迹信息");
        }
    }

    /**
     * 根据公司类型获取所有支持快递公司信息
     *
     * @return
     */
    public List<Express> getExpressCategory(ExpressEnum expressType) {
        return expressRepository.findAllByExpressType(expressType);
    }
}

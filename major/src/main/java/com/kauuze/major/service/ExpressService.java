package com.kauuze.major.service;

import com.jiwuzao.common.domain.common.MongoUtil;
import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
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
import com.jiwuzao.common.pojo.order.ExpressPojo;
import com.jiwuzao.common.vo.order.ExpressCancelVO;
import com.kauuze.major.config.contain.properties.KdniaoProperties;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private OrderService orderService;


    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    public ExpressResult getOrderTracesByJson(String expCode, String expNo, String orderCode) throws Exception {
        if(StringUtils.isAnyBlank(expCode,expNo,orderCode)) {
            throw new RuntimeException("快递输入参数为空");
        }
        KdniaoUtil kdniaoUtil = new KdniaoUtil();
        ExpressRequestDataDto requestDataDto = new ExpressRequestDataDto().setLogisticCode(expNo).setShipperCode(expCode).setOrderCode(orderCode);
        String requestData = JsonUtil.toJsonString(requestDataDto);
        if(expCode.equals("SF")){
            GoodsOrderDetail orderDetail = orderService.getDetailByGoodsOrderNo(orderCode);
            requestData = JsonUtil.toJsonString(requestDataDto.setCustomerName(StringUtil.getPhoneLast4(orderDetail.getReceiverPhone())));
        }
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdniaoUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getEBusinessID());
        params.put("RequestType", "1002");
        String dataSign = kdniaoUtil.encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", kdniaoUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = kdniaoUtil.sendPost(properties.getTraceUrl(), params);
        log.info("kdniaoTraceResult:{}", result);
        //获取并转换对象
        ExpressResultDto expressResultDto = JsonUtil.parseJsonString(result, ExpressResultDto.class);
        Optional<ExpressResult> byOrderCode = resultRepository.findByOrderCode(orderCode);
        ExpressResult expressResult = byOrderCode.orElse(new ExpressResult());
        BeanUtils.copyProperties(expressResultDto, expressResult);
        log.info("快递物流信息expressResult:{}", expressResult);
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
     * 处理商家无法发货
     *
     * @param goodsOrderNo
     * @param reason
     */
    public ExpressCancelVO cancelExpress(String goodsOrderNo, String reason) {
        Optional<GoodsOrder> optional = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (!optional.isPresent())
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        GoodsOrder goodsOrder = optional.get();
        if (goodsOrder.getOrderStatus() != OrderStatusEnum.waitDeliver)
            throw new OrderException(OrderExceptionEnum.ORDER_STATUS_NO_FIT);
        goodsOrder.setOrderExStatus(OrderExStatusEnum.exception);
        if (null != goodsOrderRepository.save(goodsOrder)) {
            Optional<GoodsOrderDetail> byGoodsOrderNo = goodsOrderDetailRepository.findByGoodsOrderNo(goodsOrderNo);
            if (!byGoodsOrderNo.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
            GoodsOrderDetail goodsOrderDetail = byGoodsOrderNo.get().setApplyCancel(true).setApplyCancelMerchantAudit(AuditTypeEnum.wait)
                    .setApplyCancelMerchantRefuseCause(reason).setApplyCancelTime(System.currentTimeMillis());
            GoodsOrderDetail save = goodsOrderDetailRepository.save(goodsOrderDetail);
            return new ExpressCancelVO().setCreateTime(save.getCancelTime()).setGoodsOrderNo(goodsOrderNo)
                    .setReason(save.getApplyCancelMerchantRefuseCause())
                    .setType(AuditTypeEnum.wait.getMsg());
        } else {
            throw new RuntimeException("订单入库异常");
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
            Optional<Express> byCode = expressRepository.findByCode(goodsOrderDetail.getExpCode());
            if(!byCode.isPresent()) throw new RuntimeException("不支持快递类型");
            Express express = byCode.get();
            ExpressShowDto expressShowDto = new ExpressShowDto().setOrderNo(goodsOrderNo).setExpCompany(express.getName()).setExpNo(goodsOrderDetail.getExpressNo());
//            return getExpressOneByLogistic(goodsOrderDetail.getExpressNo());
            try {
                ExpressResult orderTracesByJson = getOrderTracesByJson(goodsOrderDetail.getExpCode(), goodsOrderDetail.getExpressNo(), goodsOrderNo);
                Collections.reverse(orderTracesByJson.getTraces());
                expressShowDto.setTraces(orderTracesByJson.getTraces());
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("showDTO:{}",expressShowDto);
            return expressShowDto;
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
                Optional<GoodsOrderDetail> goodsOrderDetail = goodsOrderDetailRepository.findByExpressNo(dataPojo.getLogisticCode()).filter(GoodsOrderDetail::getIsSubscribe);
                if (!goodsOrderDetail.isPresent() || !goodsOrderDetail.get().getIsSubscribe()) continue;
                Optional<ExpressResult> expressResultOptional = resultRepository.findByLogisticCode(dataPojo.getLogisticCode());
                ExpressResult expressResult = new ExpressResult();
                if (!expressResultOptional.isPresent()) {
                    BeanUtils.copyProperties(dataPojo, expressResult);
                } else {
                    expressResult = expressResultOptional.get();
                    BeanUtils.copyProperties(dataPojo, expressResult, "id");
                }
                ExpressResult save = resultRepository.save(expressResult);//更新物流订单信息
                if (userReceiveGoods(save)) {
                    log.info("用户成功收货:{}", save);
//                    orderService.takeOver(dataPojo.getLogisticCode(),OrderStatusEnum.waitAppraise);
                }
            }
            return notifySendDto.setSuccess(true);
        } else {
            log.info("未接收到轨迹信息");
            return notifySendDto.setSuccess(false).setReason("未接收到轨迹信息");
        }
    }

    /**
     * 处理用户签收信息
     * 用户收货
     *
     * @param result
     * @return
     */
    private Boolean userReceiveGoods(ExpressResult result) {
        if (StringUtil.isBlank(result.getState()))
            return false;
        if (!"3".equals(result.getState())) {//如果未收货
            return false;
        } else {//处理已收货
            if (StringUtil.isBlank(result.getLogisticCode())) {
                return false;
            } else {
                //根据物流单号获取订单详情
                Optional<GoodsOrderDetail> opt = goodsOrderDetailRepository.findByExpressNo(result.getLogisticCode());
                if (!opt.isPresent())//如果没有，有可能是商家未发货或者用户未支付
                    return false;
                GoodsOrderDetail goodsOrderDetail = opt.get();
                Optional<GoodsOrder> byGoodsOrderNo = goodsOrderRepository.findByGoodsOrderNo(goodsOrderDetail.getGoodsOrderNo());
                if (!byGoodsOrderNo.isPresent())
                    return false;
                GoodsOrder goodsOrder = byGoodsOrderNo.get();
                GoodsOrder save = goodsOrderRepository.save(goodsOrder.setTakeTime(System.currentTimeMillis()).setOrderStatus(OrderStatusEnum.waitAppraise));
                return null != save;
            }
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

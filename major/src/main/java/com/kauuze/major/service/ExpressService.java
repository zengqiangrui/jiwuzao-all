package com.kauuze.major.service;

import com.jiwuzao.common.domain.mongo.entity.Express;
import com.jiwuzao.common.dto.order.ExpressResultDto;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.yun.KdniaoUtil;
import com.kauuze.major.config.contain.KdniaoProperties;
import com.kauuze.major.domain.mongo.repository.ExpressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ExpressService {

    @Autowired
    private KdniaoProperties properties;
    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;
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
        log.info("kdniaoResult", result);
        //根据公司业务处理返回的信息......
        return JsonUtil.parseJsonString(result, ExpressResultDto.class);
    }

    /**
     * 根据快递公司编号获取快递公司
     * @param expCode
     * @return
     */
    public Optional<Express> getOneByExpCode(String expCode){
        return expressRepository.findByCode(expCode);
    }

    public GoodsOrderDto addExpressOrder(int uid, String expCode, String orderId, String expNo){
        try {
            if(getOrderTracesByJson(expCode,expNo,orderId).getSuccess()){
            //todo
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

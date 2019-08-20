package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.ReceiptEnum;
import com.jiwuzao.common.domain.mysql.entity.Receipt;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.ReceiptRepository;
import com.kauuze.major.include.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;

    /**
     * 生成一张发票，一张发票对应一个订单
     * @param goodsOrder
     * @param name
     * @param type
     * @param taxId
     * @return
     */
    public Receipt createOne(GoodsOrder goodsOrder, String name, ReceiptEnum type, String taxId) {
        Optional<Receipt> opt = receiptRepository.findByGoodsOrderNo(goodsOrder.getGoodsOrderNo());
        if (opt.isPresent())
            throw new OrderException(OrderExceptionEnum.HAS_RECEIPT);
        Receipt receipt = new Receipt().setName(name).setType(type).setCreateTime(System.currentTimeMillis()).setGoodsOrderNo(goodsOrder.getGoodsOrderNo())
                .setGoodsDescription("商品名:" + goodsOrder.getGoodsTitle() + ",规格:" + goodsOrder.getSpecClass());
        if (type == ReceiptEnum.COMPANY) {
            if (StringUtil.isBlank(taxId)) {
                throw new RuntimeException("公司必填纳税人编号");
            }
            receipt.setTaxId(taxId);
        }
        log.info("receipt:{}",receipt);
        return receiptRepository.save(receipt);
    }

    /**
     * 获取一张发票
     * @param goodsOrderNo
     * @return
     */
    public Receipt getOne(String goodsOrderNo) {
        return receiptRepository.findByGoodsOrderNo(goodsOrderNo).orElse(null);
    }

}

package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.enumType.OpeningBankEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.enumType.WithdrawStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mongo.entity.userBastic.VerifyActor;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.domain.mysql.entity.WithdrawOrder;
import com.jiwuzao.common.exception.StoreException;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import com.jiwuzao.common.vo.store.StoreWithdrawVO;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mongo.repository.VerifyActorRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawService {

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private VerifyActorRepository verifyActorRepository;

    /**
     * 获取店铺可提现金额
     *
     * @param storeId
     * @return
     */
    public StoreWithdrawVO getWithDraw(String storeId) {
        //获取订单
        Store store = checkAndGetStore(storeId);
        List<GoodsOrder> list = goodsOrderRepository.findAllBySidAndOrderStatus(storeId, OrderStatusEnum.finish);
        BigDecimal withdrawAbleCash = store.getWithdrawCash();
        for (GoodsOrder goodsOrder : list) {
            Optional<PayOrder> byId = payOrderRepository.findById(goodsOrder.getPayid());
            if (byId.isPresent()) {
                PayOrder payOrder = byId.get();
                if (null != payOrder.getPayTime() && payOrder.getPayTime() > 0) {
                    if (System.currentTimeMillis() - payOrder.getPayTime() > 1000 * 60 * 60 * 24 * 15) {//15天内不可提现
                        withdrawAbleCash = withdrawAbleCash.add(goodsOrder.getFinalPay().multiply(new BigDecimal(0.8)));
                    }
                }
            }
        }
        store.setWithdrawCash(withdrawAbleCash);
        storeRepository.save(store);
        return new StoreWithdrawVO().setStoreId(storeId)
                .setStoreName(store.getStoreName()).setWithdrawAbleCash(store.getWithdrawCash()).setUid(store.getUid());
    }


    /**
     * 获取店铺信息
     *
     * @param storeId 店铺id
     * @return 店铺信息
     */
    private Store checkAndGetStore(String storeId) {
        Optional<Store> byId = storeRepository.findById(storeId);
        if (!byId.isPresent()) {
            throw new StoreException(StoreExceptionEnum.STORE_NOT_FOUND);//店铺为空
        }
        Store store = byId.get();
        if (store.getViolation()) {
            throw new StoreException(StoreExceptionEnum.STORE_ILLEGAL);//店铺违规
        }
        return store;
    }


    public WithdrawOrder merchantCreateCommonWithDraw(int uid, String storeId, BigDecimal remitMoney, Long bankNo, OpeningBankEnum openingBank, String remark) {
        if (remitMoney.compareTo(new BigDecimal(1000)) < 0) {//提现金额不足
            throw new StoreException(StoreExceptionEnum.STORE_REMIT_SHORTAGE);
        }
        Store store = checkAndGetStore(storeId);
        if (remitMoney.compareTo(store.getWithdrawCash()) > 0) {//超出可提现金额
            throw new StoreException(StoreExceptionEnum.STORE_REMIT_EXCEED);
        }
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if (null == verifyActor) {
            return null;
        }
        if (verifyActor.getAuditType() != AuditTypeEnum.agree) {
            throw new StoreException(StoreExceptionEnum.VERIFY_NOT_AGREE);
        }
        WithdrawOrder withdrawOrder = new WithdrawOrder().setWithdrawStatus(WithdrawStatusEnum.wait)
                .setBankNo(verifyActor.getBankNo()).setBankTrueName(verifyActor.getBankTrueName()).setOpeningBank(verifyActor.getOpeningBank())
                .setUid(uid).setRemitMoney(remitMoney).setCreateTime(System.currentTimeMillis())
                .setRemark(remark);

        return withdrawOrderRepository.save(withdrawOrder);
    }

}

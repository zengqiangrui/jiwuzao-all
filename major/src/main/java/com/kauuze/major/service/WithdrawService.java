package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.*;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mongo.entity.userBastic.VerifyActor;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.domain.mysql.entity.WithdrawOrder;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.StoreException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.vo.store.AllEarningVO;
import com.jiwuzao.common.vo.store.ManageWithdrawVO;
import com.jiwuzao.common.vo.store.StoreWithdrawVO;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mongo.repository.VerifyActorRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import com.kauuze.major.include.Rand;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.function.SingletonSupplier;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@EnableScheduling
@Slf4j
@Transactional
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

    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点扫描订单信息
    public void refreshOrderWithDrawAble() {
        goodsOrderRepository.saveAll(goodsOrderRepository.findAll().stream()
                .filter(goodsOrder -> goodsOrder.getOrderStatus() == OrderStatusEnum.finish)
                .filter(goodsOrder -> goodsOrder.getOrderExStatus() != OrderExStatusEnum.exception)//过滤异常订单
                .filter(goodsOrder -> {
                    Optional<PayOrder> opt = payOrderRepository.findById(goodsOrder.getPayid());//过滤订单未支付,或者支付时间不足15天的订单
                    return opt.isPresent() && opt.get().getPay() && (System.currentTimeMillis() - opt.get().getPayTime() > 15 * 24 * 60 * 60 * 1000);
                })
                .map(goodsOrder -> goodsOrder.setCanRemit(true).setWithdrawal(goodsOrder.getFinalPay().subtract(goodsOrder.getPostage()).multiply(new BigDecimal(0.8))))
                .collect(Collectors.toList()));

    }

    /**
     * 每日0点扫描店铺信息，更新提现上限
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshStore() {
        storeRepository.saveAll(storeRepository.findAll().stream()
                .filter(store -> !store.getViolation())
                .map(store -> store.setWithdrawNum(0))
                .collect(Collectors.toList()));
    }

    /**
     * 获取店铺可提现金额
     *
     * @param storeId
     * @return
     */
    public StoreWithdrawVO getWithDraw(String storeId) {
        //获取订单,对订单进行提现统计
        Store store = checkAndGetStore(storeId);
        //订单完成可提现
        List<GoodsOrder> list = goodsOrderRepository.findAllBySidAndOrderStatus(storeId, OrderStatusEnum.finish);
        BigDecimal withdrawAbleCash = BigDecimal.ZERO;
        for (GoodsOrder goodsOrder : list) {
            Optional<PayOrder> byId = payOrderRepository.findById(goodsOrder.getPayid());
            if (byId.isPresent() && goodsOrder.getCanRemit()) {//用户已经支付，订单完成而且可提现
                PayOrder payOrder = byId.get();
                if (null != payOrder.getPayTime() && payOrder.getPay()) {
                    withdrawAbleCash = withdrawAbleCash.add(goodsOrder.getWithdrawal());
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

    /**
     * 商家根据订单提现
     *
     * @param uid
     * @param goodsOrderNo
     * @param remark
     * @return
     */
    public WithdrawOrder merchantCreateOrderWithDraw(int uid, String goodsOrderNo, String remark) {
        VerifyActor actor = verifyActorRepository.findByUid(uid);
        if (null == actor || actor.getAuditType() != AuditTypeEnum.agree) {
            throw new RuntimeException("用户认证异常！");
        }
        Optional<GoodsOrder> opt = goodsOrderRepository.findByGoodsOrderNo(goodsOrderNo);
        if (opt.isPresent()) {
            GoodsOrder goodsOrder = opt.get();
            if (goodsOrder.getCanRemit()) {
                WithdrawOrder withdrawOrder = new WithdrawOrder()
                        .setRemark(remark).setCreateTime(System.currentTimeMillis()).setUid(uid).setWithdrawOrderNo(Rand.createOrderNo())
                        .setBankTrueName(actor.getBankTrueName()).setBankNo(actor.getBankNo()).setOpeningBank(actor.getOpeningBank())
                        .setWithdrawStatus(WithdrawStatusEnum.wait).setStoreId(goodsOrder.getSid())
                        .setRemitMoney(goodsOrder.getWithdrawal());
                return withdrawOrderRepository.save(withdrawOrder);
            } else {
                throw new OrderException(OrderExceptionEnum.CAN_NOT_REMIT);
            }
        } else {
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        }
    }


    /**
     * 商家根据金额申请提现,本质上针对每一个订单，
     * 有一个可提现金额，用户支付的*0.8
     *
     * @param uid        用户id
     * @param storeId    店铺id
     * @param remitMoney 提现金额
     * @param remark     备注
     * @return 提现申请
     */
    public WithdrawOrder merchantCreateCommonWithDraw(int uid, String storeId, BigDecimal remitMoney, String remark) {
        if (remitMoney.compareTo(new BigDecimal(1000)) < 0) {//提现金额不足1000,无法提现
            throw new StoreException(StoreExceptionEnum.STORE_REMIT_SHORTAGE);
        }
        Optional<WithdrawOrder> opt = withdrawOrderRepository.findByStoreIdAndWithdrawStatus(storeId, WithdrawStatusEnum.wait);
        if (opt.isPresent()) throw new RuntimeException("还有未处理的提现申请");//如果存在待处理的提现请求，不能继续提现
        Store store = checkAndGetStore(storeId);
        if (store.getWithdrawNum() == 3) {//一天只能提现3次
            throw new StoreException(StoreExceptionEnum.STORE_OVER_WITHDRAW);
        }
        if (remitMoney.compareTo(store.getWithdrawCash()) > 0) {//超出可提现金额
            throw new StoreException(StoreExceptionEnum.STORE_REMIT_EXCEED);
        }
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if (null == verifyActor) {
            return null;//非认证的店铺和人员
        }
        if (verifyActor.getAuditType() != AuditTypeEnum.agree) {
            throw new StoreException(StoreExceptionEnum.VERIFY_NOT_AGREE);
        }
        WithdrawOrder withdrawOrder = new WithdrawOrder().setWithdrawStatus(WithdrawStatusEnum.wait).setWithdrawOrderNo(Rand.createOrderNo())
                .setBankNo(verifyActor.getBankNo()).setBankTrueName(verifyActor.getBankTrueName()).setOpeningBank(verifyActor.getOpeningBank())
                .setUid(uid).setRemitMoney(remitMoney).setCreateTime(System.currentTimeMillis()).setStoreId(storeId)
                .setRemark(remark);
        storeRepository.save(store.setWithdrawNum(store.getWithdrawNum() + 1));
        return withdrawOrderRepository.save(withdrawOrder);
    }


    /**
     * 获取店铺提现申请列表
     *
     * @param withdrawStatus
     * @return
     */
    public List<ManageWithdrawVO> managerShowVO(WithdrawStatusEnum withdrawStatus) {
        return withdrawOrderRepository.findAllByWithdrawStatus(withdrawStatus).stream().map(withdrawOrder -> {
            ManageWithdrawVO manageWithdrawVO = new ManageWithdrawVO();
            BeanUtils.copyProperties(withdrawOrder, manageWithdrawVO);//获取申请提现的信息
            manageWithdrawVO.setStoreName(checkAndGetStore(withdrawOrder.getStoreId()).getStoreName());//获取店铺名称
            return manageWithdrawVO;
        }).collect(Collectors.toList());
    }

    /**
     * 此为财务人员确认提现的方法
     *
     * @param storeId
     * @param withdrawNo
     */
    public WithdrawOrder confirmWithDraw(String storeId, String withdrawNo) {
        checkAndGetStore(storeId);
        Optional<WithdrawOrder> optional = withdrawOrderRepository.findByWithdrawOrderNo(withdrawNo);
        if (!optional.isPresent())
            throw new StoreException(StoreExceptionEnum.STORE_NO_REMIT);
        WithdrawOrder withdrawOrder = optional.get();
        if (withdrawOrder.getWithdrawStatus() != WithdrawStatusEnum.wait) {
            throw new RuntimeException("提现请求状态异常");
        }
        withdrawOrder.setWithdrawStatus(WithdrawStatusEnum.processing).setProcessingTime(System.currentTimeMillis());//资金提出
        return withdrawOrderRepository.save(withdrawOrder);
    }

    /**
     * 处理提现是否成功
     * 成功提现的处理是将已完成的订单按照排序，依次提现处理，有剩余加入最近的一个订单上。
     *
     * @param storeId
     * @param withDrawOrderNo
     * @return
     */
    public WithdrawOrder handleWithDraw(String storeId, String withDrawOrderNo, Boolean success, String reason) {
        Store store = checkAndGetStore(storeId);
        Optional<WithdrawOrder> opt = withdrawOrderRepository.findByWithdrawOrderNo(withDrawOrderNo);
        if (!opt.isPresent())
            throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        WithdrawOrder withdrawOrder = opt.get();
        if (withdrawOrder.getWithdrawStatus() != WithdrawStatusEnum.processing)
            throw new RuntimeException("提现请求状态不是处理中");
        if (success) {//处理成功提现
            BigDecimal remitMoney = withdrawOrder.getRemitMoney();
            //获取已完成订单按照时间升序排列
            List<GoodsOrder> list = goodsOrderRepository.findAllBySidAndOrderStatusOrderByCreateTimeAsc(storeId, OrderStatusEnum.finish);
            for (GoodsOrder goodsOrder : list) {
                if (goodsOrder.getCanRemit())
                    if (remitMoney.subtract(goodsOrder.getWithdrawal()).compareTo(BigDecimal.ZERO) > 0) {
                        remitMoney = remitMoney.subtract(goodsOrder.getWithdrawal());
                        //订单成功提现并入库
                        goodsOrderRepository.save(goodsOrder.setWithdrawal(BigDecimal.ZERO).setCanRemit(false));
                    } else {
                        GoodsOrder save = goodsOrderRepository.save(goodsOrder.setWithdrawal(goodsOrder.getWithdrawal().subtract(remitMoney)));
                        log.info("成功提现的最后的订单是:{}", save);
                        break;
                    }
            }
            return withdrawOrderRepository.save(withdrawOrder.setWithdrawStatus(WithdrawStatusEnum.success).setFinishTime(System.currentTimeMillis()));
        } else {//处理失败的提现
            log.info("提现失败的处理");
            return withdrawOrderRepository.save(withdrawOrder.setWithdrawStatus(WithdrawStatusEnum.failure).setFailureReason(reason).setFinishTime(System.currentTimeMillis()));
        }
    }

    public List<WithdrawOrder> merchantShowHistory(String storeId) {
        checkAndGetStore(storeId);
        return withdrawOrderRepository.findAllByStoreIdOrderByCreateTimeDesc(storeId);//根据时间降序排列
    }

    public AllEarningVO merchantGetAllEarning(String storeId) {
        checkAndGetStore(storeId);
        BigDecimal bigDecimal = new BigDecimal(0);
        for (WithdrawOrder withdrawOrder : withdrawOrderRepository.findAllByStoreId(storeId)) {
            if (withdrawOrder.getWithdrawStatus() == WithdrawStatusEnum.success)
                bigDecimal = bigDecimal.add(withdrawOrder.getRemitMoney());
        }
        return new AllEarningVO().setAllEarning(bigDecimal).setStoreId(storeId);
    }

}

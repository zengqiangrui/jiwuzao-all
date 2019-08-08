package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 11:08
 */
@Repository
public interface GoodsOrderRepository extends JpaRepository<GoodsOrder,Integer> {
    List<GoodsOrder> findByUid(int uid);
    List<GoodsOrder> findByPayOrderNo(String pid);
    Optional<GoodsOrder> findByGoodsOrderNo(String goodsOrderNo);

    /**
     * 查找店铺中某用户的下单情况
     * @param uid 用户id
     * @param sid 店铺id
     * @return list
     */
    List<GoodsOrder> findAllByUidAndSid(int uid,String sid);

    /**
     * 查找店铺中某状态下的订单情况
     * @param sid 店铺id
     * @param orderStatus 状态枚举
     * @return list
     */
    List<GoodsOrder> findAllBySidAndOrderStatus(String sid, OrderStatusEnum orderStatus);

    /**
     * 查找某店铺所有订单
     * @param sid 店铺id
     * @return list
     */
    List<GoodsOrder> findAllBySid(String sid);

    /**
     * 分页查找店铺下所有订单
     * @param sid
     * @param pageable
     * @return
     */
    Page<GoodsOrder> findAllBySid(String sid, Pageable pageable);

    Page<GoodsOrder> findAllBySidAndOrderStatus(String sid,OrderStatusEnum orderStatus,Pageable pageable);

    /**
     * 查找某一用户，订单状态下的所有订单
     * @param uid 用户id
     * @param orderStatus 订单状态
     * @return list
     */
    List<GoodsOrder> findAllByUidAndOrderStatus(int uid, OrderStatusEnum orderStatus);
}

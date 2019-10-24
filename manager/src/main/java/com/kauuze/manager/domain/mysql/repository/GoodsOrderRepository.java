package com.kauuze.manager.domain.mysql.repository;


import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
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
public interface GoodsOrderRepository extends JpaRepository<GoodsOrder, Integer> {
    Optional<GoodsOrder> findByGoodsOrderNo(String orderNo);

    List<GoodsOrder> findBySid(String sid);

    List<GoodsOrder> findByUid(Integer uid);

    List<GoodsOrder> findByOrderExStatus(OrderExStatusEnum status);

    Page<GoodsOrder> findAllByOrderStatus(OrderStatusEnum status, Pageable pageable);

    Page<GoodsOrder> findAllBySid(String sid, Pageable pageable);

    Page<GoodsOrder> findAllByOrderExStatus(OrderExStatusEnum statusEnum,Pageable pageable);

    /**
     * 根据店铺id和订单状态进行查找
     * @param status 订单状态
     * @param sid 店铺id
     * @param pageable 分页接口
     * @return 分页对象
     */
    Page<GoodsOrder> findAllByOrderStatusAndSid(OrderStatusEnum status,String sid,Pageable pageable);

    /**
     * 根据店铺id和订单异常状态进行查找
     * @param status 订单异常状态
     * @param sid 店铺id
     * @param pageable 分页接口
     * @return 分页对象
     */
    Page<GoodsOrder> findAllByOrderExStatusAndSid(OrderExStatusEnum status,String sid,Pageable pageable);


}

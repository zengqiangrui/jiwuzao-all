package com.kauuze.manager.service;

import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.jiwuzao.common.include.PageUtil;
import com.jiwuzao.common.vo.order.OrderDetailVO;
import com.jiwuzao.common.vo.order.OrderSimpleVO;
import com.kauuze.manager.domain.mongo.repository.StoreRepository;
import com.kauuze.manager.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.manager.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.manager.domain.mysql.repository.PayOrderRepository;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.include.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class OrderViewService {
    @Resource
    private StoreRepository storeRepository;
    @Resource
    private GoodsOrderRepository goodsOrderRepository;
    @Resource
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Resource
    private PayOrderRepository payOrderRepository;

    /**
     * 查找所有订单
     *
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getOrderSimple(int num, int size) {
        Pageable pageable = PageUtil.getNewsInsert(num, size);
        Page<GoodsOrder> page = goodsOrderRepository.findAll(pageable);
        return createOrderPage(page);
    }

    /**
     * 创建分页对象
     *
     * @param page
     * @return
     */
    private PageDto<OrderSimpleVO> createOrderPage(Page<GoodsOrder> page) {
        List<OrderSimpleVO> collect = page.getContent().stream().map(goodsOrder -> {
            OrderSimpleVO vo = new OrderSimpleVO();
            BeanUtils.copyProperties(goodsOrder, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageDto<OrderSimpleVO>().setTotal(page.getTotalElements()).setContent(collect);
    }

    /**
     * 根据订单状态查
     *
     * @param status
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getSimpleOrderByStatus(OrderStatusEnum status, Integer num, Integer size) {
        Pageable pageable = PageUtil.getNewsInsert(num, size);
        Page<GoodsOrder> page = goodsOrderRepository.findAllByOrderStatus(status, pageable);
        return createOrderPage(page);
    }

    /**
     * 获取店铺全部订单分页
     *
     * @param storeId
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getSimpleOrderByStoreId(String storeId, Integer num, Integer size) {
        Pageable pageable = PageUtil.getNewsInsert(num, size);
        Page<GoodsOrder> page = goodsOrderRepository.findAllBySid(storeId, pageable);
        return createOrderPage(page);
    }

    /**
     * 分页查询全部异常订单
     *
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getSimpleExceptionOrder(Integer num, Integer size) {
        Page<GoodsOrder> page = goodsOrderRepository.findAllByOrderExStatus(OrderExStatusEnum.exception, PageUtil.getNewsInsert(num, size));
        return createOrderPage(page);
    }

    /**
     * 根据店铺名进行查找全部的订单
     *
     * @param storeName
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getSimpleOrderByStoreName(String storeName, Integer num, Integer size) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new RuntimeException("不存在该店铺");
        }
        return getSimpleOrderByStoreId(store.getId(), num, size);
    }

    /**
     * 根据店铺id和订单状态进行查询
     *
     * @param storeId
     * @param status
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getSimpleByStoreIdStatus(String storeId, OrderStatusEnum status, Integer num, Integer size) {
        if (StringUtil.isBlank(storeId)) throw new RuntimeException("店铺id为空！");
        Page<GoodsOrder> page = goodsOrderRepository.findAllByOrderStatusAndSid(status, storeId, PageUtil.getNewsInsert(num, size));
        return createOrderPage(page);
    }

    /**
     * 根据店铺名和订单状态进行查询分页订单
     *
     * @param storeName
     * @param status
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getSimpleByStoreNameStatus(String storeName, OrderStatusEnum status, Integer num, Integer size) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new RuntimeException("不存在该店铺");
        }
        return getSimpleByStoreIdStatus(store.getId(), status, num, size);
    }

    public PageDto<OrderSimpleVO> getSimpleByUidStatus(int uid,OrderStatusEnum status,Integer num,Integer size){
        Page<GoodsOrder> page = goodsOrderRepository.findAllByUidAndOrderStatus(uid, status, PageUtil.getNewsInsert(num, size));
        return createOrderPage(page);
    }

    public PageDto<OrderSimpleVO> getSimpleByUid(int uid, Integer num,Integer size){
        Page<GoodsOrder> page = goodsOrderRepository.findAllByUid(uid,PageUtil.getNewsInsert(num,size));
        return createOrderPage(page);
    }

    /**
     * 创建订单简单对象
     *
     * @param orderNo
     * @return
     */
    public OrderSimpleVO getSimpleByOrderNo(String orderNo) {
        Optional<GoodsOrder> optional = goodsOrderRepository.findByGoodsOrderNo(orderNo);
        if (!optional.isPresent()) throw new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND);
        OrderSimpleVO orderSimpleVO = new OrderSimpleVO();
        BeanUtils.copyProperties(optional.get(), orderSimpleVO);
        return orderSimpleVO;
    }

    public OrderDetailVO getDetailOrder(String orderNo) {
        GoodsOrder goodsOrder = goodsOrderRepository.findByGoodsOrderNo(orderNo).orElseThrow(() -> new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND));
        GoodsOrderDetail goodsOrderDetail = goodsOrderDetailRepository.findById(goodsOrder.getGoodsOrderDetailId()).orElseThrow(() -> new OrderException(OrderExceptionEnum.ORDER_NOT_FOUND));
        PayOrder payOrder = payOrderRepository.findById(goodsOrder.getPayid()).orElseThrow(() -> new OrderException(OrderExceptionEnum.NOT_PAID));
        Store store = storeRepository.findById(goodsOrder.getSid()).orElseThrow(() -> new RuntimeException("店铺不存在"));
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtils.copyProperties(goodsOrder, orderDetailVO);
        BeanUtils.copyProperties(goodsOrderDetail, orderDetailVO);
        BeanUtils.copyProperties(payOrder, orderDetailVO);
        BeanUtils.copyProperties(store, orderDetailVO);
        return orderDetailVO;
    }
}

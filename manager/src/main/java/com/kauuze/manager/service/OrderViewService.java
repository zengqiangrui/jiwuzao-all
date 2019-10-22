package com.kauuze.manager.service;

import com.jiwuzao.common.domain.enumType.BackRoleEnum;
import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.include.PageUtil;
import com.jiwuzao.common.vo.order.OrderSimpleVO;
import com.kauuze.manager.domain.mongo.repository.StoreRepository;
import com.kauuze.manager.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.manager.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.manager.include.PageDto;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
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
     * 获取店铺订单分页
     *
     * @param storeId
     * @param num
     * @param size
     * @return
     */
    public PageDto<OrderSimpleVO> getSimpleOrderByStore(String storeId, Integer num, Integer size) {
        Pageable pageable = PageUtil.getNewsInsert(num, size);
        Page<GoodsOrder> page = goodsOrderRepository.findAllBySid(storeId, pageable);
        return createOrderPage(page);
    }

    /**
     * 分页查询异常订单
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
     * 根据店铺名进行查找
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
        return getSimpleOrderByStore(store.getId(), num, size);
    }
}

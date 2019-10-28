package com.kauuze.manager.service;

import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import com.jiwuzao.common.domain.mysql.entity.User;
import com.jiwuzao.common.dto.user.UserPhoneDto;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.include.PageUtil;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.vo.order.ReturnOrderVO;
import com.kauuze.manager.domain.mongo.repository.GoodsRepository;
import com.kauuze.manager.domain.mongo.repository.StoreRepository;
import com.kauuze.manager.domain.mysql.repository.GoodsOrderDetailRepository;
import com.kauuze.manager.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.manager.domain.mysql.repository.ReturnOrderRepository;
import com.kauuze.manager.domain.mysql.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReturnOrderService {
    @Resource
    private ReturnOrderRepository returnOrderRepository;
    @Resource
    private GoodsOrderRepository goodsOrderRepository;
    @Resource
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Resource
    private GoodsRepository goodsRepository;
    @Resource
    private StoreRepository storeRepository;
    @Resource
    private UserRepository userRepository;

    public ReturnOrderVO createVO(ReturnOrder returnOrder) {
        ReturnOrderVO returnOrderVO = new ReturnOrderVO();
        BeanUtils.copyProperties(returnOrder, returnOrderVO);
        return returnOrderVO;
    }

    /**
     * 分页获取所有退款订单
     *
     * @param num
     * @param size
     * @return
     */
    public PageDto<ReturnOrder> getReturnOrder(Integer num, Integer size) {
        Page<ReturnOrder> page = returnOrderRepository.findAll(PageUtil.getNewsInsert(num, size));
        return new PageDto<ReturnOrder>().setTotal(page.getTotalElements()).setContent(page.getContent());
    }

    /**
     * 分页获取退款订单前端展示
     *
     * @param num
     * @param size
     * @return
     */
    public PageDto<ReturnOrderVO> getReturnOrderVOS(Integer num, Integer size) {

//        return new PageDto<ReturnOrderVO>().setContent(collect).setTotal(page.getTotalElements());
//        Optional<Store> optional = storeRepository.findByUid(uid);
//        if (!optional.isPresent()) throw new RuntimeException("店铺不存在");
//        Store store = optional.get();
//        String storeId = store.getId();
        Page<ReturnOrder> page = returnOrderRepository.findAll(PageUtil.getNewsInsert(num, size));
        List<ReturnOrderVO> list = page.get().map(returnOrder -> {
            GoodsOrder goodsOrder = goodsOrderRepository.findByGoodsOrderNo(returnOrder.getGoodsOrderNo()).orElseThrow(() -> new RuntimeException("订单不存在"));
            Goods goods = goodsRepository.findByGid(goodsOrder.getGid());
            Store store = storeRepository.findById(returnOrder.getStoreId()).orElseThrow(()->new RuntimeException("店铺信息异常"));
            User user = userRepository.findById(returnOrder.getUid()).orElseThrow(() -> new RuntimeException("异常用户信息"));
            return new ReturnOrderVO().setGoodsImg(StringUtil.isBlank(returnOrder.getImages()) ? goods.getCover() : returnOrder.getImages())
                    .setGoodsTitle(goods.getTitle()).setGoodsOrderNo(returnOrder.getGoodsOrderNo())
                    .setGoodsReturnNo(returnOrder.getGoodsReturnNo()).setId(returnOrder.getId()).setReturnReason(returnOrder.getContent())
                    .setReturnStatus(returnOrder.getStatus()).setReturnPromise(goods.getGoodsReturn().getMsg())
                    .setStoreName(store.getStoreName())
                    .setUserName(user.getNickName()).setUserPhone(user.getPhone())
                    .setFailReason(returnOrder.getFailReason())
                    ;
        }).collect(Collectors.toList());
        PageDto<ReturnOrderVO> pageDto = new PageDto<>();
        pageDto.setTotal(page.getTotalElements()).setContent(list);
        return pageDto;
    }

}

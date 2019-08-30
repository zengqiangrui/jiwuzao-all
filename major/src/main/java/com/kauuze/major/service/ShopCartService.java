package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.ShopCartEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsDetail;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.domain.mongo.entity.ShopCart;
import com.jiwuzao.common.vo.shopcart.ShopCartVO;
import com.jiwuzao.common.dto.shopCart.ShopCartItem;
import com.kauuze.major.domain.mongo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackOn = Exception.class)
public class ShopCartService {
    @Autowired
    private ShopCartRepository shopCartRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsSpecRepository goodsSpecRepository;
    @Autowired
    private GoodsDetailRepository goodsDetailRepository;
    @Autowired
    private StoreRepository storeRepository;

    public String addItem(int uid, String gid, String specId, Integer num) {
        if (num > 10000)
            return null;
        String sid = goodsRepository.findByGid(gid).getSid();
        if (sid == null)
            return null;
        if (shopCartRepository.findByUidAndSpecId(uid, specId) != null) {
            return "商品已在购物车中";
        }
        ShopCart shopCart = new ShopCart(null, uid, sid, gid, specId, ShopCartEnum.settled, System.currentTimeMillis(), num);
        shopCartRepository.save(shopCart);
        return "添加成功";
    }

    //返回购物车列表
    public List<ShopCartVO> getItems(int uid) {
        List<ShopCart> items = shopCartRepository.findByUid(uid);
        //用map将同一个店铺的商品放在一个数组中
        Map<String, List<ShopCartItem>> map = new HashMap<>();
        items.forEach(e -> {
            String gid = e.getGid();
            String sid = e.getSid();
            Integer goodsNum = e.getNum();
            ShopCartItem item = makeItems(e.getId(), gid, e.getSpecId(), goodsNum);
            List<ShopCartItem> list = map.computeIfAbsent(sid, k -> new ArrayList<>());
            list.add(item);
        });

        //每个店铺组成一个ShopCartDto
        List<ShopCartVO> result = new ArrayList<>();
        map.forEach((k, v) -> {
            String sname = storeRepository.findById(k).get().getStoreName();
            result.add(new ShopCartVO(k, false, sname, v));
        });
        return result;
    }

    public ShopCartItem makeItems(String cid, String gid, String specId, int goodsNum) {
        Goods goods = goodsRepository.findByGid(gid);
        GoodsSpec goodsSpec = goodsSpecRepository.findById(specId).get();
        return new ShopCartItem(cid, gid, goods.getSid(), goodsSpec.getId(), false, goods.getDeliveryTime().getMsg(), goods.getGoodsReturn().getMsg(), goods.getTitle(), goods.getCover(), goodsSpec.getSpecClass(),
                goodsSpec.getSpecPrice().toString(), goods.getPostage().toString(), goodsNum, goodsSpec.getSpecInventory());
    }

    public String delItems(List<String> cidList) {
        cidList.forEach(e -> {
            shopCartRepository.deleteById(e);
        });
        return "操作成功";
    }
}

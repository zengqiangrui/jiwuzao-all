package com.kauuze.manager.service;

import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.kauuze.manager.domain.common.MongoUtil;
import com.kauuze.manager.domain.enumType.SystemGoodsNameEnum;
import com.kauuze.manager.domain.mongo.repository.*;
import com.kauuze.manager.domain.mysql.repository.UserRepository;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.include.PageUtil;
import com.kauuze.manager.service.dto.Goods.GoodsShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


@Service
public class GoodsService {

    @Autowired
    private GoodsSpecRepository goodsSpecRepository;
    @Autowired
    private GoodsDetailRepository goodsDetailRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private SystemGoodsRepository systemGoodsRepository;

    public String audit(int uid, String gid, AuditTypeEnum auditTypeEnum) {
//        User user = userRepository.findById(uid);
        BigDecimal decimal = systemGoodsRepository.findByName(SystemGoodsNameEnum.deposit).getPrice();
//        if (user.getDeposit().compareTo(decimal) < 0) {
//            return "未交满保证金" + decimal.toString();
//        }
        Goods goods = goodsRepository.findByGid(gid);
        if (goods == null) {
            return "该商品不存在";
        }
        if (goods.getAuditType() == AuditTypeEnum.agree) {
            return "该商品已通过审批";
        }
        Goods goods1 = new Goods();
        goods1.setGid(goods.getGid());
        goods1.setAuditType(auditTypeEnum);
        MongoUtil.updateNotNon("gid", goods1, Goods.class);
        return null;
    }

    public PageDto<GoodsShowDto> findGoodsByAuditType(AuditTypeEnum auditType, Integer page, int size) {
        Pageable pageable = PageUtil.getNewsInsert(page,size);
        Page<Goods> goodsPage;

        if(auditType != null){
            goodsPage = goodsRepository.findByAuditType(auditType, pageable);
        }else{
            goodsPage = goodsRepository.findAll(pageable);
        }
        PageDto<GoodsShowDto> goodsPageDto = new PageDto<>();
        goodsPageDto.setTotal(goodsPage.getTotalElements());
        //将商品信息放入GoodsShowDto
        List<Goods> goodsList = goodsPage.getContent();
        List<GoodsShowDto> dtoList = new LinkedList<GoodsShowDto>();
        for (Goods goods : goodsList) {
            String gid = goods.getGid();
            GoodsShowDto goodsShowDto = new GoodsShowDto();
            goodsShowDto.setGoods(goods);
            //Todo
            goodsShowDto.setGoodsDetail(goodsDetailRepository.findByGid(gid).get());
            goodsShowDto.setGoodsSpec(goodsSpecRepository.findByGid(gid));
            dtoList.add(goodsShowDto);

        }
        goodsPageDto.setContent(dtoList);
        return goodsPageDto;
    }
}

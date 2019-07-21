package com.kauuze.manager.service;

import com.jiwuzao.common.domain.common.MongoUtil;
import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.enumType.SystemGoodsNameEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsDetail;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.dto.goods.GoodsOpenDto;
import com.kauuze.manager.domain.mongo.repository.*;
import com.kauuze.manager.domain.mysql.repository.UserRepository;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.include.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        System.out.println("out:1");
        MongoUtil.updateNotNon("gid", goods1, Goods.class);
        System.out.println("out:2");
        return null;
    }

    public PageDto<GoodsOpenDto> findGoodsByAuditType(AuditTypeEnum auditType, Integer page, int size) {
        Pageable pageable = PageUtil.getNewsInsert(page,size);
        Page<Goods> goodsPage;

        if(auditType != null){
            goodsPage = goodsRepository.findByAuditType(auditType, pageable);
        }else{
            goodsPage = goodsRepository.findAll(pageable);
        }
        PageDto<GoodsOpenDto> goodsPageDto = new PageDto<>();
        goodsPageDto.setTotal(goodsPage.getTotalElements());
        //GoodsOpenDto
        List<Goods> goodsList = goodsPage.getContent();
        List<GoodsOpenDto> dtoList = new ArrayList<GoodsOpenDto>();
        goodsList.forEach(e -> {
            dtoList.add(getGoodsOpenDto(e.getGid()));
        });
        goodsPageDto.setContent(dtoList);
        return goodsPageDto;
    }

    /**
     * 获取商品资料
     *
     * @param gid
     * @return
     */
    public GoodsOpenDto getGoodsOpenDto(String gid) {
        Goods goods = goodsRepository.findByGid(gid);
        Optional<Store> optional = storeRepository.findById(goods.getSid());
        Optional<GoodsDetail> optional2 = goodsDetailRepository.findByGid(gid);
        List<GoodsSpec> goodsSpecs = goodsSpecRepository.findByGid(gid);
        if (!optional.isPresent() || !optional2.isPresent()) {
            return null;
        }
        Store store = optional.get();
        GoodsDetail goodsDetail = optional2.get();
        return new GoodsOpenDto(gid, goods.getUid(), goods.getSid(),
                store.getBusinessLicense(), store.getServicePhone(),
                goods.getTitle(), goods.getCover(), goods.getClassify(),
                goods.getSalesVolume(), goods.getDefaultPrice(), goods.getPostage(),
                goodsDetail.getSlideshow(), goodsDetail.getDetailLabel(),
                goodsDetail.getDetailPhotos(), goodsDetail.getGoodsType(),
                goodsDetail.getGoodsTypeClass(), goodsSpecs, goods.getPutaway(),
                goods.getAuditType(), goods.getCreateTime(), goods.getUpdateTime());
    }
}

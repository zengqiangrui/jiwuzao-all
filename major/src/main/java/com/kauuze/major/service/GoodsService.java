package com.kauuze.major.service;

import com.kauuze.major.api.pojo.common.GoodsSpecPojo;
import com.kauuze.major.domain.common.EsUtil;
import com.kauuze.major.domain.common.MongoUtil;
import com.kauuze.major.domain.enumType.AuditTypeEnum;
import com.kauuze.major.domain.enumType.GoodsClassifyEnum;
import com.kauuze.major.domain.enumType.SystemGoodsNameEnum;
import com.kauuze.major.domain.es.entity.GoodsEs;
import com.kauuze.major.domain.mongo.entity.Category;
import com.kauuze.major.domain.mongo.entity.Goods;
import com.kauuze.major.domain.mongo.entity.GoodsDetail;
import com.kauuze.major.domain.mongo.entity.GoodsSpec;
import com.kauuze.major.domain.mongo.entity.userBastic.Store;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.entity.User;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.include.DateTimeUtil;
import com.kauuze.major.include.PageDto;
import com.kauuze.major.service.dto.goods.GoodsOpenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-29 15:50
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class GoodsService {
    @Autowired
    private GoodsDetailRepository goodsDetailRepository;
    @Autowired
    private GoodsSpecRepository goodsSpecRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private SystemGoodsRepository systemGoodsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 添加商品
     */
    public String addGoods(int uid, GoodsClassifyEnum classify, String title, String cover, BigDecimal defaultPrice, String slideshow, BigDecimal postage, String detailLabel, String goodsType, String goodsTypeClass, String detailPhotos, List<GoodsSpecPojo> goodsSpecPojo) {
        Optional<Store> opt = storeRepository.findByUid(uid);
        if (!opt.isPresent()) {
            return "未开通店铺";
        } else if (opt.get().getViolation()) {
            return "店铺被封禁";
        }
        if (goodsRepository.countByUid(uid) > 100) {
            return "最多添加100个商品";
        }
        Goods goods = new Goods(null, uid, opt.get().getId(), title, cover, classify, 0, 0, defaultPrice, postage, false, null, null, AuditTypeEnum.wait, null,System.currentTimeMillis(),null);
        goodsRepository.save(goods);
        GoodsDetail goodsDetail = new GoodsDetail(null, goods.getGid(), slideshow, detailLabel, classify, goodsType, goodsTypeClass, detailPhotos);
        goodsDetailRepository.save(goodsDetail);
        for (GoodsSpecPojo specPojo : goodsSpecPojo) {
            goodsSpecRepository.save(new GoodsSpec(null, goods.getGid(), specPojo.getSpecClass(), specPojo.getSpecPrice(), specPojo.getSpecInventory()));
        }
        return null;
    }

    /**
     * 上架商品
     *
     * @param uid
     * @return
     */
    public String putAway(int uid, String gid) {
        User user = userRepository.findById(uid);
        BigDecimal decimal = systemGoodsRepository.findByName(SystemGoodsNameEnum.deposit).getPrice();
        if (user.getDeposit().compareTo(decimal) < 0) {
            return "未交满保证金" + decimal.toString();
        }
        Goods goods = goodsRepository.findByGid(gid);
        if (goods == null) {
            return "该商品不存在";
        }
        if (goods.getUid() != uid) {
            return "你没有权限";
        }
        if (goods.getAuditType() != AuditTypeEnum.agree) {
            return "该商品未通过审批";
        }
        if (goods.getPutaway()) {
            return "该商品已上架";
        }
        Map<String, String> map = new HashMap<>();
        map.put("gid", goods.getGid());
        map.put("putaway", "true");
        EsUtil.modify(map);
        return null;
    }

    /**
     * 商品下架
     *
     * @param uid
     * @param gid
     * @return
     */
    public String soldOut(int uid, String gid) {
        Goods goods = goodsRepository.findByGid(gid);
        if (goods == null) {
            return "该商品不存在";
        }

        if (goods.getUid() != uid) {
            return "你没有权限";
        }
        if (!goods.getPutaway()) {
            return "该商品已下架";
        }
        Map<String, String> map = new HashMap<>();
        map.put("gid", goods.getGid());
        map.put("putaway", "false");
        map.put("soldOutTime", String.valueOf(System.currentTimeMillis()));
        EsUtil.modify(map);
        return null;
    }

    /**
     * 删除商品
     *
     * @return
     */
    public String deleteGoods(int uid, String gid) {
        Goods goods = goodsRepository.findByGid(gid);
        if (goods == null) {
            return "该商品不存在";
        }
        if (goods.getUid() != uid) {
            return "你没有权限";
        }
        if (goods.getPutaway()) {
            return "该商品未下架";
        }
        if (goods.getSoldOutTime() + DateTimeUtil.getOneDayMill() * 3 > System.currentTimeMillis()) {
            return "该商品下架时间未满72小时";
        }
        goodsRepository.deleteById(gid);
        goodsDetailRepository.deleteById(gid);
        goodsSpecRepository.deleteByGid(gid);
        return null;
    }

    /**
     * 修改价格
     *
     * @return
     */
    public String modifyPrice(int uid, String gid, String goodsSpecId, BigDecimal specPrice) {
        Goods goods = goodsRepository.findByGid(gid);
        if (goods == null) {
            return "商品不存在";
        }
        if (goods.getUid() != uid) {
            return "你无权限";
        }
        Optional<GoodsSpec> optional = goodsSpecRepository.findById(goodsSpecId);
        if (!optional.isPresent()) {
            return "规格不存在";
        }
        if (optional.get().getGid() != gid) {
            return "你无权限";
        }
        MongoUtil.updateNotNon("id", new GoodsSpec().setId(goodsSpecId).setSpecPrice(specPrice), GoodsSpec.class);
        return null;
    }

    /**
     * 修改库存
     *
     * @param uid
     * @param gid
     * @param goodsSpecId
     * @return
     */
    public String modifyInventory(int uid, String gid, String goodsSpecId, Integer inventory) {
        Goods goods = goodsRepository.findByGid(gid);
        if (goods == null) {
            return "商品不存在";
        }
        if (goods.getUid() != uid) {
            return "你无权限";
        }
        Optional<GoodsSpec> optional = goodsSpecRepository.findById(goodsSpecId);
        if (!optional.isPresent()) {
            return "规格不存在";
        }
        if (optional.get().getGid() != gid) {
            return "你无权限";
        }
        MongoUtil.updateNotNon("id", new GoodsSpec().setId(goodsSpecId).setSpecInventory(inventory), GoodsSpec.class);
        return null;
    }

    /**
     * 修改邮费
     *
     * @return
     */
    public String modifyPostage(int uid, String gid, BigDecimal postage) {
        Goods goods = goodsRepository.findByGid(gid);
        if (goods == null) {
            return "商品不存在";
        }
        if (goods.getUid() != uid) {
            return "你无权限";
        }
        MongoUtil.updateNotNon("id", new GoodsEs().setGid(gid).setPostage(postage), GoodsEs.class);
        return null;
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
        return new GoodsOpenDto(gid, goods.getUid(), goods.getSid(), store.getBusinessLicense(), store.getServicePhone(), goods.getTitle(), goods.getCover(), goods.getClassify(), goods.getSalesVolume(), goods.getDefaultPrice(), goods.getPostage(), goodsDetail.getSlideshow(), goodsDetail.getDetailLabel(), goodsDetail.getDetailPhotos(), goodsDetail.getGoodsType(), goodsDetail.getGoodsTypeClass(), goodsSpecs,goods.getPutaway(),goods.getAuditType(),goods.getCreateTime(),goods.getUpdateTime());
    }


    /**
     * 获取所有商品详情的list
     * @param pageable
     * @return
     */
    public PageDto<GoodsOpenDto> getGoodsPage(Pageable pageable) {
        return getByGoodsPage(goodsRepository.findAll(pageable));
    }

    /**
     * 根据用户id获取所有发布商品
     * @param uid
     * @param pageable
     * @return
     */
    public PageDto<GoodsOpenDto> getGoodsPageByUid(int uid, Pageable pageable){
        Page<Goods> goods = goodsRepository.findByUid(uid,pageable);
        return getByGoodsPage(goods);
    }

    private PageDto<GoodsOpenDto> getByGoodsPage(Page<Goods> goods){
        List<GoodsOpenDto> goodsOpenList = new ArrayList<>();
        PageDto<GoodsOpenDto> goodsOpenDtos = new PageDto<>();
        goods.forEach(e->{
            goodsOpenList.add(getGoodsOpenDto(e.getGid()));
        });
        goodsOpenDtos.setContent(goodsOpenList);
        goodsOpenDtos.setTotal(goods.getTotalElements());
        return goodsOpenDtos;
    }

    /**
     * 根据商品目录id获取商品目录json字符串
     *
     * @param categoryId
     * @return
     */
    public Category getCategoryById(Integer categoryId) {
        Optional<Category> opt = categoryRepository.findById(categoryId);
        return opt.orElse(null);
    }
}

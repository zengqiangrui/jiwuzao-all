package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.*;
import com.jiwuzao.common.domain.mongo.entity.Category;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsDetail;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.domain.mysql.entity.User;
import com.jiwuzao.common.dto.goods.GoodsOpenDto;
import com.jiwuzao.common.dto.goods.GoodsSimpleDto;
import com.jiwuzao.common.include.DateTimeUtil;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.common.GoodsSpecPojo;
import com.jiwuzao.common.pojo.goods.GoodsPagePojo;
import com.jiwuzao.common.vo.goods.GoodsCommentVO;
import com.jiwuzao.common.vo.goods.GoodsDetailVO;
import com.jiwuzao.common.vo.goods.GoodsSimpleVO;
import com.jiwuzao.common.vo.goods.MerchantGoodsVO;
import com.jiwuzao.common.vo.goods.ViewHistoryVO;
import com.kauuze.major.domain.common.EsUtil;
import com.kauuze.major.domain.common.MongoUtil;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.repository.UserRepository;
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
    private UserInfoRepository userInfoRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private SystemGoodsRepository systemGoodsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GoodsCommentRepository goodsCommentRepository;
    @Autowired
    private AppriseRepository appriseRepository;
    @Autowired
    private ViewHistoryRepository viewHistoryRepository;

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
        Goods goods = new Goods(null, uid, opt.get().getId(), title, cover, classify, 0, 0, defaultPrice, postage, false, null, null, AuditTypeEnum.wait, null, System.currentTimeMillis(), null);
        goodsRepository.save(goods);
        GoodsDetail goodsDetail = new GoodsDetail(null, goods.getGid(), slideshow, detailLabel, classify, goodsType, goodsTypeClass, detailPhotos, 0L);
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
        /**
         * 获取保证金，暂时不交保证金
         */
//        User user = userRepository.findById(uid);
//        BigDecimal decimal = systemGoodsRepository.findByName(SystemGoodsNameEnum.deposit).getPrice();
//        if (user.getDeposit().compareTo(decimal) < 0) {
//            return "未交满保证金" + decimal.toString();
//        }
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
        goods.setPutaway(true).setPutawayTime(System.currentTimeMillis());
        Goods save = goodsRepository.save(goods);
        if (null != save)
            return "上架成功";
        else
            return "上架失败";
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
        if (!optional.get().getGid().equals(gid)) {
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
        if (!optional.get().getGid().equals(gid)) {
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
        MongoUtil.updateNotNon("id", new Goods().setGid(gid).setPostage(postage), Goods.class);
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
        return new GoodsOpenDto(gid, goods.getUid(), goods.getSid(), store.getBusinessLicense(), store.getServicePhone(), goods.getTitle(), goods.getCover(), goods.getClassify(), goods.getSalesVolume(), goods.getDefaultPrice(), goods.getPostage(), goodsDetail.getSlideshow(), goodsDetail.getDetailLabel(), goodsDetail.getDetailPhotos(), goodsDetail.getGoodsType(), goodsDetail.getGoodsTypeClass(), goodsSpecs, goods.getPutaway(), goods.getAuditType(), goods.getCreateTime(), goods.getUpdateTime());
    }

    /**
     * 获取商品简略信息
     *
     * @param gid
     * @return
     */
    public GoodsSimpleDto getGoodsSimpleDto(String gid) {
        Goods goods = goodsRepository.findByGid(gid);
        return new GoodsSimpleDto(gid, goods.getTitle(), goods.getCover(), goods.getSalesVolume(), goods.getDefaultPrice(), goods.getPostage(), goods.getPutaway(), goods.getAuditType(), goods.getCreateTime(), goods.getUpdateTime());
    }


    /**
     * 获取所有商品详情的list
     *
     * @param pageable
     * @return
     */
    public PageDto<GoodsOpenDto> getGoodsPage(Pageable pageable) {
        return getByGoodsPage(goodsRepository.findAll(pageable));
    }

    /**
     * 根据用户id获取所有发布商品
     *
     * @param uid
     * @param pageable
     * @return
     */
    public PageDto<GoodsOpenDto> getGoodsPageByUid(int uid, Pageable pageable) {
        Page<Goods> goods = goodsRepository.findByUid(uid, pageable);
        return getByGoodsPage(goods);
    }

    /**
     * 获取简单用户信息，效率高
     *
     * @param uid
     * @param pageable
     * @return
     */
    public PageDto<GoodsSimpleDto> getGoodsSimple(int uid, Pageable pageable) {
        Page<Goods> goods = goodsRepository.findByUid(uid, pageable);
        return getGoodsSimplePage(goods);
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

    /**
     * 获取商品所有信息
     *
     * @param goods
     * @return
     */
    private PageDto<GoodsOpenDto> getByGoodsPage(Page<Goods> goods) {
        List<GoodsOpenDto> goodsOpenList = new ArrayList<>();
        PageDto<GoodsOpenDto> goodsOpenDtos = new PageDto<>();
        goods.forEach(e -> {
            goodsOpenList.add(getGoodsOpenDto(e.getGid()));
        });
        goodsOpenDtos.setContent(goodsOpenList);
        goodsOpenDtos.setTotal(goods.getTotalElements());
        return goodsOpenDtos;
    }

    /**
     * 获取用户商品简单分页
     *
     * @param goods
     * @return
     */
    private PageDto<GoodsSimpleDto> getGoodsSimplePage(Page<Goods> goods) {
        List<GoodsSimpleDto> goodsSimpleDtos = new ArrayList<>();
        PageDto<GoodsSimpleDto> dto = new PageDto<>();
        goods.forEach(e -> {
            goodsSimpleDtos.add(getGoodsSimpleDto(e.getGid()));
        });
        dto.setContent(goodsSimpleDtos);
        dto.setTotal(goods.getTotalElements());
        return dto;
    }

    /**
     * 根据规格字符串获取商品规格
     *
     * @param gid
     * @param specClass
     * @return
     */
    public GoodsSpec getSpecByGoodsSpecClass(String gid, String specClass) {
        return goodsSpecRepository.findByGidAndAndSpecClass(gid, specClass).orElse(null);
    }

    /**
     * app端获取商品详情
     *
     * @param gid
     * @return
     */
    public GoodsDetailVO getGoodsDetail(String gid) {
        GoodsDetail detail = goodsDetailRepository.findByGid(gid).get();
        Goods goods = goodsRepository.findByGid(gid);
        User user = userRepository.findById(goods.getUid()).get();
        UserInfo info = userInfoRepository.findByUid(goods.getUid());
        if (detail == null || goods == null || user == null || info == null)
            return null;
        GoodsDetailVO vo = new GoodsDetailVO();
        vo.setTitle(goods.getTitle()).setDefaultPrice(goods.getDefaultPrice())
                .setDetailLabel(detail.getDetailLabel()).setDetailPhotos(detail.getDetailPhotos())
                .setPostage(goods.getPostage()).setSlideshow(detail.getSlideshow())
                .setNickName(user.getNickName()).setPortrait(info.getPortrait())
                .setGoodsType(detail.getGoodsType()).setGoodsTypeClass(detail.getGoodsTypeClass())
                .setCover(goods.getCover()).setAppriseCnt(detail.getAppriseCnt());
        return vo;
    }


    /**
     * 获取商品列表
     *
     * @return
     */
    public List<Goods> getGoodsList(GoodsPagePojo goodsPagePojo) {
//        Pageable pageable = PageUtil.getNewsInsert(page,size);
        PageRequest pageRequest = PageRequest.of(goodsPagePojo.getCurrentPage(), goodsPagePojo.getPageSize(),
                Sort.Direction.DESC, goodsPagePojo.getSortBy());
        List<Goods> goodsPage = null;
        switch (goodsPagePojo.getCurrentTab()) {
            case 0:
                //推荐，暂写food
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.food);
                break;
            case 1:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.special);
                break;
            case 2:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.food);
                break;
            case 3:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.clothing);
                break;
            case 4:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.jewelry);
                break;
            case 5:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.bags);
                break;
            case 6:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.appliance);
                break;
            case 7:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.gift);
                break;
            case 8:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.beauty);
                break;
            case 9:
                goodsPage = goodsRepository.findByClassify(pageRequest, GoodsClassifyEnum.other);
                break;
        }
        return goodsPage;
    }

    /**
     * 商家获取一个商品信息
     *
     * @param gid 商品id
     * @return MerchantGoodsVO 单个商品显示对象
     */
    public MerchantGoodsVO merchantGetGoodsDetail(String gid) {
        Goods goods = goodsRepository.findByGid(gid);
        if (null == goods) {
            return null;
        } else {
            Optional<GoodsDetail> detailOptional = goodsDetailRepository.findByGid(gid);
            if (!detailOptional.isPresent()) {
                return null;
            } else {
                GoodsDetail goodsDetail = detailOptional.get();
                List<GoodsSpec> specList = goodsSpecRepository.findByGid(gid);
                return new MerchantGoodsVO()
                        .setTitle(goods.getTitle()).setSlideShow(goodsDetail.getSlideshow())
                        .setPutAway(goods.getPutaway()).setPostage(goods.getPostage())
                        .setGid(goods.getGid()).setDetailPhotos(goodsDetail.getDetailPhotos())
                        .setDetailLabel(goodsDetail.getDetailLabel())
                        .setDefaultPrice(goods.getDefaultPrice()).setCover(goods.getCover())
                        .setClassify(goods.getClassify()).setGoodsSpecs(specList);
            }
        }
    }

    /**
     * 根据店铺查询上架商品
     * @param storeId
     * @param pageable
     * @return
     */
    public PageDto<GoodsSimpleVO> getGoodsByStore(String storeId, Pageable pageable) {
        Page<Goods> page = goodsRepository.findAllBySidAndPutaway(storeId,true, pageable);
        PageDto<GoodsSimpleVO> pageDto = new PageDto<>();
        pageDto.setTotal(page.getTotalElements());
        List<GoodsSimpleVO> list = new ArrayList<>();
        for (Goods goods : page.getContent()) {
                GoodsSimpleVO goodsSimpleVO = new GoodsSimpleVO()
                        .setGoodsId(goods.getGid()).setGoodsImg(goods.getCover())
                        .setGoodsName(goods.getTitle()).setGoodsPrice(goods.getDefaultPrice());
                list.add(goodsSimpleVO);

        }
        pageDto.setContent(list);
        return pageDto;
    }

    /**
     * 添加评论,购买后商品的才能够评论
     * @param uid
     * @param comment
     * @return
     */
    public String addComment(int uid, String gid, String comment) {
        Comment comment1 = new Comment(null, gid, uid, comment, System.currentTimeMillis(), false);
        goodsCommentRepository.save(comment1);
        return "添加成功";
    }

    /**
     * 获取商品评论列表
     * @param gid
     * @return
     */
    public List<GoodsCommentVO> getGoodsComment(String gid) {
        List<Comment> list = goodsCommentRepository.findByGid(gid);
        List<GoodsCommentVO> res = new ArrayList<>();
        list.forEach((e)->{
            Integer uid = e.getUid();
            UserInfo info = userInfoRepository.findByUid(uid);
            GoodsCommentVO vo = new GoodsCommentVO(e.getUid(), info.getNickName(), e.getTime(),
                    info.getPortrait(), e.getContent());
            res.add(vo);
        });
        if (res.size() > 0)
            return res;
        else {
            return null;
        }
    }

    /**
     * 用户对指定商品进行点赞
     * @param uid
     * @param gid
     * @return
     */
    public Long addApprise(int uid, String gid) {
        //判断对象是否存在
        GoodsDetail detail = goodsDetailRepository.findByGid(gid).get();
        if (detail == null)
            return null;
        //判断是否已经点过赞
        Apprise apprise = appriseRepository.findByGidAndUid(gid, uid);
        if (apprise != null) {
            return detail.getAppriseCnt();
        }
        //点赞操作
        apprise = new Apprise(null, uid, gid, System.currentTimeMillis());
        appriseRepository.save(apprise);

        GoodsDetail detail1Up = new GoodsDetail();
        Long cnt = detail.getAppriseCnt()+1;
        detail1Up.setAppriseCnt(cnt);
        MongoUtil.updateNotNon("gid", detail1Up,GoodsDetail.class);
        return cnt;
    }

    /**
     * 取消点赞
     * @param uid
     * @param gid
     * @return
     */
    public Long delApprise(int uid, String gid) {
        //判断对象是否存在
        GoodsDetail detail = goodsDetailRepository.findByGid(gid).get();
        if (detail == null)
            return null;
        //判断是否已经点过赞
        Apprise apprise = appriseRepository.findByGidAndUid(gid, uid);
        if (apprise != null) {
            //取消操作
            appriseRepository.deleteById(apprise.getId());

            GoodsDetail detail1Up = new GoodsDetail();
            Long cnt = detail.getAppriseCnt()-1;
            detail1Up.setAppriseCnt(cnt);
            MongoUtil.updateNotNon("gid", detail1Up,GoodsDetail.class);
            return cnt;
        } else {
            return detail.getAppriseCnt();
        }
    }

    /**
     * 获取浏览记录
     * @param uid
     * @return
     */
    public List<ViewHistoryVO> getViewHistory(int uid) {
        List<ViewHistory> list = viewHistoryRepository.findByUidAndDeleteFalse(uid);
        List<ViewHistoryVO> volist = new ArrayList<>();
        for (ViewHistory e : list){
            Goods goods = goodsRepository.findByGid(e.getGid());
            if (goods == null){
                continue;
            }
            ViewHistoryVO vo = new ViewHistoryVO(e.getGid(), e.getTime(), goods.getTitle(), goods.getCover());
        }
        return volist;
    }


    /**
     * 添加浏览记录
     * @param uid
     * @param gid
     * @return
     */
    public String addViewHistory(int uid, String gid) {
        //去重判断
        ViewHistory viewHistory = viewHistoryRepository.findByUidAndGid(uid, gid);
        if (viewHistory != null)
        {
            //更新浏览时间
            viewHistory.setTime(System.currentTimeMillis());
            viewHistory.setDelete(false);
            viewHistoryRepository.save(viewHistory);
            return "添加成功";
        } else {
            viewHistory = new ViewHistory(null, uid, gid, System.currentTimeMillis(), false);
            viewHistoryRepository.save(viewHistory);
            return "添加成功";
        }
    }


    /**
     * 删除历史记录
     * @param uid
     * @param id
     * @return
     */
    public String delViewHistory(int uid, String id) {
        ViewHistory viewHistory = viewHistoryRepository.findById(id).get();
        if (viewHistory == null) {
            return null;
        } else {
            viewHistoryRepository.deleteById(id);
            return "删除成功";
        }
    }
}
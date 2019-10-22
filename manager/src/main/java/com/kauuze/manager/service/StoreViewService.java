package com.kauuze.manager.service;

import com.jiwuzao.common.domain.enumType.UserStateEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserToken;
import com.jiwuzao.common.include.PageUtil;
import com.kauuze.manager.domain.common.MongoUtil;
import com.kauuze.manager.domain.mongo.repository.GoodsRepository;
import com.kauuze.manager.domain.mongo.repository.StoreRepository;
import com.kauuze.manager.include.DateTimeUtil;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.service.dto.storeView.StoreShowDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class StoreViewService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 通过Uid搜索店铺
     *
     * @param uid
     * @return
     */
    public StoreShowDto findByUid(int uid) {
        Store store = storeRepository.findByUid(uid);
        if (store == null) {
            return null;
        }
        return new StoreShowDto(store.getId(), store.getUid(), store.getCreateTime(), store.getViolation(),
                store.getViolationCause(), store.getStoreName(), store.getStoreIcon(), store.getServicePhone(),
                store.getStoreIntro(), store.getBusinessLicense());
    }

    /**
     * 通过店铺名搜索店铺
     *
     * @param name
     * @return
     */
    public StoreShowDto findByStoreName(String name) {
        Store store = storeRepository.findByStoreName(name);
        if (store == null) {
            return null;
        }
        return new StoreShowDto(store.getId(), store.getUid(), store.getCreateTime(), store.getViolation(),
                store.getViolationCause(), store.getStoreName(), store.getStoreIcon(), store.getServicePhone(),
                store.getStoreIntro(), store.getBusinessLicense());
    }

    /**
     * 封禁店铺
     *
     * @param uid            店铺id
     * @param violationCause 封禁原因
     * @return
     */
    public boolean illegalStore(Integer uid, String violationCause) {
        Store store = storeRepository.findByUid(uid);
        if (store == null)
            return false;

        Store store2 = new Store();
        store2.setId(store.getId());
        store2.setViolation(true);
        store2.setViolationCause(violationCause);
        MongoUtil.updateNotNon("id", store2, Store.class);

        List<Goods> list = goodsRepository.findByUid(uid);
        for (Goods goods : list) {
            goods.setPutOffTime(System.currentTimeMillis());
            goods.setPutaway(false);
            goodsRepository.save(goods);
        }
        UserToken userToken = new UserToken();
        userToken.setUid(uid);
        userToken.setUserState(UserStateEnum.ban);
        userToken.setUserStateEndTime(System.currentTimeMillis() + DateTimeUtil.getOneYearMill() * 70);
        MongoUtil.updateNotNon("uid", userToken, UserToken.class);
        return true;
    }

    /**
     * 分页查找店铺获取简略信息
     *
     * @param num
     * @param size
     * @return
     */
    public PageDto<StoreShowDto> findAllStorePage(Integer num, Integer size) {
        Pageable pageable = PageUtil.getNewsInsert(num, size);
        Page<Store> page = storeRepository.findAll(pageable);
        PageDto<StoreShowDto> pageDto = new PageDto<>();
        List<StoreShowDto> collect = page.getContent().stream().map(store -> {
            StoreShowDto storeDto = new StoreShowDto();
            BeanUtils.copyProperties(store, storeDto,"storeIntro");
            return storeDto;
        }).collect(Collectors.toList());
        return pageDto.setTotal(page.getTotalElements()).setContent(collect);
    }

}

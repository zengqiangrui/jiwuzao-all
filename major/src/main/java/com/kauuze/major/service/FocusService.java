package com.kauuze.major.service;

import com.jiwuzao.common.domain.mongo.entity.Focus;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.dto.user.FansAndFocusDTO;
import com.jiwuzao.common.vo.user.FocusVO;
import com.kauuze.major.domain.mongo.repository.FocusRepository;
import com.kauuze.major.domain.mongo.repository.UserInfoRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FocusService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private FocusRepository focusRepository;

    /**
     * 创建关注
     *
     * @param uidA
     * @param uidB
     * @return
     */
    public Focus createFocus(int uidA, int uidB) {
        if (uidA == uidB) {
            throw new RuntimeException("用户不能关注自己");
        }
        Optional<Focus> opt = focusRepository.findByUidAAndUidB(uidA, uidB);
        if (opt.isPresent()) {
            Focus focus = opt.get();
            if (focus.getStatus()) {
                throw new RuntimeException("已经关注该用户");
            } else {
                return focusRepository.save(focus.setStatus(true));
            }
        } else {
            Focus focus = new Focus().setUidA(uidA).setUidB(uidB).setCreateTime(System.currentTimeMillis());
            return focusRepository.save(focus);
        }
    }

    /**
     * a取消关注b
     *
     * @param uidA
     * @param uidB
     * @return
     */
    public Focus cancelFocus(int uidA, int uidB) {
        Optional<Focus> opt = focusRepository.findByUidAAndUidB(uidA, uidB);
        if (!opt.isPresent()) {
            throw new RuntimeException("从未关注过");
        } else {
            Focus focus = opt.get();
            if (focus.getStatus()) {
                return focusRepository.save(focus.setStatus(false));
            } else {
                throw new RuntimeException("已取消关注");
            }
        }
    }

    /**
     * 获取A是否关注B
     *
     * @param uidA
     * @param uidB
     * @return
     */
    public Focus getFocus(int uidA, int uidB) {
        if (uidA == uidB) {
            return null;
        } else {
            Optional<Focus> opt = focusRepository.findByUidAAndUidB(uidA, uidB);
            return opt.orElse(null);
        }
    }

    /**
     * 获取关注列表
     * @param uid
     * @return
     */
    public List<FocusVO> getFocusList(int uid) {
        List<Focus> list = focusRepository.findAllByUidA(uid);
        List<FocusVO> focusVOS = new ArrayList<>();
        for (Focus focus : list) {
            if (focus.getStatus()) {//用户已关注
                Optional<Focus> optional = focusRepository.findByUidAAndUidB(focus.getUidB(), uid);
                UserInfo user = userInfoRepository.findByUid(focus.getUidB());
                FocusVO focusVO = new FocusVO();
                focusVO.setAvatar(user.getPortrait()).setNickName(user.getNickName()).setSex(user.getSex()).setPersonalSign(user.getPersonalSign()).setUid(user.getUid());
                if (!optional.isPresent()) {
                    focusVO.setStatus(false);
                } else {
                    focusVO.setStatus(optional.get().getStatus());
                }
                focusVOS.add(focusVO);
            }
        }
        return focusVOS;
    }

    /**
     * 获取粉丝列表
     * @param uid
     * @return
     */
    public List<FocusVO> getFansList(int uid){
        List<Focus> list = focusRepository.findAllByUidB(uid);
        List<FocusVO> focusVOS = new ArrayList<>();
        for (Focus focus : list) {
            if (focus.getStatus()){
                Optional<Focus> optional = focusRepository.findByUidAAndUidB(uid, focus.getUidA());
                UserInfo user = userInfoRepository.findByUid(focus.getUidA());
                FocusVO focusVO = new FocusVO()
                .setAvatar(user.getPortrait()).setNickName(user.getNickName()).setSex(user.getSex()).setPersonalSign(user.getPersonalSign()).setUid(user.getUid());
                if (!optional.isPresent()) {
                    focusVO.setStatus(false);
                } else {
                    focusVO.setStatus(optional.get().getStatus());
                }
                focusVOS.add(focusVO);
            }
        }
        return focusVOS;
    }


    /**
     * 获取粉丝数和关注数
     * @param uid
     * @return
     */
    public FansAndFocusDTO getFocusAndFansNum(int uid) {
        return new FansAndFocusDTO().setFansNum(focusRepository.countByUidBAndStatus(uid,true))
                .setFocusNum(focusRepository.countByUidAAndStatus(uid,true)).setUid(uid);
    }
}

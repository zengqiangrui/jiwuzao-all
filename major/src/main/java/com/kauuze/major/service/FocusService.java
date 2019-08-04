package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.FocusStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.Focus;
import com.kauuze.major.domain.mongo.repository.FocusRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class FocusService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FocusRepository focusRepository;

    /**
     * 创建关注
     * @param uidA
     * @param uidB
     * @return
     */
    public Focus createFocus(int uidA,int uidB){
        if(uidA == uidB){
            throw new RuntimeException("用户不能关注自己");
        }
        Optional<Focus> opt = focusRepository.findByUidAAndUidB(uidA,uidB);
        if(opt.isPresent()){
            Focus focus = opt.get();
            if(focus.getStatus()){
                throw new RuntimeException("已经关注该用户");
            }else{
                return focusRepository.save(focus.setStatus(true));
            }
        }else{
            Focus focus = new Focus().setUidA(uidA).setUidB(uidB).setCreateTime(System.currentTimeMillis());
            return focusRepository.save(focus);
        }
    }

    /**
     * a取消关注b
     * @param uidA
     * @param uidB
     * @return
     */
    public Focus cancelFocus(Integer uidA,Integer uidB){
        Optional<Focus> opt = focusRepository.findByUidAAndUidB(uidA,uidB);
        if(!opt.isPresent()){
            throw new RuntimeException("从未关注过");
        }else {
            Focus focus = opt.get();
            if(focus.getStatus()){
                return focusRepository.save(focus.setStatus(false));
            }else {
                throw new RuntimeException("已取消关注");
            }
        }
    }

    //todo 获取AB关系
//    public FocusStatusEnum getFocusStatus(Integer uidA, Integer uidB){
//        Optional<Focus> opt1 = focusRepository.findByUidAAndUidB(uidA, uidB);
//        Optional<Focus> opt2 = focusRepository.findByUidAAndUidB(uidB, uidA);
//        if(!opt1.isPresent()){
//
//        }
//    }


}

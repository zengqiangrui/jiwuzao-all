package com.kauuze.major.service;

import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;
}

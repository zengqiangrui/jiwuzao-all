package com.kauuze.manager.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class OrderViewService {
}

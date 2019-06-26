package com.kauuze.major.api;

import com.kauuze.major.service.SystemOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-20 17:46
 */
@RestController
@RequestMapping("/systemOrder")
public class SystemOrderController {
    @Autowired
    private SystemOrderService systemOrderService;


}

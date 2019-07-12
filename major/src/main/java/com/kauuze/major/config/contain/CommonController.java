package com.kauuze.major.config.contain;


import com.kauuze.major.config.permission.GreenWay;
import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.repository.UserTokenRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.include.DateTimeUtil;
import com.kauuze.major.include.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用api的配置
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-27 13:27
 */
@RestController
public class CommonController {
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    /**
     * 查看服务器响应情况
     * @return
     */
    @RequestMapping("/ping")
    @GreenWay
    public JsonResult ping(){
        Long start = System.currentTimeMillis();
        userRepository.findById(1);
        userTokenRepository.findByUid(1);
        goodsRepository.findById("1");
        Long runtime = System.currentTimeMillis() - start;
        Map map = new HashMap<>();
        map.put("server",getLinuxLocalIp());
        map.put("runtime",runtime);
        return JsonResult.success(map);
    }

    /**
     * 获取当前系统时间
     * @return
     */
    @RequestMapping("/time")
    @GreenWay
    public JsonResult time(){
        return JsonResult.success(DateTimeUtil.covertDateView(System.currentTimeMillis()));
    }

    private static String getLinuxLocalIp(){
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        return ip;
    }
}

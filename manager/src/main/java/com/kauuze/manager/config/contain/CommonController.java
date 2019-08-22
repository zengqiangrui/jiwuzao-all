package com.kauuze.manager.config.contain;


import com.jiwuzao.common.pojo.common.GidPojo;
import com.kauuze.manager.config.permission.GreenWay;
import com.kauuze.manager.config.permission.Root;
import com.kauuze.manager.domain.mongo.repository.UserTokenRepository;
import com.kauuze.manager.domain.mysql.repository.UserRepository;
import com.kauuze.manager.include.DateTimeUtil;
import com.kauuze.manager.include.JsonResult;
import com.kauuze.manager.include.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
//        goodsRepositoryEs.findById("1");
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

//    @RequestMapping("/modifyEs")
//    @Root
//    public JsonResult modifyEs(@RequestBody Map<String,String> map){
//        if(StringUtil.isBlank(map.get("gid"))){
//            return JsonResult.failure();
//        }
//        EsUtil.modify(map);
//        return JsonResult.success();
//    }
//
//    @RequestMapping("/delEs")
//    @Root
//    public JsonResult delEs(@Valid @RequestBody GidPojo gidPojo){
//        goodsRepositoryEs.deleteById(gidPojo.getGid());
//        return JsonResult.success();
//    }
}

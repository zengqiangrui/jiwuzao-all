package com.kauuze.major.api;

import com.kauuze.major.api.pojo.common.GidPojo;
import com.kauuze.major.api.pojo.goods.AddGoodsPojo;
import com.kauuze.major.config.contain.ParamMismatchException;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.include.JsonResult;
import com.kauuze.major.include.JsonUtil;
import com.kauuze.major.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-29 15:49
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/addGoods")
    @Merchant
    public JsonResult addGoods(@RequestAttribute int uid, @Valid @RequestBody AddGoodsPojo addGoodsPojo){
        List<Map<String,List<String>>> list = JsonUtil.parseJsonString(addGoodsPojo.getGoodsTypeClass(),List.class);
        int countSpec = 1;
        for (Map<String, List<String>> map : list) {
            for (String s : map.keySet()) {
                List list2 = map.get(s);
                countSpec = list2.size()*countSpec;
            }
        }
        if(countSpec != addGoodsPojo.getGoodsSpecPojo().size()){
            throw new ParamMismatchException();
        }
        String result = goodsService.addGoods(uid,addGoodsPojo.getGoodsClassify(),addGoodsPojo.getTitle(),addGoodsPojo.getCover(),addGoodsPojo.getDefaultPrice(),addGoodsPojo.getSlideshow(),addGoodsPojo.getPostage(),addGoodsPojo.getDetailLabel(),addGoodsPojo.getGoodsType(),addGoodsPojo.getGoodsTypeClass(),addGoodsPojo.getDetailPhotos(),addGoodsPojo.getGoodsSpecPojo());
        if(result == null){
            return JsonResult.success();
        }else{
            return JsonResult.failure(result);
        }
    }

    @RequestMapping("/putaway")
    @Merchant
    public JsonResult putaway(@RequestAttribute int uid, @Valid @RequestBody GidPojo gidPojo){
        String result = goodsService.putAway(uid,gidPojo.getGid());
        if(result == null){
            return JsonResult.success();
        }else{
            return JsonResult.failure(result);
        }
    }
}

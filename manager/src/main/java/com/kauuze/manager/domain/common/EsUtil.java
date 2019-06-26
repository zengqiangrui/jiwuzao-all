package com.kauuze.manager.domain.common;


import com.kauuze.manager.ConfigUtil;
import com.kauuze.manager.config.contain.SpringContext;
import com.kauuze.manager.domain.es.entity.Goods;
import com.kauuze.manager.include.HttpUtils;
import com.kauuze.manager.include.ObjectUtil;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.include.StringUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * elasticsearach 搜索 增加数值
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-21 19:37
 */
public class EsUtil {

    /**
     * 用于搜索
     * @param title
     * @param condition
     * @param sort
     * @return
     */
    public static PageDto<SearchGoodsSimpleDto> search(String title, Goods condition, Map<String,SortOrder> sort, int num, int size, Long pageTime){
        ElasticsearchTemplate elasticsearchTemplate = SpringContext.getBean(ElasticsearchTemplate.class);
        ExtResultMapper extResultMapper = SpringContext.getBean(ExtResultMapper.class);

        /**
         * 标题与分页
         */
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("title",title))
                .withHighlightFields(new HighlightBuilder.Field("title").preTags("<mark>").postTags("</mark>"))
                .withPageable(PageRequest.of(num-1,size));

        /**
         * 筛选条件
         */
        if(condition != null){
            List<Map<String,Object>> conditionInfo = ObjectUtil.getFiledsInfo(condition);
            for (Map<String, Object> stringObjectMap : conditionInfo) {
                if(stringObjectMap.get("value") != null){
                    nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery(String.valueOf(stringObjectMap.get("name"))
                            ,stringObjectMap.get("value")));
                }
            }
        }
        nativeSearchQueryBuilder.withQuery(QueryBuilders.rangeQuery("createTime").lte(pageTime));
        /**
         * 排序
         */
        if(sort != null){
            for (String s : sort.keySet()) {
                nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(s).order(sort.get(s)));
            }
        }
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("_id").order(SortOrder.DESC));

        Page<Goods> searchGoodss = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build()
                ,Goods.class,extResultMapper);
        List<Goods> list = searchGoodss.getContent();
        PageDto<SearchGoodsSimpleDto> pageDto = new PageDto<>();
        List<SearchGoodsSimpleDto> searchGoodsSimpleDtos = new ArrayList<>();
        for (Goods goods : list) {
            SearchGoodsSimpleDto searchGoodsSimpleDto = new SearchGoodsSimpleDto(goods.getGid(), goods.getTitle(), goods.getCover(), goods.getDefaultPrice(), goods.getPostage(), goods.getSalesVolume());
            searchGoodsSimpleDtos.add(searchGoodsSimpleDto);
        }
        pageDto.setTotal(searchGoodss.getTotalElements());
        pageDto.setContent(searchGoodsSimpleDtos);
        return pageDto;
    }

    /**
     * 增加某个字段数值
     */
    public static void inc(String gid,String field,Integer incNum){
        Map<String,Object> body = new HashMap<>();
        field = "ctx._source." + field;
        Map<String,String> inline = new HashMap<>();
        inline.put("inline",field + " = " + field + " + " + incNum);
        body.put("script",inline);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpUtils.post("http://" + ConfigUtil.elasticsearchNode + "/search_goods_" + ConfigUtil.customEnvironment + "/_doc/" + gid + "/_update",null,body,httpHeaders);
    }

    /**
     * 修改某个字段值
     * @param map
     */
    public static void modify(Map<String,String> map){
        Map<String,Object> body = new HashMap<>();
        StringBuilder inline = new StringBuilder("");
        for (String s : map.keySet()) {
            if(StringUtil.isEq(s,"gid")){
                continue;
            }
            inline = inline.append("ctx._source." + s  + " = '" + map.get(s) + "';");
        }
        Map<String,String> script = new HashMap<>();
        script.put("inline",inline.toString());
        body.put("script",script);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpUtils.post("http://" + ConfigUtil.elasticsearchNode + "/search_goods_" + ConfigUtil.customEnvironment + "/_doc/" + map.get("gid") + "/_update",null,body,httpHeaders);
    }
}

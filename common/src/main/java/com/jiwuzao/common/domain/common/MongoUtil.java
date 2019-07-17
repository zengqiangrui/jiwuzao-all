package com.jiwuzao.common.domain.common;


import com.jiwuzao.common.config.contain.SpringContext;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.ObjectUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Map;

/**
 * mongodb求和、更新、增加数值
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-15 02:56
 */
public class MongoUtil {
    /**
     * 更新不为空的对象
     * @param entity
     * @param type
     */
    public static <T> T updateNotNon(String queryKey,T entity,Class<T> type){
        MongoTemplate mongoTemplate = SpringContext.getBean(MongoTemplate.class);
        Query query = new Query();
        Object queryValue = ObjectUtil.getValueByPropName(entity,queryKey);
        if(queryKey == null){
            return null;
        }
        query.addCriteria(Criteria.where(queryKey).is(queryValue));
        Map<String,Object> map = JsonUtil.copy(entity,Map.class);
        Update update = new Update();
        for (String s : map.keySet()) {
            if (map.get(s) != null){
                update.set(s,map.get(s));
            }
        }
        return mongoTemplate.findAndModify(query,update,type);
    }

    /**
     * 更新Map内的值:值的类型严格
     * @param queryKey
     * @param queryValue
     * @param entity
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T updateMapStrictType(String queryKey, Object queryValue, Map<String,Object> entity, Class<T> type){
        MongoTemplate mongoTemplate = SpringContext.getBean(MongoTemplate.class);
        Query query = new Query();
        query.addCriteria(Criteria.where(queryKey).is(queryValue));
        Update update = new Update();
        for (String s : entity.keySet()) {
            update.set(s,entity.get(s));
        }
        return mongoTemplate.findAndModify(query,update,type);
    }

    /**
     * 增加filed的值
     * @param queryKey
     * @param queryValue
     * @param filed
     * @param num
     * @param type
     * @param <T>
     */
    public static <T> void incNumStrictType(String queryKey, Object queryValue, String filed,Integer num, Class<T> type){
        MongoTemplate mongoTemplate = SpringContext.getBean(MongoTemplate.class);
        Query query = new Query();
        query.addCriteria(Criteria.where(queryKey).is(queryValue));
        Update update = new Update();
        update.inc(filed,num);
        mongoTemplate.findAndModify(query,update,type);
    }

    public static <T> Object sum(String field, Class<T> type){
        MongoTemplate mongoTemplate = SpringContext.getBean(MongoTemplate.class);
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("id").sum(field).as("sum"));
        AggregationResults<Map> max = mongoTemplate.aggregate(aggregation, type.getName().toLowerCase(),Map.class);
        return max.iterator().next().get("sum");
    }
}

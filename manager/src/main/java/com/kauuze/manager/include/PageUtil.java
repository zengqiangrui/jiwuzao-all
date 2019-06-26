package com.kauuze.manager.include;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分页工具
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-28 20:09
 */
public class PageUtil {

    /**
     * 获取最新插入数据
     * @param size
     * @return
     */
    public static Pageable getNewsInsert(int page,int size){
        return PageRequest.of(page - 1,size,new Sort(Sort.Direction.DESC,"createTime"));
    }

    /**
     * 获取最旧插入数据
     * @return
     */
    public static Pageable getOldestInsert(int page,int size){
        return PageRequest.of(page - 1,size,new Sort(Sort.Direction.ASC,"createTime"));
    }

    /**
     * 默认排序
     * @param page
     * @param size
     * @return
     */
    public static Pageable getPageable(int page,int size){
        return PageRequest.of(page - 1,size);
    }

    /**
     * 按某字段升降排列
     * @param page
     * @param size
     * @param sortName
     * @return
     */
    public static Pageable getPageableAsc(int page, int size, String sortName){
        Sort sort = new Sort(Sort.Direction.ASC,sortName);
        return PageRequest.of(page - 1,size,sort);
    }

    /**
     * 按某字段降序排列
     * @param page
     * @param size
     * @param sortName
     * @return
     */
    public static Pageable getPageableDesc(int page ,int size, String sortName){
        Sort sort = new Sort(Sort.Direction.DESC,sortName);
        return PageRequest.of(page - 1,size,sort);
    }

    /**
     * 多字段联合排序
     *
     * List< PayOrder> orders=new ArrayList< PayOrder>();
     * orders.add( new PayOrder(Direction. ASC, "c"));
     * orders.add( new PayOrder(Direction. DESC, "d"));
     * @param page
     * @param size
     * @param orders
     * @return
     */
    public static Pageable getPageable(int page, int size, List<Sort.Order> orders){
        return PageRequest.of(page - 1, size, Sort.by(orders));
    }


    /**
     * 获取排序Sort,用于不需要分页
     * @param field
     * @return
     */
    public static Sort getSort(String field,boolean asc){
        if(asc){
            return new Sort(Sort.Direction.ASC,field);
        }else{
            return new Sort(Sort.Direction.DESC,field);
        }
    }

    /**
     * 降序取第一条
     * @param field
     * @return
     */
    public static Pageable getMaxOne(String field){
        return PageRequest.of(0, 1, Sort.by(new Sort.Order(Sort.Direction.DESC,field)));
    }

    /**
     * 升序取第一条
     * @param field
     * @return
     */
    public static Pageable getMinOne(String field){
        return PageRequest.of(0, 1, Sort.by(new Sort.Order(Sort.Direction.ASC,field)));
    }
}

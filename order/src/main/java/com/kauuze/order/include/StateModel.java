package com.kauuze.order.include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用于返回 参数对应内容 的类型
 * @author kauuze
 * @createTime 2019.01.20 20:40
 * @IDE IntelliJ IDEA 2017.2.5
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StateModel {
    /**
     * 状态
     */
    private String state;
    /**
     * 状态对应内容
     */
    private Object data;

    public StateModel(String state) {
        this.state = state;
    }
    public boolean eq(String state){
        return StringUtil.isEq(state,this.state);
    }
    public String toJsonString(){
        return JsonUtil.toJsonString(this);
    }
}

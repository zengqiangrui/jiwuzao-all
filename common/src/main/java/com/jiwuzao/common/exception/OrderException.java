package com.jiwuzao.common.exception;

import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
    private Integer code;

    public OrderException (OrderExceptionEnum orderException){
        super(orderException.getMsg());
        this.code = orderException.getCode();
    }

    public OrderException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}

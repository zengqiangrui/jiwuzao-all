package com.jiwuzao.common.exception;

import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import lombok.Getter;

@Getter
public class StoreException extends RuntimeException {
    private Integer code;

    public StoreException(StoreExceptionEnum storeException){
        super(storeException.getMsg());
        this.code = storeException.getCode();
    }

    public StoreException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}

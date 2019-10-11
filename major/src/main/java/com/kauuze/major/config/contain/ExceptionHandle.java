package com.kauuze.major.config.contain;


import com.jiwuzao.common.domain.mongo.entity.Log;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.include.DateTimeUtil;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.StateModel;
import com.kauuze.major.domain.mongo.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {
    @Autowired
    private LogRepository logRepository;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String handle(Exception e, HttpServletResponse response, HttpServletRequest request) {
        response.setCharacterEncoding("utf-8");
        if (e instanceof MethodArgumentNotValidException || e instanceof HttpMessageNotReadableException || e instanceof ParamMismatchException) {
            StateModel stateModel = new StateModel();
            stateModel.setState("param mismatch");
            try {
                response.setStatus(400);
                response.getWriter().write(stateModel.toJsonString());
                return null;
            } catch (IOException e1) {
                return null;
            }
        }

        //处理订单异常
        if (e instanceof OrderException) {
            try {
                Log log = new Log();
                log.setService("orderService").setCreateTime(System.currentTimeMillis()).setLog(e.getMessage()).setError(true).setErrCode(((OrderException) e).getCode());
                logRepository.save(log);
                response.setStatus(505);
                StateModel stateModel = new StateModel();
                stateModel.setState("order exception");
                stateModel.setData(e.getMessage());
                response.getWriter().write(stateModel.toJsonString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                logRepository.save(new Log(null, System.currentTimeMillis(), request.getRequestURL().toString() + "\r\n" + stringWriter.toString(), "major", true, null, DateTimeUtil.covertDateView(System.currentTimeMillis())));
                response.setStatus(500);
                StateModel stateModel = new StateModel();
                stateModel.setState("service wrong");
                stateModel.setData(e.getMessage());
                response.getWriter().write(stateModel.toJsonString());
            } catch (Exception e1) {
                return null;
            }
        }
        return null;
    }

//    @ExceptionHandler(value = OrderException.class)
//    @ResponseBody
//    public JsonResult showOrderException(OrderException e){
//        Log log = new Log();
//        log.setService("orderService").setCreateTime(System.currentTimeMillis()).setLog(e.getMessage()).setError(true).setErrCode(e.getCode());
//        logRepository.save(log);
//        return JsonResult.error(e.getCode(),e.getMessage());
//    }
}
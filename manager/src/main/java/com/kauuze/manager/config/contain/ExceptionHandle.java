package com.kauuze.manager.config.contain;


import com.jiwuzao.common.domain.mongo.entity.Log;
import com.kauuze.manager.domain.mongo.repository.LogRepository;
import com.kauuze.manager.include.DateTimeUtil;
import com.kauuze.manager.include.StateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class ExceptionHandle {
    @Autowired
    private LogRepository logRepository;
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String handle(Exception e, HttpServletResponse response, HttpServletRequest request) {
        if(e instanceof MethodArgumentNotValidException){
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
        if(e instanceof HttpMessageNotReadableException){
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
        try {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            logRepository.save(new Log(null, System.currentTimeMillis(),request.getRequestURL().toString() + "\r\n" + stringWriter.toString(), "major",true, DateTimeUtil.covertDateView(System.currentTimeMillis())));
            response.setStatus(500);
            StateModel stateModel = new StateModel();
            stateModel.setState("service wrong");
            response.getWriter().write(stateModel.toJsonString());
        } catch (Exception e1) {
            return null;
        }
        return null;
    }
}
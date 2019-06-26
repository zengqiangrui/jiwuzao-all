package com.kauuze.major.config.contain;


import com.kauuze.major.config.permission.GreenWay;
import com.kauuze.major.domain.mongo.repository.LogRepository;
import com.kauuze.major.include.StateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局错误拦截
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@RestController
public class HandlerController implements ErrorController {
  @Autowired
  private LogRepository logRepository;
  @RequestMapping("/error")
  @GreenWay
  public void error(HttpServletResponse response) {
    try {
      int status = response.getStatus();
      StateModel stateModel = new StateModel();
      if(status == 404){
        stateModel.setState("not find");
        response.getWriter().write(stateModel.toJsonString());
        return;
      }else if(status == 400){
        stateModel.setState("param mismatch");
        response.getWriter().write(stateModel.toJsonString());
        return;
      } else if(status == 415){
        stateModel.setState("Content-Type mismatch");
        response.getWriter().write(stateModel.toJsonString());
        return;
      } else {
        response.setStatus(500);
        stateModel.setState("service wrong");
        response.getWriter().write(stateModel.toJsonString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}

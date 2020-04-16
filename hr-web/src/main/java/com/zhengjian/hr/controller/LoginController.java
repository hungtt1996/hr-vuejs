package com.zhengjian.hr.controller;

import com.zhengjian.hr.common.pojo.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author cxy35
 * @Date 2019/12/20 12:45
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    // 1.use@CrossOrigin("*") // 2.use.authenticationEntryPoint，seeMyWebSecurityConfigurerAdapter
    public RespBean login() {
        return RespBean.error("please log in first！");
    }
}

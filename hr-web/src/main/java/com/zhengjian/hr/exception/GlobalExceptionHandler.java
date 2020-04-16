package com.zhengjian.hr.exception;

import com.zhengjian.hr.common.pojo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException e) {
        e.printStackTrace();
        RespBean respBean = RespBean.error("operation failed，Database exception!");
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return respBean.setMsg("operation failed，The data has associated data!");
        }
        return respBean;
    }
}

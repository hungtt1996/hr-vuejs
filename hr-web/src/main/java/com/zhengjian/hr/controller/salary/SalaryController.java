package com.zhengjian.hr.controller.salary;

import com.zhengjian.hr.common.pojo.RespBean;
import com.zhengjian.hr.model.Salary;
import com.zhengjian.hr.service.salary.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cxy35
 * @Date 2020/1/23 15:57
 */
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {
    @Autowired
    SalaryService salaryService;

    @PostMapping("/add")
    public RespBean add(@RequestBody Salary salary) {
        int r = salaryService.add(salary);
        if (r == 1) {
            return RespBean.ok("added successfully");
        } else {
            return RespBean.error("Add failed");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public RespBean deleteById(@PathVariable Integer id) {
        int r = salaryService.deleteById(id);
        if (r == 1) {
            return RespBean.ok("successfully deleted");
        } else {
            return RespBean.error("failed to delete");
        }
    }

    @DeleteMapping("/deleteByIds")
    public RespBean deleteByIds(Integer[] ids) {
        int r = salaryService.deleteByIds(ids);
        if (r == ids.length) {
            return RespBean.ok("successfully deleted");
        } else {
            return RespBean.error("failed to delete");
        }
    }

    @PutMapping("/edit")
    public RespBean edit(@RequestBody Salary salary) {
        int r = salaryService.edit(salary);
        if (r == 1) {
            return RespBean.ok("Edit successfully");
        } else {
            return RespBean.error("Edit failed");
        }
    }

    @GetMapping("/getListAll")
    public List<Salary> getListAll() {
        return salaryService.getListAll();
    }

}

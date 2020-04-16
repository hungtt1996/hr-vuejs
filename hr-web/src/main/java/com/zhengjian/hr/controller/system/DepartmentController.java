package com.zhengjian.hr.controller.system;

import com.zhengjian.hr.common.pojo.RespBean;
import com.zhengjian.hr.model.Department;
import com.zhengjian.hr.service.system.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cxy35
 * @Date 2020/1/6 14:37
 */
@RequestMapping("/system/basic/department")
@RestController
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @PostMapping("/add")
    public RespBean add(@RequestBody Department department) {
        int r = departmentService.add(department);
        if (r == 1) {
            return RespBean.ok("added successfully", department);
        } else {
            return RespBean.error("Add failed");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public RespBean deleteById(@PathVariable Integer id) {
        int r = departmentService.deleteById(id);
        if (r == 1) {
            return RespBean.ok("successfully deleted");
        } else if (r == -1) {
            return RespBean.error("failed to delete，There are sub-departments under this department");
        } else if (r == -2) {
            return RespBean.error("failed to delete，There are employees under this department");
        } else {
            return RespBean.error("failed to delete");
        }
    }

    @PutMapping("/edit")
    public RespBean edit(@RequestBody Department department) {
        int r = departmentService.edit(department);
        if (r == 1) {
            return RespBean.ok("Edit successfully");
        } else {
            return RespBean.error("Edit failed");
        }
    }

    @GetMapping("/getListAllWithChildren")
    public List<Department> getListAllWithChildren() {
        return departmentService.getListAllWithChildren();
    }
}

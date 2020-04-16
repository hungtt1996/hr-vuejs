package com.zhengjian.hr.controller.system;

import com.zhengjian.hr.common.pojo.RespBean;
import com.zhengjian.hr.model.User;
import com.zhengjian.hr.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cxy35
 * @Date 2020/1/8 15:05
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    UserService userService;

    @DeleteMapping("/deleteById/{id}")
    public RespBean deleteById(@PathVariable Integer id) {
        int r = userService.deleteById(id);
        if (r == 1) {
            return RespBean.ok("successfully deleted");
        } else {
            return RespBean.error("failed to delete");
        }
    }

    @PutMapping("/edit")
    public RespBean edit(@RequestBody User user) {
        int r = userService.edit(user);
        if (r == 1) {
            return RespBean.ok("Edit successfully");
        } else {
            return RespBean.error("Edit failed");
        }
    }

    @PutMapping("/editUserRole")
    public RespBean editUserRole(Integer uid, Integer[] rids) {
        boolean r = userService.editUserRole(uid, rids);
        if (r) {
            return RespBean.ok("Edit successfully");
        } else {
            return RespBean.error("Edit failed");
        }
    }

    @GetMapping("/getListWithRoles")
    public List<User> getListWithRoles(User user) {
        return userService.getListWithRoles(user);
    }

}

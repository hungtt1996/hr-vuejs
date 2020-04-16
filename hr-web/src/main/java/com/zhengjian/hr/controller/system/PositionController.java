package com.zhengjian.hr.controller.system;

import com.zhengjian.hr.common.pojo.RespBean;
import com.zhengjian.hr.model.Position;
import com.zhengjian.hr.service.system.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cxy35
 * @Date 2020/1/1 13:55
 */
@RequestMapping("/system/basic/position")
@RestController
public class PositionController {
    @Autowired
    PositionService positionService;

    @PostMapping("/add")
    public RespBean add(@RequestBody Position position) {
        int r = positionService.add(position);
        if (r == 1) {
            return RespBean.ok("added successfully");
        } else {
            return RespBean.error("Add failed");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public RespBean deleteById(@PathVariable Integer id) {
        int r = positionService.deleteById(id);
        if (r == 1) {
            return RespBean.ok("successfully deleted");
        } else {
            return RespBean.error("failed to delete");
        }
    }

    @DeleteMapping("/deleteByIds")
    public RespBean deleteByIds(Integer[] ids) {
        int r = positionService.deleteByIds(ids);
        if (r == ids.length) {
            return RespBean.ok("successfully deleted");
        } else {
            return RespBean.error("failed to delete");
        }
    }

    @PutMapping("/edit")
    public RespBean edit(@RequestBody Position position) {
        int r = positionService.edit(position);
        if (r == 1) {
            return RespBean.ok("Edit successfully");
        } else {
            return RespBean.error("Edit failed");
        }
    }

    @GetMapping("/getListAll")
    public List<Position> getListAll() {
        return positionService.getListAll();
    }

}

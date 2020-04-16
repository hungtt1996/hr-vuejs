package com.zhengjian.hr.mapper;

import com.zhengjian.hr.model.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User loadUserByUsername(String username);

    /**
     * according to user Query object list。If specified id ，then sql Increase in != id ，Exclude current user。
     *
     * @param user The object of the query，for null Then query all objects
     * @return Result object list
     */
    List<User> getList(User user);

    /**
     * according to user Query object list。If specified id ，then sql Increase in != id ，Exclude current user。
     *
     * @param user The object of the query，for null Then query all objects
     * @return Result object list
     */
    List<User> getListWithRoles(User user);
}

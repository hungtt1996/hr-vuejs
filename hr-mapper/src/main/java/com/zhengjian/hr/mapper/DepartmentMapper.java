package com.zhengjian.hr.mapper;

import com.zhengjian.hr.model.Department;

import java.util.List;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    List<Department> getListAll();

    List<Department> getListWithChildrenByParentId(Integer parentId);

    /**
     * according to departmentSearch（ id except）Query records
     *
     * @param department The object of the query（ id except），for null Then query the total number of objects
     * @return Records
     */
    Long count(Department department);

    /**
     * By calling a stored procedure。
     * Unused，Has been changed toserviceachieve
     *
     * @param department
     */
    void add(Department department);

    /**
     * By calling a stored procedure。
     * Unused，Has been changed toserviceachieve
     * @param department
     */
    void deleteById(Department department);
}

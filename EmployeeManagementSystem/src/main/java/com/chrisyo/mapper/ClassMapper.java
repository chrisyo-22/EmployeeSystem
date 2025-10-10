package com.chrisyo.mapper;


import com.chrisyo.entity.Class;
import com.chrisyo.entity.ClassQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ClassMapper {

//    @Select("select id, name, room, begin_date, end_date, master_id, subject, create_time, update_time from class")
    List<Class> list(ClassQueryParam classQueryParam);

    @Delete("delete from class where id = #{id}")
    void delete(Integer id);

    @Update("update student set class_id = null where class_id = #{id}")
    void deleteStudentClassId(Integer id);

    void add(Class classObj);

    Class getClassById(Integer id);


    void update(Class classObj);



}

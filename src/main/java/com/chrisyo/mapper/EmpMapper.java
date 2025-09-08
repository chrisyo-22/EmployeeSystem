package com.chrisyo.mapper;

import com.chrisyo.entity.EmpQueryParam;
import com.chrisyo.entity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface EmpMapper {

//    /**
//     * Counts the total number of records in the corresponding database table.
//     *
//     * @return the total number of records as a Long
//     */
    //Long count();


//    /**
//     * Retrieves a paginated list of employees starting from a specific index with a specified page size.
//     *
//     * @param start_index the starting index of the records to retrieve
//     * @param pageSize the number of records to retrieve starting from the specified index
//     * @return a list of employees corresponding to the specified pagination parameters
//     */
    //List<Employee> page(Integer start_index, Integer pageSize);

    //Used pageHelper plugging
    List<Employee> list(EmpQueryParam empQueryParam);

    //    @Insert("insert into emp values(null, #{username}, #{password}, #{name}," +
//            "#{gender}, #{phone}, #{job},#{salary}, #{image}," +
//            " #{entryDate},#{deptId}, #{createTime}, #{updateTime})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    //if you used Annotation here, you can't use XML
    void insert(Employee emp);

    @Delete("delete from emp where id in (${ids})")
    void delete(Integer[] ids);

}


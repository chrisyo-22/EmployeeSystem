package com.chrisyo.mapper;

import com.chrisyo.entity.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    void insertBatch(List<EmpExpr> exprList);

    void deleteBatch(List<Integer> empIds);

    void deleteByEmpId(Integer id);
}

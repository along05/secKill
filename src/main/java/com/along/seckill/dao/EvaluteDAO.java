package com.along.seckill.dao;


import com.along.seckill.entity.Evaluate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EvaluteDAO {

    List<Evaluate> findEvalutesByGid(@Param("gid") Long gid);
}

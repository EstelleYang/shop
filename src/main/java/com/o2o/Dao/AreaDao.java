package com.o2o.Dao;

import com.o2o.DO.AreaDO;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface AreaDao {
    //列出区域列表
    public List<AreaDO>queryAreaList();
}

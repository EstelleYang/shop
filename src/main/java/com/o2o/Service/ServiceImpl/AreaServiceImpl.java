package com.o2o.Service.ServiceImpl;

import com.o2o.DO.AreaDO;
import com.o2o.Dao.AreaDao;
import com.o2o.Service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
    @Override
    public List<AreaDO> getAreaList() {
        return areaDao.queryAreaList();
    }
}

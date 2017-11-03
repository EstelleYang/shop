package com.o2o.Dao;

import com.o2o.BaseTest;
import com.o2o.DO.AreaDO;
import junit.runner.BaseTestRunner;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaDao areaDao;
    @Test
    public void queryAreaTest(){
        List<AreaDO>list = areaDao.queryAreaList();
        assertEquals(2,list.size());
    }
}

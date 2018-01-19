package com.o2o.Dao;

import com.o2o.BaseTest;
import com.o2o.DO.AreaDO;
import com.o2o.Util.PathUtil;
import junit.runner.BaseTestRunner;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaDao areaDao;
    @Test
    public void queryAreaTest(){
        List<AreaDO>list = areaDao.queryAreaList();
        assertEquals(2,list.size());
    }
    public int queue(int[]arr,int item){
        if(arr.length==0){
            arr[0]=item;
            return item;
        }else{
        int result = arr[0];
        for(int i = 0;i<(arr.length)-1;i++){
            arr[i]=arr[i+1];
        }
         arr[arr.length-1] = item;
         return result;
         }

    }

    @Test
    public void test(){
        PathUtil pathUtil = new PathUtil();
        System.out.print(pathUtil.timeChange(6));
        Date t1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(t1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        System.out.println(cal.getTime());
        int[]arr = new int[10];
        System.out.print(queue(arr,7)+" ");
        for(int a:arr){
            System.out.print(a+" ");
        }
    }

}

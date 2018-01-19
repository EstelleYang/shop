package com.o2o.Dao;

import com.o2o.BaseTest;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 496934  Date: 2017/12/28 Time: 9:35
 */
public class JedisTest extends BaseTest{
    @Test
    public void test(){
        Jedis jedis = new Jedis("192.168.146.130",6380);
        String r = jedis.get("a6688");

        System.out.print(r);
        jedis.close();
    }
    @Test //池化
    public void shared(){
        //构造一个池的配置对象
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        //存储配置的信息
        List<JedisShardInfo> listInfo=new ArrayList<JedisShardInfo>();

        JedisShardInfo info1=new JedisShardInfo("192.168.146.130", 6379);
        listInfo.add(info1);
        JedisShardInfo info2=new JedisShardInfo("192.168.146.130", 6380);
        listInfo.add(info2);
        JedisShardInfo info3=new JedisShardInfo("192.168.146.130", 6379);
        listInfo.add(info3);
        ShardedJedisPool pool=new ShardedJedisPool(poolConfig, listInfo);

        ShardedJedis jedis=pool.getResource();

        for(int i=0;i<10000;i++){
            jedis.set("a"+i,"i");
        }
//      String r=jedis.get("a");
//      System.out.println(r);
        jedis.close();

    }
}
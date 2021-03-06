package com.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring和Junit整合，Junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-mybatis.xml"})
public class BaseTest {

}

package com.o2o.Service;

import com.o2o.DO.AreaDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AreaService {
    List<AreaDO>getAreaList();
}

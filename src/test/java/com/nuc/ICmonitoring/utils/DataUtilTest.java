package com.nuc.ICmonitoring.utils;

import com.nuc.ICmonitoring.mapper.HeatRecordMapper;
import com.nuc.ICmonitoring.pojo.HeatRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName DataUtilTest
 * @Deacription
 * @Author DeepBlue
 * @Date 2020/11/25 11:26
 * @Version 1.0
 **/
@SpringBootTest
class DataUtilTest {
    @Autowired
    DataUtil dataUtil;

    @Autowired
    HeatRecordMapper mapper;

    @Test
    public void testData() {
        HeatRecord heatRecord = dataUtil.generateRecord();
        mapper.insert(heatRecord);
        System.out.println(heatRecord);
    }
}
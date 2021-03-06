package com.nuc.ICmonitoring.mapper;

import cn.hutool.core.date.DateUtil;
import com.nuc.ICmonitoring.pojo.HeatRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @ClassName HeatRecordMapperTest
 * @Deacription
 * @Author DeepBlue
 * @Date 2020/11/28 15:14
 * @Version 1.0
 **/
@SpringBootTest
@Slf4j
class HeatRecordMapperTest {
    @Autowired
    HeatRecordMapper mapper;

    @Test
    public void sqlTest() {
        List<HeatRecord> records = mapper.getDateWithFixRate(5, DateUtil.parse("2020-11-27 15:30:55"),DateUtil.parse("2020-11-27 16:00:30"), "中北大学站");
        for (HeatRecord h:records) {
            log.info(String.valueOf(h));
        }
    }
}
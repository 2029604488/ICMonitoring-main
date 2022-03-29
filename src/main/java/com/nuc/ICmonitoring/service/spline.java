package com.nuc.ICmonitoring.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nuc.ICmonitoring.mapper.HeatRecordMapper;
import com.nuc.ICmonitoring.pojo.HeatRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class spline {
    @Autowired
    HeatRecordMapper heatmapper;

    @Value("${data.timeout}")
    private Integer timeout;

    private final ScheduledExecutorService scheduledExecutorService;

    public spline() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }
    @PostConstruct
    public void scan() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ArrayList<Double> nums = new ArrayList<>();//nums里边存的就是拟合的数据源，直接对nums操作即可
            List<HeatRecord> head = heatmapper.selectList(new QueryWrapper<>());
            for(HeatRecord ch:head){
                nums.add(ch.getOneSupplyTemperature());
            }
            for(Double ch:nums){

            }
        }, 5, timeout, TimeUnit.SECONDS);
    }
//拟合完数据，可以输出函数，或者把拟合结果取数据插入test中

}

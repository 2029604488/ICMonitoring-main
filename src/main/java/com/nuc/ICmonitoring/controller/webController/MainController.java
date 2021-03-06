package com.nuc.ICmonitoring.controller.webController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuc.ICmonitoring.mapper.HeatRecordMapper;
import com.nuc.ICmonitoring.mapper.StationInfoMapper;
import com.nuc.ICmonitoring.mapper.UsersMapper;
import com.nuc.ICmonitoring.pojo.*;
import com.nuc.ICmonitoring.service.interfaces.UserService;
import com.nuc.ICmonitoring.utils.JwtUtils;
import com.nuc.ICmonitoring.utils.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author DeepBlue
 * @ClassName MainController
 * @Deacription
 * @Date 2020/11/24 16:59
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("api")
@Slf4j
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private HeatRecordMapper mapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private StationInfoMapper stationInfoMapper;




    @GetMapping("getRecord")
    public ResultBody getRecord(String station, Integer cur, Integer pageSize,
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        log.error("{} {} {} {} {}", station, cur, pageSize, start, end);
        Page<HeatRecord> heatRecordPage = mapper.selectPage(new Page<>(cur, pageSize, mapper.selectCount(
                new QueryWrapper<HeatRecord>().eq("heat_exchange_station", station).orderByDesc("time").between("time", start, end))),
                new QueryWrapper<HeatRecord>().eq("heat_exchange_station", station).orderByDesc("time").between("time", start, end));
        return ResultBody.success(heatRecordPage);
    }

    //??????????????????????????????????????????
    @GetMapping("getRecordWithFixRate")
    public ResultBody getRecordWithFixRate(String station, Integer gap,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        List<HeatRecord> dateWithFixRate = mapper.getDateWithFixRate(gap, start, end, station);
        return ResultBody.success(dateWithFixRate);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    @PostMapping("login")
    public ResultBody login(@RequestBody User user) {
        try {
            Map<String, String> map = new HashMap<>();
            User login = userService.login(user);
            map.put("identity", "admin");
            if (login == null) {
                Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("username", user.getUsername()).eq("password", user.getPassword()));
                if (users == null) {
                    throw new RuntimeException("?????????????????????????????????");
                }
                map.put("identity", "user");
            }
            map.put("username", user.getUsername());
            String token = jwtUtils.getToken(map);
            return ResultBody.success(token);
        } catch (RuntimeException e) {
            log.warn("?????????????????????????????????");
            return ResultBody.error(e.getMessage());
        }
    }

    /**
     * @date: 2020/11/28 21:23
     * @description:
     */
    @GetMapping("getStationsRunning")
    public ResultBody getStations() {
        List<StationInfo> stationInfos = stationInfoMapper.selectList(new QueryWrapper<StationInfo>().select("station_name").eq("station_state", "1"));
        ArrayList<String> strings = new ArrayList<>();
        for (StationInfo info : stationInfos) {
            strings.add(info.getStationName());
        }
        return ResultBody.success(strings);
    }


    @GetMapping("getAllStationInfo")
    public ResultBody getAllStationInfo() {
        List<StationInfo> infos = stationInfoMapper.selectList(new QueryWrapper<>());
        return ResultBody.success(infos);
    }




    @PostMapping("addStation")
    public ResultBody addStation(@RequestBody StationInfo stationInfo) {
        stationInfoMapper.insert(stationInfo);
        return ResultBody.success("?????????");
    }

    @PostMapping("deleteStation")
    public ResultBody deleteStation(@RequestBody StationInfo stationInfo) {
        int id = stationInfoMapper.deleteById(stationInfo.getId());
        if (id > 0) {
            return ResultBody.success("?????????");
        } else {
            return ResultBody.error("???????????????");
        }
    }


    @PostMapping("updateStation")
    public ResultBody updateUser(@RequestBody StationInfo stationInfo) {
        stationInfoMapper.updateById(stationInfo);
        return ResultBody.success("?????????");
    }

    @GetMapping("getAllUsers")
    public ResultBody getAllUser() {
        List<Users> users = usersMapper.selectList(new QueryWrapper<Users>().select("username", "name", "id", "belong", "phone"));
        return ResultBody.success(users);
    }

    @PostMapping("addUsers")
    public ResultBody addUser(@RequestBody Users users) {
        usersMapper.insert(users);
        return ResultBody.success("?????????");
    }

    @PostMapping("deleteUsers")
    public ResultBody deleteUser(@RequestBody Users users) {
        int id = usersMapper.deleteById(users.getId());
        if (id > 0) {
            return ResultBody.success("?????????");
        } else {
            return ResultBody.error("???????????????");
        }
    }

    @PostMapping("updateUsers")
    public ResultBody updateUser(@RequestBody Users users) {
        usersMapper.updateById(users);
        return ResultBody.success("?????????");
    }

    @GetMapping("getMenu")
    public ResultBody getMenu(HttpServletRequest request) {
        if ("admin".equals(jwtUtils.getIdentity(request))) {
            return ResultBody.success(JSON.parse("[\n" +
                    "        {\n" +
                    "          index: 1, name: \"????????????\", lists: [\n" +
                    "            {index: 1, name: \"????????????\", path: \"heatData\"},\n" +
                    "            {index: 2, name: \"??????????????????\", path: \"chart\"},\n" +
                    "            {index: 3, name: \"??????????????????\", path: \"dynamic\"},\n" +
                    "          ]\n" +
                    "        }," +
                    "     \n" +
                    "        {\n" +
                    "          index: 2, name: \"??????????????????\", lists: [\n" +
                    "            {index: 4, name: \"????????????\", path: \"user\"},\n" +
                    "          ]\n" +
                    "        }, " +
                    "         {\n" +
                    "          index: 3, name: \"????????????\", lists: [\n" +
                    "            {index: 5, name: \"????????????\", path: \"manage\"},\n" +
                    "          ]\n" +
                    "        }," +
                            "         {\n" +
                            "          index: 4, name: \"other\", lists: [\n" +
                            "            {index: 6, name: \"other\", path: \"column\"},\n" +
                            "          ]\n" +
                            "        },"+
                    "         {\n" +
                    "          index: 5, name: \"??????????????????\", lists: [\n" +
                    "            {index: 7, name: \"????????????????????????\", path: \"forecast\"},\n" +
                    "          ]\n" +
                    "        },"+
                    "    \n" +
                    "      ]"));
        } else {
            return ResultBody.success(JSON.parse("[\n" +
                    "    {\n" +
                    "      index: 1, name: \"????????????\", lists: [\n" +
                    "        {index: 1, name: \"????????????\", path: \"heatData\"},\n" +
                    "        {index: 2, name: \"??????????????????\", path: \"chart\"},\n" +
                    "        {index: 3, name: \"??????????????????\", path: \"dynamic\"},\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    ]"));
        }
    }
}

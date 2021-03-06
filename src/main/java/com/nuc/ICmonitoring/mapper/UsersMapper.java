package com.nuc.ICmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.ICmonitoring.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName UsersMapper
 * @Deacription
 * @Author DeepBlue
 * @Date 2020/11/28 23:51
 * @Version 1.0
 **/

@Mapper
public interface UsersMapper extends BaseMapper<Users> {
}
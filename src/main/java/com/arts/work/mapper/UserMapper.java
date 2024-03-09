package com.arts.work.mapper;

import com.arts.work.common.pojo.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserVO login(@Param("email") String email);

    void register(@Param("userId") String userId, @Param("email") String email);
}

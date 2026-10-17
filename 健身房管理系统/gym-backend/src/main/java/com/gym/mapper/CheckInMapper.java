package com.gym.mapper;

import com.gym.entity.CheckIn;
import com.gym.entity.GymPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 签到 仓库类
 *
 * @author: ShanZhu
 * @date: 2024-08-01
 */
@Mapper
@Repository
public interface CheckInMapper {

    int addCheckIn(CheckIn checkIn);

    CheckIn getCheckByMemberNo(CheckIn checkIn);

    List<GymPoint> countSince(@Param("from") LocalDate from);

    LocalDate latestByMember(@Param("memberNo") int memberNo);

}

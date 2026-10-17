package com.gym.service;

import com.gym.entity.Common;
import com.gym.entity.Member;
import com.gym.entity.Register;
import com.gym.mapper.CourseMapper;
import com.gym.mapper.MemberMapper;
import com.gym.mapper.RegisterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 报名 服务类
 *
 * @author: ShanZhu
 * @date: 2024-08-01
 */
@Service
public class RegisterService {

    @Resource
    private RegisterMapper registerMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private CourseMapper courseMapper;

    public List<Register> getAllRegister(int page, int size) {
        return registerMapper.getAllRegister(page, size);
    }


    public List<Register> getRegisterByMemberNo(int memberNo) {
        return registerMapper.getRegisterByMemberNo(memberNo);
    }


    public Map<String, Object> addRegister(Register register) {
        Map<String, Object> resultMap = new HashMap<>();
        String memberPhone = register.getMemberPhone();
        Member memberByPhone = registerMapper.getMemberByPhone(memberPhone);
        
        // 先检查会员是否存在
        if (memberByPhone == null) {
            resultMap.put("code", 400);
            resultMap.put("message", "会员不存在");
            return resultMap;
        }
        
        // 会员存在，继续检查是否已购买同期课程
        Register checkRegister = registerMapper.checkRegister(register.getCourseNo(), memberByPhone.getMemberNo());
        
        if (checkRegister != null) {
            resultMap.put("code", 400);
            resultMap.put("message", "已购买同期课程，请勿重复购买");
        } else {
            register.setMemberNo(memberByPhone.getMemberNo());
            double coursePriceByCourseNo = courseMapper.getCoursePriceByCourseNo(register.getCourseNo());
            Integer power = memberMapper.getMemberPower(register.getMemberNo());
            double payPrice = payPrice(coursePriceByCourseNo, power == null ? 0 : power);
            Double change = memberMapper.getMemberChange(register.getMemberNo());
            double memberChange = change == null ? 0 : change;
            if (memberChange + 0.001 >= payPrice) {
                int result = registerMapper.addRegister(register);

                if (result > 0) {
                    memberMapper.updateMemberChangeByMemberNo(register.getMemberNo(), payPrice);
                    resultMap.put("code", 200);
                    resultMap.put("message", "购买成功");
                } else {
                    resultMap.put("code", 400);
                    resultMap.put("message", "购买失败");
                }
            } else {
                resultMap.put("code", 400);
                resultMap.put("message", "购买失败,余额不足请充值余额后再试");
            }
        }
        
        return resultMap;
    }

    public Map<String, Object> updateRegister(Register register) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = registerMapper.updateRegister(register);

        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "修改成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "修改失败");
        }

        return resultMap;
    }

    public Map<String, Object> deleteRegister(int registerNo) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = registerMapper.deleteRegister(registerNo);

        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "删除成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "删除失败");
        }

        return resultMap;
    }

    public Common totalRegister() {
        return registerMapper.totalRegister();
    }

    public List<Register> getByKeywordRegister(String keyWord, int page, int size) {
        return registerMapper.getByKeywordRegister(keyWord, page, size);
    }

    public Common totalRegisterFuzzy(String keyWord) {
        return registerMapper.totalRegisterFuzzy(keyWord);
    }

    private double payPrice(double price, int power) {
        double rate = 1;
        if (power == 1) {
            rate = 0.9;
        } else if (power == 2) {
            rate = 0.8;
        } else if (power == 3) {
            rate = 0.7;
        } else if (power == 4) {
            rate = 0.6;
        }
        return Math.round(price * rate * 100.0) / 100.0;
    }

}

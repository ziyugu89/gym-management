package com.gym.service;

import com.gym.entity.Course;
import com.gym.entity.GymPoint;
import com.gym.entity.Register;
import com.gym.mapper.CheckInMapper;
import com.gym.mapper.CourseMapper;
import com.gym.mapper.RegisterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InsightService {

    @Resource
    private CheckInMapper checkInMapper;

    @Resource
    private RegisterMapper registerMapper;

    @Resource
    private CourseMapper courseMapper;

    public Map<String, Object> stats() {
        LocalDate start = LocalDate.now().minusDays(6);
        Map<String, Long> found = new HashMap<String, Long>();
        List<GymPoint> stored = checkInMapper.countSince(start);
        if (stored != null) {
            for (GymPoint point : stored) {
                found.put(point.getLabel(), point.getTotal() == null ? 0L : point.getTotal());
            }
        }
        List<GymPoint> days = new ArrayList<GymPoint>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = start.plusDays(i);
            GymPoint point = new GymPoint();
            point.setLabel(day.toString());
            point.setTotal(found.containsKey(day.toString()) ? found.get(day.toString()) : 0L);
            days.add(point);
        }
        List<GymPoint> courses = registerMapper.countCourseBuy();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("days", days);
        data.put("courses", courses == null ? Collections.emptyList() : courses);
        return data;
    }

    public Map<String, Object> recommend(int memberNo) {
        List<Course> courses = courseMapper.getAllCourse(0, 200);
        List<Register> owned = registerMapper.getRegisterByMemberNo(memberNo);
        Set<Integer> ownedNos = new HashSet<Integer>();
        if (owned != null) {
            for (Register register : owned) {
                if (register.getCourseNo() != null) {
                    ownedNos.add(register.getCourseNo());
                }
            }
        }
        List<Course> left = new ArrayList<Course>();
        if (courses != null) {
            for (Course course : courses) {
                if (course.getCourseNo() != null && !ownedNos.contains(course.getCourseNo())) {
                    left.add(course);
                }
            }
        }
        LocalDate latest = checkInMapper.latestByMember(memberNo);
        boolean away = latest == null || latest.isBefore(LocalDate.now().minusDays(14));
        if (away) {
            Collections.sort(left, new Comparator<Course>() {
                public int compare(Course leftCourse, Course rightCourse) {
                    return Double.compare(leftCourse.getCoursePrice(), rightCourse.getCoursePrice());
                }
            });
        }
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        int limit = Math.min(4, left.size());
        for (int i = 0; i < limit; i++) {
            Course course = left.get(i);
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("courseNo", course.getCourseNo());
            row.put("courseName", course.getCourseName());
            row.put("coursePrice", course.getCoursePrice());
            row.put("courseTime", course.getCourseTime());
            items.add(row);
        }
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        if (left.isEmpty()) {
            data.put("reason", courses == null || courses.isEmpty()
                    ? "还没有课程，无法推荐。"
                    : "已经购买了现有课程，暂无新的推荐。");
        } else if (away) {
            data.put("reason", "近14天没有到店签到，优先推荐价格较低、还没购买的课程。");
        } else {
            data.put("reason", "近期有到店签到，推荐还没购买的课程。");
        }
        data.put("items", items);
        return data;
    }
}

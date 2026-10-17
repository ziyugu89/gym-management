package com.gym.controller;

import com.gym.service.DeviceFeedService;
import com.gym.service.GymGuideService;
import com.gym.service.InsightService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class InsightController {

    @Resource
    private InsightService insightService;

    @Resource
    private DeviceFeedService deviceFeedService;

    @Resource
    private GymGuideService gymGuideService;

    @RequestMapping(path = "/gymStats")
    public Map<String, Object> gymStats() {
        return insightService.stats();
    }

    @RequestMapping(path = "/recommendCourse")
    public Map<String, Object> recommendCourse(int memberNo) {
        return insightService.recommend(memberNo);
    }

    @RequestMapping(path = "/deviceFeed")
    public Map<String, Object> deviceFeed(String kind) {
        return deviceFeedService.query(kind);
    }

    @RequestMapping(path = "/syncDeviceScans")
    public Map<String, Object> syncDeviceScans() {
        return deviceFeedService.syncScans();
    }

    @RequestMapping(path = "/gymGuide")
    public Map<String, Object> gymGuide(int memberNo, String question) {
        return gymGuideService.guide(memberNo, question);
    }
}

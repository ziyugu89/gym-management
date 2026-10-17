package com.gym.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.entity.CheckIn;
import com.gym.entity.Member;
import com.gym.mapper.CheckInMapper;
import com.gym.mapper.MemberMapper;
import com.gym.mapper.RegisterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫码签到和器材状态。文件为空时不编造记录，导入后即可查询。
 */
@Service
public class DeviceFeedService {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ObjectMapper json = new ObjectMapper();

    @Resource
    private CheckInMapper checkInMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private RegisterMapper registerMapper;

    public Map<String, Object> query(String kind) {
        String use = "scans".equals(kind) ? "scans" : "equipment";
        Loaded loaded = load();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("ready", loaded.ready);
        data.put("message", loaded.message);
        data.put("kind", use);
        data.put("equipment", loaded.equipment);
        data.put("scans", loaded.scans);
        data.put("items", "scans".equals(use) ? loaded.scans : loaded.equipment);
        data.put("counts", counts(loaded));
        data.put("skipped", loaded.skipped);
        data.put("path", loaded.path);
        return data;
    }

    public Map<String, Object> syncScans() {
        Loaded loaded = load();
        Map<String, Object> result = new HashMap<String, Object>();
        if (!loaded.ready || loaded.scans.isEmpty()) {
            result.put("code", 400);
            result.put("message", "尚未导入扫码记录，没有可写入的签到");
            return result;
        }
        int inserted = 0;
        int duplicated = 0;
        int unknown = 0;
        for (Map<String, Object> row : loaded.scans) {
            if (!Boolean.TRUE.equals(row.get("matched"))) {
                unknown++;
                continue;
            }
            CheckIn checkIn = new CheckIn();
            checkIn.setMemberNo(((Number) row.get("memberNo")).intValue());
            checkIn.setCheckDate(LocalDate.parse(String.valueOf(row.get("date"))));
            if (checkInMapper.getCheckByMemberNo(checkIn) != null) {
                duplicated++;
                continue;
            }
            if (checkInMapper.addCheckIn(checkIn) > 0) {
                inserted++;
            }
        }
        result.put("code", 200);
        result.put("message", "写入 " + inserted + " 条签到，" + duplicated + " 条当天已有记录，" + unknown + " 条没有对应会员");
        return result;
    }

    private Map<String, Integer> counts(Loaded loaded) {
        Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
        counts.put("equipment", loaded.equipment.size());
        counts.put("scans", loaded.scans.size());
        return counts;
    }

    private Loaded load() {
        Loaded loaded = new Loaded();
        Path path = feedPath();
        loaded.path = path.toAbsolutePath().normalize().toString();
        if (!Files.exists(path)) {
            loaded.message = "尚未导入扫码签到或器材状态。请把资料写入 device_feed/feed.json 后再刷新。";
            return loaded;
        }
        JsonNode root;
        try {
            root = json.readTree(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            loaded.message = "设备资料文件无法读取，请检查是否为 JSON。";
            return loaded;
        }
        if (root == null || !root.isObject()) {
            loaded.message = "尚未导入扫码签到或器材状态。请把资料写入 device_feed/feed.json 后再刷新。";
            return loaded;
        }
        loaded.equipment = readEquipment(root.path("equipment"), loaded);
        loaded.scans = readScans(root.path("scans"), loaded);
        if (loaded.equipment.isEmpty() && loaded.scans.isEmpty()) {
            loaded.message = "尚未导入扫码签到或器材状态。请把资料写入 device_feed/feed.json 后再刷新。";
            return loaded;
        }
        loaded.ready = true;
        loaded.message = "已读取导入资料。格式不对的记录已跳过，不会编造设备状态或签到。";
        return loaded;
    }

    private List<Map<String, Object>> readEquipment(JsonNode node, Loaded loaded) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (node == null || !node.isArray()) {
            return rows;
        }
        for (JsonNode item : node) {
            if (rows.size() >= 50) {
                break;
            }
            String name = text(item, "name");
            String status = text(item, "status");
            if (name.isEmpty() || !("在线".equals(status) || "离线".equals(status) || "维护".equals(status))) {
                loaded.skipped++;
                continue;
            }
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("name", name);
            row.put("status", status);
            row.put("place", text(item, "place"));
            row.put("updated", text(item, "updated"));
            row.put("note", text(item, "note"));
            if (item.hasNonNull("equipmentNo") && item.get("equipmentNo").canConvertToInt()) {
                row.put("equipmentNo", item.get("equipmentNo").asInt());
            } else {
                row.put("equipmentNo", "");
            }
            rows.add(row);
        }
        return rows;
    }

    private List<Map<String, Object>> readScans(JsonNode node, Loaded loaded) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (node == null || !node.isArray()) {
            return rows;
        }
        for (JsonNode item : node) {
            if (rows.size() >= 50) {
                break;
            }
            LocalDateTime when = time(item);
            if (when == null) {
                loaded.skipped++;
                continue;
            }
            Member member = findMember(item);
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("time", when.format(DATE_TIME));
            row.put("date", when.toLocalDate().toString());
            row.put("device", text(item, "device"));
            if (member == null) {
                row.put("matched", false);
                row.put("memberNo", item.hasNonNull("memberNo") ? item.get("memberNo").asText() : "");
                row.put("memberName", text(item, "memberPhone"));
                row.put("result", "没有对应会员");
            } else {
                row.put("matched", true);
                row.put("memberNo", member.getMemberNo());
                row.put("memberName", member.getMemberName() == null ? member.getMemberPhone() : member.getMemberName());
                row.put("result", "可写入签到");
            }
            rows.add(row);
        }
        return rows;
    }

    private Member findMember(JsonNode item) {
        if (item.hasNonNull("memberNo") && item.get("memberNo").canConvertToInt()) {
            Member member = memberMapper.getMemberByMemberNo(item.get("memberNo").asInt());
            if (member != null) {
                return member;
            }
        }
        String phone = text(item, "memberPhone");
        if (phone.isEmpty()) {
            return null;
        }
        return registerMapper.getMemberByPhone(phone);
    }

    private LocalDateTime time(JsonNode item) {
        String raw = text(item, "time");
        if (raw.isEmpty()) {
            return null;
        }
        try {
            if (raw.length() == 10) {
                return LocalDate.parse(raw).atStartOfDay();
            }
            return LocalDateTime.parse(raw, DATE_TIME);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private String text(JsonNode item, String field) {
        if (item == null || !item.hasNonNull(field)) {
            return "";
        }
        return item.get(field).asText("").trim();
    }

    private Path feedPath() {
        Path[] candidates = new Path[] {
                Paths.get("device_feed", "feed.json"),
                Paths.get("..", "device_feed", "feed.json"),
                Paths.get("..", "..", "device_feed", "feed.json")
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        return candidates[1];
    }

    private static class Loaded {
        private boolean ready;
        private String message = "";
        private String path = "";
        private int skipped;
        private List<Map<String, Object>> equipment = new ArrayList<Map<String, Object>>();
        private List<Map<String, Object>> scans = new ArrayList<Map<String, Object>>();
    }
}

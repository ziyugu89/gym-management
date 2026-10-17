package com.gym.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员侧助手。课程推荐来自已有报名和签到。资料问答只返回导入原文，不生成训练方案。
 */
@Service
public class GymGuideService {

    private final ObjectMapper json = new ObjectMapper();

    @Resource
    private InsightService insightService;

    public Map<String, Object> guide(int memberNo, String question) {
        Loaded loaded = load();
        String ask = question == null ? "" : question.trim();
        List<Map<String, Object>> matches = new ArrayList<Map<String, Object>>();
        if (loaded.ready && !ask.isEmpty()) {
            for (Map<String, String> article : loaded.articles) {
                if (matches.size() >= 5) {
                    break;
                }
                if (contains(article.get("title"), ask) || contains(article.get("text"), ask)
                        || contains(ask, article.get("title"))) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("title", article.get("title"));
                    row.put("text", article.get("text"));
                    matches.add(row);
                }
            }
        }
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("ready", loaded.ready);
        data.put("message", message(loaded, ask, matches.size()));
        data.put("matches", matches);
        data.put("recommend", insightService.recommend(memberNo));
        data.put("path", loaded.path);
        return data;
    }

    private String message(Loaded loaded, String ask, int hits) {
        if (!loaded.ready) {
            return "尚未导入问答资料。课程推荐仍使用已有的签到和报名记录。把原文写入 device_feed/knowledge.json 后即可检索。";
        }
        if (ask.isEmpty()) {
            return "已导入 " + loaded.articles.size() + " 条资料。输入问题后只返回原文，不改写。";
        }
        if (hits == 0) {
            return "导入资料里没有和这个问题对应的原文。";
        }
        return "以下是导入资料中的原文，未作改写。";
    }

    private boolean contains(String text, String part) {
        return text != null && part != null && part.length() >= 2 && text.contains(part);
    }

    private Loaded load() {
        Loaded loaded = new Loaded();
        Path path = knowledgePath();
        loaded.path = path.toAbsolutePath().normalize().toString();
        if (!Files.exists(path)) {
            return loaded;
        }
        JsonNode root;
        try {
            root = json.readTree(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            return loaded;
        }
        JsonNode articles = root == null ? null : root.path("articles");
        if (articles == null || !articles.isArray()) {
            return loaded;
        }
        for (JsonNode item : articles) {
            if (loaded.articles.size() >= 50) {
                break;
            }
            String title = text(item, "title");
            String body = text(item, "text");
            if (title.isEmpty() || body.isEmpty()) {
                continue;
            }
            Map<String, String> row = new LinkedHashMap<String, String>();
            row.put("title", title);
            row.put("text", body);
            loaded.articles.add(row);
        }
        loaded.ready = !loaded.articles.isEmpty();
        return loaded;
    }

    private String text(JsonNode item, String field) {
        if (item == null || !item.hasNonNull(field)) {
            return "";
        }
        return item.get(field).asText("").trim();
    }

    private Path knowledgePath() {
        Path[] candidates = new Path[] {
                Paths.get("device_feed", "knowledge.json"),
                Paths.get("..", "device_feed", "knowledge.json"),
                Paths.get("..", "..", "device_feed", "knowledge.json")
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
        private String path = "";
        private List<Map<String, String>> articles = new ArrayList<Map<String, String>>();
    }
}

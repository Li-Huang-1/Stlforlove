package org.example.myblog.service.impl;

import org.example.myblog.config.GithubConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageUploadServiceImpl {

    @Autowired
    private GithubConfig gitHubConfig;

    /*
    WebClient 是 Spring 5 引入的非阻塞式 HTTP 客户端，用于发起 HTTP 请求并处理响应。
    它是 Spring WebFlux 模块的核心组件之一，旨在替代传统的 RestTemplate。
     */
    private final WebClient webClient = WebClient.create();

    /**
     * 上传图片到 GitHub
     */
    public String uploadImage(MultipartFile file) throws Exception {
        // 生成文件名（时间戳 + 扩展名）
        String originalFilename = file.getOriginalFilename();

        // 获取扩展名（不带点，如 "jpg"）
        String ext = getFileExtension(originalFilename);

        // 获取基础文件名（安全移除扩展名）
        String baseName = getBaseName(originalFilename);

        // 清理特殊字符（只保留字母、数字、中文、下划线、连字符）
        String sanitizedBaseName = sanitizeFilename(baseName);

        // 生成时间戳和UUID
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        // 最终文件名格式: 20261226_143025_abc12345_avatar.jpg
        String filename = String.format("%s_%s_%s%s",
                timestamp,
                uuid,
                sanitizedBaseName,
                ext.isEmpty() ? "" : "." + ext);  // 显式添加点号

        String path = "img/" + filename;

        // 转 Base64
        byte[] bytes = file.getBytes();
        String base64Content = Base64.getEncoder().encodeToString(bytes);

        // 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Upload image " + filename);
        body.put("content", base64Content);
        body.put("branch", gitHubConfig.getBranch());

        // 调用 GitHub API
        // 字符串格式化
        String apiUrl = String.format(
                "https://api.github.com/repos/%s/%s/contents/%s",
                gitHubConfig.getUsername(),
                gitHubConfig.getRepo(),
                path
        );

        // 使用 Spring WebFlux 的 WebClient 发起 HTTP PUT 请求
        // 这是一个链式调用的 Fluent API 风格写法
        webClient.put() // 【1】创建 HTTP PUT 方法的请求规范对象（RequestHeadersUriSpec）
                .uri(apiUrl) // 【2】设置请求的目标 URL（此处使用变量 apiUrl）
                // 【3】添加 HTTP 请求头：使用 GitHub 个人访问令牌进行身份认证
                .header("Authorization", "token " + gitHubConfig.getToken())
                // 【4】添加 HTTP 请求头：告诉 GitHub API 返回 JSON 格式响应（v3 版本）
                .header("Accept", "application/vnd.github.v3+json")
                // 【5】设置请求体（PUT 请求要发送的数据对象），WebClient 会自动序列化为 JSON
                .bodyValue(body)
                // 【6】执行 HTTP 请求并开始获取响应（返回 ResponseSpec）
                // retrieve() 会自动处理 4xx/5xx 状态码并抛出 WebClientResponseException
                .retrieve()
                // 【7】将响应体转换为 Mono<String>（Reactor 的异步包装类型）
                .bodyToMono(String.class)
                // 【8】阻塞当前线程，订阅 Mono 并等待异步操作完成
                // block() 会强制当前线程等待，直到收到响应或超时/异常
                // 这是"将异步转为同步"的关键操作，会破坏 WebClient 的非阻塞优势
                // 返回值：响应体的 String 内容（如 GitHub API 返回的 JSON 字符串）
                .block();

        // 返回 CDN URL
        return String.format(
                "https://cdn.jsdmirror.com/gh/%s/%s@%s/%s",
                gitHubConfig.getUsername(),
                gitHubConfig.getRepo(),
                gitHubConfig.getBranch(),
                path
        );
    }

    /**
     * 获取文件扩展名（不带点）
     * 例如: "avatar.jpg" → "jpg"
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        // 去除文件名首尾的空白字符（空格、制表符、换行符等）
        String name = filename.trim();
        // 查找文件名中最后一个点号（.）的位置
        int lastDotIndex = name.lastIndexOf(".");

        if (lastDotIndex <= 0) {  // <= 0 表示没有扩展名 或 以点开头
            return "";
        }
        // 从点号的下一个位置开始截取字符串到末尾
        return name.substring(lastDotIndex + 1);
    }

    /**
     * 获取基础文件名（安全移除扩展名）
     * 例如: "photo.backup.jpg" → "photo.backup"
     *      "avatar.jpg" → "avatar"
     *      ".gitignore" → ".gitignore"
     */
    private String getBaseName(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "image";  // 默认名称
        }

        // 移除可能的路径信息
        String name = filename.replaceAll("^.*[/\\\\]", "");

        // 移除扩展名（只移除最后一个点后面的部分）
        int lastDotIndex = name.lastIndexOf(".");
        if (lastDotIndex > 0) {  // > 0 确保不是以点开头的文件
            name = name.substring(0, lastDotIndex);
        }

        return name;
    }

    /**
     * 清理文件名中的特殊字符
     * 只保留字母、数字、中文、下划线、连字符
     */
    private String sanitizeFilename(String name) {
        if (name == null || name.isEmpty()) {
            return "image";
        }

        // 替换特殊字符为下划线
        String sanitized = name.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5_-]", "_");

        // 限制长度，避免文件名过长
        int maxLength = 30;
        if (sanitized.length() > maxLength) {
            sanitized = sanitized.substring(0, maxLength);
        }

        // 移除首尾的下划线（可选）
        sanitized = sanitized.replaceAll("^_+|_+$", "");

        // 如果清理后为空，使用默认名称
        return sanitized.isEmpty() ? "image" : sanitized;
    }
}


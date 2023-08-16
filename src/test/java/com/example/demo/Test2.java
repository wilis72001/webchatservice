package com.example.demo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Test2 {

    static void sendMessage(){
        // 定义接口 URL
        String apiUrl = "http://localhost:8080/api/send-message"; // 替换为实际的接口 URL

        // 创建 HttpClient 实例
        HttpClient httpClient = HttpClient.newHttpClient();

        // 构造请求参数
        String sender = "i am sender"; // 替换为实际的发送者值
        String receiver = "i am receiver"; // 替换为实际的接收者值
        String content = "这是发送的消息"; // 替换为实际的消息内容
        String currentTime = "1111222"; // 替换为实际的发送时间

        // 创建 POST 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "sender=" + sender +
                                "&receiver=" + receiver +
                                "&content=" + content +
                                "&sendTime=" + currentTime))
                .build();

        try {
            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 打印响应内容
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        }

    static void testGetMessagesByAccount(){
        // 定义接口 URL
        String apiUrl = "http://localhost:8080/api/getMessagesByAccount"; // 替换为实际的接口 URL
        String receiver = "yourReceiverValue"; // 替换为实际的接收者值

        // 创建 HttpClient 实例
        HttpClient httpClient = HttpClient.newHttpClient();

        // 创建 POST 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("receiver=" + receiver))
                .build();

        try {
            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 打印响应内容
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            // 解析响应 JSON 数据，这里假设响应为 JSON 格式
            // 如果响应内容是 JSON 格式，请使用 JSON 解析库解析响应内容
            // 示例：JsonNode jsonResponse = objectMapper.readTree(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
   //     sendMessage();
          testGetMessagesByAccount();
    }
}

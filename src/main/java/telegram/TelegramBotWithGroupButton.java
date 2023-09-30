package telegram;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TelegramBotWithGroupButton {
    public static void main(String[] args) {
        // 机器人API令牌
        String botToken = "5817537605:AAG77T7_jpDJuJW2PvufqAMhcBTQ1YoLcb8";

        try {
            // 创建URL对象
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/getUpdates");

            // 打开HTTP连接以接收更新
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 获取响应数据（JSON格式）
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // 从响应中获取响应体
                String responseBody = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

                // 解析JSON响应
                Gson gson = new Gson();
                JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

                // 检查是否有回调查询数据
                if (responseJson.has("result")) {
                    responseJson.getAsJsonArray("result").forEach(result -> {
                        JsonObject update = result.getAsJsonObject();
                        if (update.has("callback_query")) {
                            JsonObject callbackQuery = update.getAsJsonObject("callback_query");

                            // 获取回调数据
                            String callbackData = callbackQuery.getAsJsonPrimitive("data").getAsString();
                  //          String chatId = callbackQuery.getAsJsonObject("message").getAsJsonPrimitive("chat").getAsString();

                            // 根据回调数据执行相应的操作
                            if ("button_click".equals(callbackData)) {
                                // 用户点击了按钮，执行操作
                                sendMessageToGroup(botToken, "-1001956257821", "这是配置好的消息");
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送消息到群组
    private static void sendMessageToGroup(String botToken, String chatId, String message) {
        try {
            // 创建URL对象
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage");

            // 创建消息数据
            JsonObject messageData = new JsonObject();
            messageData.addProperty("chat_id", chatId);
            messageData.addProperty("text", message);

            // 将消息数据转换为JSON字符串
            String postData = new Gson().toJson(messageData);

            // 打开HTTP连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));

            // 获取输出流并发送数据
            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            // 获取响应代码
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("消息已成功发送到群组！");
            } else {
                System.out.println("消息发送失败，响应代码：" + responseCode);
            }

            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

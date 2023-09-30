package telegram;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TelegramBotSendInlineKeyboard {
    public static void main(String[] args) {
        // 机器人API令牌
        String botToken = "5817537605:AAG77T7_jpDJuJW2PvufqAMhcBTQ1YoLcb8";
        // 目标聊天ID（可以是用户ID或群组ID） 频道
        String chatId = "-1001956257821";
        String message = "欢迎来到我的群组";

        try {
            // 创建URL对象
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage");

            // 创建第一个按钮
            JsonObject button1 = new JsonObject();
            button1.addProperty("text", "功能1");
            button1.addProperty("callback_data", "button1_click");

            // 创建第二个按钮
            JsonObject button2 = new JsonObject();
            button2.addProperty("text", "功能2");
            button2.addProperty("callback_data", "button2_click");

            // 创建第三个按钮
            JsonObject button3 = new JsonObject();
            button3.addProperty("text", "功能3");
            button3.addProperty("callback_data", "button3_click");

            // 创建第四个按钮
            JsonObject button4 = new JsonObject();
            button4.addProperty("text", "功能4");
            button4.addProperty("callback_data", "button4_click");

            // 创建按钮行
            JsonArray keyboardRow1 = new JsonArray();
            keyboardRow1.add(button1);
            keyboardRow1.add(button2);

            JsonArray keyboardRow2 = new JsonArray();
            keyboardRow2.add(button3);
            keyboardRow2.add(button4);

            // 创建键盘
            JsonArray inlineKeyboard = new JsonArray();
            inlineKeyboard.add(keyboardRow1);
            inlineKeyboard.add(keyboardRow2);

            // 创建消息数据
            JsonObject messageData = new JsonObject();
            messageData.addProperty("chat_id", chatId);
            messageData.addProperty("text", message);

            // 创建包含InlineKeyboardMarkup的键盘
            JsonObject replyMarkup = new JsonObject();
            replyMarkup.add("inline_keyboard", inlineKeyboard);
            messageData.add("reply_markup", replyMarkup);

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
                System.out.println("消息已成功发送！");
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

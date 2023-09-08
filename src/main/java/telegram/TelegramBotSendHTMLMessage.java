package telegram;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TelegramBotSendHTMLMessage {
    public static void main(String[] args) {
        // 机器人API令牌
        String botToken = "5817537605:AAEtuUTVyddCQIraDWtCoDXHkeaitj_2H44";
        // 目标聊天ID（可以是用户ID或群组ID）
        String chatId = "-1001956257821";

        // 富文本消息内容（使用HTML格式）
        String htmlMessage = "<b>Bold Text</b>\n" +
                "<i>Italic Text</i>\n" +
                "<a href=\"https://www.baidu.com\">Link</a>" +
                "\uD83D\uDE03😢🎶😒😍🤷‍♀️🤦‍♀️👍😃👀" +
                "<pre>" +
                    "<code class=\"language-python\">" +
                         "print(\"Hello, World!\")\n" +
                    "</code>" +
                "</pre>\n";

        try {
            // 创建URL对象以发送HTML消息
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage");

            // 打开HTTP连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // 构建消息数据
            String postData = "chat_id=" + chatId + "&text=" + URLEncoder.encode(htmlMessage, StandardCharsets.UTF_8.toString()) + "&parse_mode=HTML";
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            // 获取输出流并发送数据
            OutputStream os = connection.getOutputStream();
            os.write(postDataBytes);

            // 获取响应代码
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("HTML富文本消息已成功发送！");
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

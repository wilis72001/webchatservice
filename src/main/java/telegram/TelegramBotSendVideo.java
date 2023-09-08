package telegram;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TelegramBotSendVideo {
    public static void main(String[] args) {
        // 机器人API令牌
        String botToken = "5817537605:AAEtuUTVyddCQIraDWtCoDXHkeaitj_2H44";
        // 目标聊天ID（可以是用户ID或群组ID）
        String chatId = "-1001956257821";
        // 要发送的图片文件路径
        String videoPath = "D:\\文件下载2\\imag\\4.5m.mp4"; // 替换为您的图片文件路径

        try {
            // 创建URL对象
            URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendVideo");

            // 打开HTTP连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // 设置请求参数
            String boundary = "---------------------------" + System.currentTimeMillis();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // 构建消息数据
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

            // 添加chat_id参数
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n");
            writer.append(chatId).append("\r\n");

            // 添加video参数
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"video\"; filename=\"video.mp4\"\r\n");
            writer.append("Content-Type: video/mp4\r\n\r\n");
            writer.flush();

            // 读取并写入视频文件数据
            FileInputStream videoInputStream = new FileInputStream(videoPath);
            byte[] videoBuffer = new byte[4096];
            int videoBytesRead;
            while ((videoBytesRead = videoInputStream.read(videoBuffer)) != -1) {
                outputStream.write(videoBuffer, 0, videoBytesRead);
            }
            videoInputStream.close();

            writer.append("\r\n");
            writer.append("--").append(boundary).append("--\r\n");
            writer.close();

            // 获取响应代码
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("视频已成功发送！");
            } else {
                System.out.println("视频发送失败，响应代码：" + responseCode);
            }

            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


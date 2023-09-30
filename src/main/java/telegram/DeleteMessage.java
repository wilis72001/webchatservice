package telegram;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DeleteMessage {
    public static void main(String[] args) {
        // 机器人API令牌
        String botToken = "5817537605:AAEtuUTVyddCQIraDWtCoDXHkeaitj_2H44";
        // 目标聊天ID（可以是用户ID或群组ID）
        String chatId = "-1001919206308";

        // 要删除的消息ID
        int messageId = 1; // 替换为要删除的消息的ID

        try {
            // 创建HTTP客户端
            HttpClient httpClient = HttpClients.createDefault();

            // 构建API请求URL
            String apiUrl = "https://api.telegram.org/bot" + botToken + "/deleteMessage?chat_id=" + chatId + "&message_id=" + messageId;

            // 创建HTTP POST请求
            HttpPost httpPost = new HttpPost(apiUrl);

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);

            // 检查响应状态码
            if (response.getStatusLine().getStatusCode() == 200) {
                // 从响应中获取响应体
                String responseBody = EntityUtils.toString(response.getEntity());

                // 解析响应，通常是一个JSON对象，可以根据需要处理
                // 请注意，删除消息成功后，响应可能为空
                System.out.println("Delete Message Response: " + responseBody);
            } else {
                System.err.println("请求失败，状态码：" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package telegram;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.HashSet;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        // 机器人API令牌
        String botToken = "5817537605:AAEtuUTVyddCQIraDWtCoDXHkeaitj_2H44";

        try {
            // 创建HTTP客户端
            HttpClient httpClient = HttpClients.createDefault();

            // 构建API请求URL
            String apiUrl = "https://api.telegram.org/bot" + botToken + "/getUpdates";

            // 创建HTTP GET请求
            HttpGet httpGet = new HttpGet(apiUrl);

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpGet);

            // 检查响应状态码
            if (response.getStatusLine().getStatusCode() == 200) {
                // 从响应中获取响应体
                String responseBody = EntityUtils.toString(response.getEntity());

                // 使用Gson解析JSON
                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray results = json.getAsJsonArray("result");

                // 使用Set来存储唯一的chat_id
                Set<String> chatIds = new HashSet<>();

                // 处理获取到的更新
                for (JsonElement element : results) {
                    JsonObject updateObject = element.getAsJsonObject();
                    JsonObject messageObject = updateObject.getAsJsonObject("channel_post");
                    JsonObject chatObject = messageObject.getAsJsonObject("sender_chat");
                    String chatId = chatObject.get("id").getAsString();

                    // 添加chat_id到Set中
                    chatIds.add(chatId);
                }

                // 打印所有chat_id
                for (String chatId : chatIds) {
                    System.out.println("Chat ID: " + chatId);
                }
            } else {
                System.err.println("请求失败，状态码：" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package telegram;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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

                // 获取第一个更新的id
                String chatId = String.valueOf(json.getAsJsonArray("result")
                        .get(0)
                        .getAsJsonObject()
                        .getAsJsonObject("message")
                        .getAsJsonObject("chat")
                        .get("id"));
                  //      .getAsInt();
                System.out.println("responseBody: " + responseBody);

                System.out.println("Chat ID: " + chatId);
            } else {
                System.err.println("请求失败，状态码：" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

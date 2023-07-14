package register;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
@Component
public class RegisterApi {
    private static final String REGISTER_URL = "https://brazil.one2.cc/lottery-login-api/user/register";
    private static final String REQUEST_BODY = "{\"body\":{" +
            "\"auxiliaryCode\":\"71a187509a2c8ced575f42a8a959b41bcd816dc\"," +
            "\"userName\":\"%s\"," +  // 添加userName占位符
            "\"password\":\"a2720eff0ba78bd7722ea504bf740b36\"," +
            "\"confirmPassword\":\"a2720eff0ba78bd7722ea504bf740b36\"," +
            "\"sourceType\":\"3\"," +
            "\"deviceCode\":\"H5\"," +
            "\"userCode\":\"\"}," +
            "\"header\":{\"apiName\":\"/lottery-login-api/user/register\"," +
            "\"callTime\":1685926713320," +
            "\"sign\":\"1111\"," +
            "\"gzipEnabled\":0," +
            "\"deviceCode\":\"71a187509a2c8ced575f42a8a959b41bcd816dc\"," +
            "\"token\":\"\"," +
            "\"clientType\":\"4\"," +
            "\"languageCode\":\"en\"," +
            "\"platformId\":\"8002\"," +
            "\"apiVersion\":\"23011301\"}}";
     Long register(String userName) {
         String requestBody = String.format(REQUEST_BODY, userName);

         // 实现注册逻辑
         Long apiResponseTime = 0L;
         try {
             URL url = new URL(REGISTER_URL);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setRequestMethod("POST");
             conn.setDoOutput(true);
             conn.setRequestProperty("Content-Type", "application/json");
             conn.setRequestProperty("apiName", "/lottery-login-api/user/register");
             conn.setRequestProperty("callTime", "1685926713320");
             conn.setRequestProperty("sign", "a3d572c526f82fbf762ce77aaad9e74e");
             conn.setRequestProperty("gzipEnabled", "0");
             conn.setRequestProperty("deviceCode", "71a187509a2c8ced575f42a8a959b41bcd816dc");
             conn.setRequestProperty("token", "");
             conn.setRequestProperty("clientType", "4");
             conn.setRequestProperty("languageCode", "en");
             conn.setRequestProperty("platformId", "8002");
             conn.setRequestProperty("apiVersion", "23011301");
             conn.setRequestProperty("reqTid", generateReqId());

             Long batchApiStartTime = System.currentTimeMillis();

             OutputStream os = conn.getOutputStream();
             os.write(requestBody.getBytes());
             os.flush();
             os.close();

             int responseCode = conn.getResponseCode();
             System.out.println("Response Code: " + responseCode);

             BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             StringBuilder response = new StringBuilder();
             String inputLine;

             while ((inputLine = in.readLine()) != null) {
                 response.append(inputLine);
             }
             in.close();
             // TODO: 处理响应数据
             String responseBody = response.toString();
             System.out.println(responseBody);
             if (responseBody != null) {
                 Long batchGetResponsTime = System.currentTimeMillis();
                 apiResponseTime = batchGetResponsTime - batchApiStartTime;
                 System.out.println("用户名是：" + userName + " 响应时间:" + apiResponseTime + " reqTid:" + generateReqId());
                 return apiResponseTime;
             }

         } catch (IOException e) {
             e.printStackTrace();
         }
         return apiResponseTime;
     }
    public static String generateReqId() {
        String randomUUID = UUID.randomUUID().toString();
        String randomId = randomUUID.substring(0, 11);
        long timestamp = System.currentTimeMillis();
        String reqId = timestamp + "_" + randomId;
        //System.out.println("generateReqId funcs: " + reqId);
        return reqId;
    }
}

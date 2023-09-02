package Controller;


import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.*;


//notice
@RestController
@CrossOrigin
public class ChatControllerDatabase {
    final String serverNumber = "53335";
    private JdbcTemplate jdbcTemplate;
    public ChatControllerDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //发送消息
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/send_message")
    public String send_message(@RequestBody Map<String, String> requestBody) {

        String sender_account =  requestBody.get("sender");
        String receiver_account =   requestBody.get("receiver");
        String message =    requestBody.get("content");
        String sent_time =    requestBody.get("sent_time");
    //    System.out.println(sender_account+"---"+receiver_account+"---"+message+"---"+sent_time);

        try {
            String sql = "INSERT INTO chat_records (sender_account, receiver_account,message,sent_time) VALUES (?,?,?,?)";
            jdbcTemplate.update(sql, sender_account,receiver_account, message,sent_time);
            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println(e);
        }
        return "发送成功";
    }

    //发送图片
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/upLoadChatImage")
    public String upLoadChatImage(@RequestBody Map<String, Object> requestBody) {

        String sender_account =  (String) requestBody.get("sender");
        String receiver_account =   (String) requestBody.get("receiver");
        int sent_time = (Integer) requestBody.get("sent_time");

        String imgVideoBase64 = (String) requestBody.get("img_video"); // 接收Base64编码的字符串


        //    System.out.println(sender_account+"---"+receiver_account+"---"+message+"---"+sent_time);

        try {
            // 将Base64编码的字符串解码为字节数组
            byte[] imgVideoBytes = Base64.getDecoder().decode(imgVideoBase64);

            String sql = "INSERT INTO chat_records (sender_account, receiver_account,sent_time,img_video) VALUES (?,?,?,?)";
            jdbcTemplate.update(sql, sender_account,receiver_account,sent_time,imgVideoBytes);
            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println(e);
        }
        return "发送成功";
    }



    //获取我发送的消息
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/getMessageISent")
    public List<Map<String, Object>> getMessageISent(@RequestBody Map<String, String> requestBody) {
        String sender_account = requestBody.get("sender");
        String receiver_account = requestBody.get("receiver");
      //  System.out.println(sender_account + "---" + receiver_account);
        try {
            String sql = "SELECT sender_account,receiver_account,message,sent_time FROM chat_records WHERE sender_account = ? AND receiver_account = ?";
            List<Map<String, Object>> chatHistory = jdbcTemplate.queryForList(sql, sender_account, receiver_account);
            chatHistory.forEach(data -> {
                data.forEach((key, value) -> {
               //     System.out.println(key + ": " + value);
                });
            });
            return chatHistory;
        } catch (Exception e) {
            System.out.println(e);
        }
        return Collections.emptyList(); // Return an empty list in case of error
    }

    //获取我发送的图片
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/getPictureISent")
    public List<Map<String, Object>> getPictureISent(@RequestBody Map<String, String> requestBody) {
        String sender_account = requestBody.get("sender");
        String receiver_account = requestBody.get("receiver");
        //  System.out.println(sender_account + "---" + receiver_account);
        try {
            String sql = "SELECT sender_account,receiver_account,img_video,sent_time FROM chat_records WHERE sender_account = ? AND receiver_account = ?";
            List<Map<String, Object>> chatHistory = jdbcTemplate.queryForList(sql, sender_account, receiver_account);
            chatHistory.forEach(data -> {
                data.forEach((key, value) -> {
                    //     System.out.println(key + ": " + value);
                });
            });
            return chatHistory;
        } catch (Exception e) {
            System.out.println(e);
        }
        return Collections.emptyList(); // Return an empty list in case of error
    }


    //获取发送给我的消息
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/getMessageSentToMe")
    public List<Map<String, Object>> getMessageSentToMe(@RequestBody Map<String, String> requestBody) {
        String sender_account = requestBody.get("sender");
        String receiver_account = requestBody.get("receiver");
    //    System.out.println(sender_account + "---" + receiver_account);
        try {
            String sql = "SELECT sender_account,receiver_account,message,sent_time FROM chat_records WHERE sender_account = ? AND receiver_account = ?";
            List<Map<String, Object>> messageSentToMe = jdbcTemplate.queryForList(sql, sender_account,receiver_account);
            messageSentToMe.forEach(data -> {
                data.forEach((key, value) -> {
               //     System.out.println(key + ": " + value);
                });
            });
            return messageSentToMe;
        } catch (Exception e) {
            System.out.println(e);
        }

        return Collections.emptyList(); // Return an empty list in case of error
    }


    //获取发送给我的消息
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/getPictureSentToMe")
    public List<Map<String, Object>> getPictureSentToMe(@RequestBody Map<String, String> requestBody) {
        String sender_account = requestBody.get("sender");
        String receiver_account = requestBody.get("receiver");

        try {
            String sql = "SELECT sender_account, receiver_account, img_video, sent_time FROM chat_records WHERE sender_account = ? AND receiver_account = ?";
            List<Map<String, Object>> pictureSentToMe = jdbcTemplate.queryForList(sql, sender_account, receiver_account);

            pictureSentToMe.forEach(data -> {
                data.forEach((key, value) -> {
                    //     System.out.println(key + ": " + value);
                });
            });

            return pictureSentToMe;
        } catch (Exception e) {
            System.out.println(e);
        }

        return Collections.emptyList(); // Return an empty list in case of error
    }




    @Configuration
    public class CorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:"+serverNumber) // 允许来自该地址的跨域请求
                    .allowedMethods("GET","POST","PUT","DELETE") // 允许的HTTP方法
                    .allowCredentials(true);// 允许携带凭证（如Cookie）
        }

    }


}

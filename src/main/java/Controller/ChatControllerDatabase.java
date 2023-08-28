package Controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//notice
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChatControllerDatabase {
    final String serverNumber = "56594";
    private JdbcTemplate jdbcTemplate;


    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/sendMessageToDatabase")
    public String sendMessageToDatabase(@RequestBody Map<String, String> requestBody) {
        String sender_account =  requestBody.get("sender_account");
        String receiver_account =   requestBody.get("receiver_account");
        String message =    requestBody.get("message");
        String sent_time =   requestBody.get("sent_time");
        System.out.println(sender_account+"---"+receiver_account+"---"+message+"---"+sent_time);

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

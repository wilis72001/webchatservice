package Controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessaging;



import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChatController {
    final String serverNumber = "50153";
    private JdbcTemplate jdbcTemplate;
    private final DatabaseReference database;

    public ChatController() {
        try {
            // 使用 ClassPathResource 加载服务账号密钥文件
            ClassPathResource resource = new ClassPathResource("webchat-80ebe-firebase-adminsdk-srmgk-30bb5c3d42.json");

            FileInputStream serviceAccount = new FileInputStream(resource.getFile());

            // 初始化 FirebaseApp
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://webchat-80ebe-default-rtdb.firebaseio.com") // 替换成你的 Firebase 实时数据库 URL
                    .build();
            FirebaseApp.initializeApp(options);

            // 获取 Firebase 实时数据库的引用
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            this.database = firebaseDatabase.getReference("messages");

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping(value = "/send-message", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String sendMessage(@RequestBody MultiValueMap<String, String> formData) {
        // 根据需要处理表单数据 formData
        // 例如，获取消息内容和发送者信息
        String sender = formData.getFirst("sender");
        String receiver = formData.getFirst("receiver");
        String content = formData.getFirst("content");
        String currentTime = formData.getFirst("sendTime"); // 解析为整数
        // 创建 Message 对象并保存到数据库
        Message2 message = new Message2(sender,receiver,content,currentTime);
        DatabaseReference newMessageRef = database.push();
        newMessageRef.setValue(message, (error, ref) -> {
            if (error != null) {
                System.err.println("Message failed to send: " + error.getMessage());
            } else {
                System.out.println("Message sent successfully!");
            }
        });
        return "Sending message...";
    }



    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/getMessagesByAccount")
    public CompletableFuture<List<Message2>> getMessagesByAccount(@RequestParam String receiver) {
        System.out.println("receiver的值是" + receiver);
        CompletableFuture<List<Message2>> futureMessages = new CompletableFuture<>();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message2> messages = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    /*
                    if (message != null && message.getReceiver().equals(receiver)) {
                        messages.add(message);
                    }

                     */
                }
                // 输出 messages 到控制台
                System.out.println("Messages: " + messages);

                // 将数据设置到 CompletableFuture，并标记为已完成
                futureMessages.complete(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 处理取消事件（可选）
                // 设置 CompletableFuture 为异常状态
                futureMessages.completeExceptionally(databaseError.toException());
            }
        });

        return futureMessages;
    }


    @PostMapping("/send-notification")
    public String sendNotification(@RequestParam String token, @RequestParam String messageText) {
        Notification notification = Notification.builder()
                .setTitle("New Message")
                .setBody(messageText)
                .build();

        // 构建消息内容，将数据放入 data 中
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("content", messageText) // 您可以放入其他自定义数据
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            return "Notification sent: " + response;
        } catch (Exception e) {
            return "Failed to send notification: " + e.getMessage();
        }
    }



    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @PostMapping("/sendMessageToDatabase")
    public String sendMessageToDatabase(@RequestBody Map<String, String> requestBody) {
        String sender_account =  requestBody.get("sender_account");
        String receiver_account =   requestBody.get("receiver_account");
        String message =    requestBody.get("message");
        String sent_time =   requestBody.get("sent_time");
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

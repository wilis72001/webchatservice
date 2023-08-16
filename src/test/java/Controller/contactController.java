package Controller;

import WebSocket.AccountGenerator;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.assertj.core.api.ObjectEnumerableAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@CrossOrigin
@Controller
public class contactController {
    final String serverNumber = "50153";
    // 将字节数组转换为十六进制字符串
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    private  JdbcTemplate jdbcTemplate;

    //查询我的好友
    @Autowired
    public contactController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @RequestMapping(value = "/getMyFriend", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getMyFriend(String my_account) throws IOException {
        System.out.println("account的值是" + my_account);
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<String> friendAccounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM my_friend WHERE my_account = ?";
            dataList = jdbcTemplate.queryForList(sql, my_account);
            List<Map<String, Object>> friendDetailsList = new ArrayList<>(); // 存储好友详细信息
            for (Map<String, Object> data : dataList) {
                String friendAccount = (String) data.get("friend_account");
                friendAccounts.add(friendAccount);
                System.out.println("我的好友地址" + friendAccounts);
                try {
                    String sql2 = "SELECT * from blc_user WHERE account_add = ?";
                    Map<String, Object> friendDetails = jdbcTemplate.queryForMap(sql2, friendAccount);
                    if (friendDetails != null) {
                        friendDetailsList.add(friendDetails);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            return friendDetailsList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return dataList;
    }


    //检查是否设置密码
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @RequestMapping(value = "/checkIfSetPw", method = RequestMethod.POST)
    @ResponseBody
    public String checkIfSetPw(String my_account) throws IOException {
        System.out.println("account的值是" + my_account);
        List<Map<String, Object>> passwordList = new ArrayList<>();
        try {
            String sql = "SELECT password FROM blc_user WHERE account_add = ?";
            passwordList = jdbcTemplate.queryForList(sql, my_account);
        } catch (Exception e) {
            System.out.println("查询密码报错"+e);
        }
        Map<String, Object> firstResult = passwordList.get(0);
        Object passwordValue = firstResult.get("password");
        if ("default".equals(passwordValue)) {
            return "请先设置账号的密码";
        } else {
            return "密码已经设置";
        }
    }

    //查询用户
    @CrossOrigin(origins = "http://localhost:" + serverNumber)
    @RequestMapping(value = "/searchUser", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> searchUser(String user_account) throws IOException {
        System.out.println("account的值是" + user_account);
        List<Map<String, Object>> user = new ArrayList<>();
        try {
            String sql = "SELECT * FROM blc_user WHERE account_add = ?";
            user = jdbcTemplate.queryForList(sql, user_account);
        } catch (Exception e) {
            System.out.println(e);
        }
        return user;
    }

    @Configuration
    public class CorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/oneKeyRegister")
                    .allowedOrigins("http://localhost:"+serverNumber) // 允许来自该地址的跨域请求
                    .allowedMethods("GET") // 允许的HTTP方法
                    .allowCredentials(true) // 允许携带凭证（如Cookie）
                    .maxAge(3600); // 预检请求的有效期，单位为秒
        }


    }

}


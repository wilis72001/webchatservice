package Controller;

import WebSocket.AccountGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
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


@Controller
public class webchatRegisterController {
    final String serverNumber = "51238";
    // 将字节数组转换为十六进制字符串
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    private  JdbcTemplate jdbcTemplate;
    @Autowired
    public webchatRegisterController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/oneKeyRegister",method = RequestMethod.GET)
    @ResponseBody
    public String oneKeyRegister() throws IOException {
        AccountGenerator ag = new AccountGenerator();
        String acount = ag.getString();
        System.out.println("一键注册的账号："+acount);
        try {
            String sql = "INSERT INTO blc_user (account_add, nick_name,email_add,phone_number,icon_image,personal_credo,password,status) VALUES (?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql,acount, "default","default", "default","default", "default", "default",1);

            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println(e);
        }
        return acount;
    }
    //登录接口
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(String account ,String password) throws IOException {
             System.out.println("account的值是"+account+"..value的值为："+password);
        try {
            // 使用SQL查询语句验证用户输入的账号和密码是否匹配
            String sql = "SELECT COUNT(*) FROM blc_user WHERE account_add = ? AND password = ?";
            int count = jdbcTemplate.queryForObject(sql, new Object[]{account, password}, Integer.class);
            if (count > 0) {
                // 账号密码匹配，登录成功
                try {
                    String sql2 = "UPDATE blc_user SET status= 1 WHERE account_add = ?";
                    jdbcTemplate.update(sql2,  account);
                } catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }
                return "登录成功";
            } else {
                // 账号密码不匹配，登录失败
                return "账号或密码错误，请重试！";
            }
        } catch (Exception e) {
            // 处理异常
            System.out.println(e);
            return "登录出现异常，请联系管理员！";
        }
    }

    //登出接口
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/logout",method = RequestMethod.POST)
    @ResponseBody
    public String logout(String account ) throws IOException {
        System.out.println("account的值是"+account);
        try {
            String sql2 = "UPDATE blc_user SET status= 0 WHERE account_add = ?";
            jdbcTemplate.update(sql2,  account);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return "登出成功";
    }

    // 保存密码接口
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value = "/savePassword", method = RequestMethod.POST)
    @ResponseBody
    public String savePassword(String address, String title, String value) throws IOException {
        // System.out.println("account的值是"+address+"..title的值为："+title+"..value的值为："+value);
        try {
            String sql = "UPDATE blc_user SET " + title + " = ? WHERE account_add = ?";
            jdbcTemplate.update(sql, value, address);
            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println("保存密码报错" + e);
        }
        return "保存成功！";
    }

    //获取个人资料接口
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/getPersonalData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getPersonalData(String address) throws IOException {
     //   System.out.println("account的值是" + address);
        Map<String, Object> data = new HashMap<>();

        try {
            String sql = "SELECT * from blc_user WHERE account_add = ?";
            data = jdbcTemplate.queryForMap(sql, address);
        } catch (Exception e) {
            System.out.println(e);
        }
        data.forEach((key, value) -> {
            //System.out.println(key + ": " + value);
        });
        System.out.println("-----------------------");

        return data;
    }

    //保存个人资料填入的数据（不含图片）
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/saveData",method = RequestMethod.POST)
    @ResponseBody
    public String saveData(String address ,String title,String value) throws IOException {
   //     System.out.println("account的值是"+address+"..title的值为："+title+"..value的值为："+value);
        try {
            String sql = "UPDATE blc_user SET "+title+" = ? WHERE account_add = ?";
            jdbcTemplate.update(sql,value, address);
            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println(e);
        }
        return "保存成功！";
    }

    //保存个人图片
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/saveImage",method = RequestMethod.POST)
    @ResponseBody
    public String saveImage(String address ,String title,String value) throws IOException {
        // 将Base64编码的字符串转换为字节数组
        byte[] valueBytes = Base64.getDecoder().decode(value);
        try {
            String sql = "UPDATE blc_user SET icon_image = ? WHERE account_add = ?";
            jdbcTemplate.update(sql, valueBytes, address);
            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println(e);
        }
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(valueBytes);
            java.awt.image.BufferedImage image = ImageIO.read(bis);

            // 显示图片
            javax.swing.JFrame frame = new javax.swing.JFrame();
            frame.getContentPane().add(new javax.swing.JLabel(new javax.swing.ImageIcon(image)));
            frame.pack();
         //   frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println("account的值是"+address+"title的值为："+title+"value的值为："+valueBytes);
        return "保存成功！";
    }

    //添加好友
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/addFriend",method = RequestMethod.POST)
    @ResponseBody
    public String addFriend(String myAccount ,String friendAccount) throws IOException {
        long timestamp = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("account的值是"+myAccount+"..friendAccount的值为："+friendAccount+"。。。当前时间："+dateTime);

        try {
            String sql = "INSERT INTO my_friend (my_account, friend_account,add_time) VALUES (?,?,?)";
            jdbcTemplate.update(sql,myAccount, friendAccount,dateTime);
            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println(e);
        }
        return "添加好友成功";
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

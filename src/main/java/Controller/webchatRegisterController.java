package Controller;

import WebSocket.AccountGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Controller
public class webchatRegisterController {
    final String serverNumber = "53703";
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
    @RequestMapping(value="/addFriend", method = RequestMethod.POST)
    @ResponseBody
    public String addFriend(String myAccount, String friendAccount) {
        try {
            long timestamp = System.currentTimeMillis();
            Instant instant = Instant.ofEpochMilli(timestamp);
            LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            System.out.println("在addFriend account的值是" + myAccount + "..friendAccount的值为：" + friendAccount + "。。。当前时间：" + dateTime);

            // Check if the friendship already exists
            String querySql = "SELECT COUNT(*) FROM my_friend WHERE my_account = ? AND friend_account = ? ";
            int count = jdbcTemplate.queryForObject(querySql, Integer.class, myAccount, friendAccount);

            if (count > 0) {
                return "用户已添加";
            }

            String insertSql = "INSERT INTO my_friend (my_account, friend_account, add_time) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, myAccount, friendAccount, dateTime);

            return "添加好友成功";
        } catch (Exception e) {
            System.out.println(e);
            return "添加好友失败";
        }
    }

    //添加好友
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/delFriend", method = RequestMethod.POST)
    @ResponseBody
    public String delFriend(String myAccount, String friendAccount) {
        try {
            System.out.println("删除好友：myAccount=" + myAccount + ", friendAccount=" + friendAccount);

            // Delete the friendship
            String sql = "DELETE FROM my_friend WHERE (my_account = ? AND friend_account = ?) OR (my_account = ? AND friend_account = ?)";
            int rowsAffected = jdbcTemplate.update(sql, myAccount, friendAccount, friendAccount, myAccount);

            if (rowsAffected > 0) {
                return "删除好友成功";
            } else {
                return "好友关系不存在";
            }
        } catch (Exception e) {
            System.out.println(e);
            return "删除好友失败";
        }
    }




    //获取我的好友
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/getMyFriend",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getMyFriend(String my_account) throws IOException {
        System.out.println("在getMyFriend my_account的值是"+my_account);
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<String> friendAccounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM my_friend WHERE my_account = ?";
            dataList = jdbcTemplate.queryForList(sql, my_account);
            List<Map<String, Object>> friendDetailsList = new ArrayList<>(); // 存储好友详细信息
            for (Map<String, Object> data : dataList) {
                String friendAccount = (String) data.get("friend_account");
                friendAccounts.add(friendAccount);
             //   System.out.println("我的好友地址" + friendAccounts);
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

    //查询用户
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/searchUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchUser(String myAccount) throws IOException {
        System.out.println("在searchUser myAccount的值是"+myAccount);
        //   System.out.println("account的值是" + address);
        Map<String, Object> data = new HashMap<>();

        try {
            String sql = "SELECT * from blc_user WHERE account_add = ?";
            data = jdbcTemplate.queryForMap(sql, myAccount);
        } catch (Exception e) {
            System.out.println(e);
        }
        data.forEach((key, value) -> {
            //System.out.println(key + ": " + value);
        });
        System.out.println("-----------------------");

        return data;
    }

    //查询是否设置密码
    @CrossOrigin(origins = "http://localhost:"+serverNumber)
    @RequestMapping(value="/checkIfSetPw",method = RequestMethod.POST)
    @ResponseBody
    public String checkIfSetPw(String my_account) throws IOException {
        System.out.println("在checkIfSetPw myAccount的值是"+my_account);
        //   System.out.println("account的值是" + address);
        Map<String, Object> data = new HashMap<>();

        try {
            String sql = "SELECT * from blc_user WHERE account_add = ?";
            data = jdbcTemplate.queryForMap(sql, my_account);
        } catch (Exception e) {
            System.out.println(e);
        }
        Object  passwordValue = data.get("password");
        String password = (String) passwordValue;
        System.out.println("password的值是"+password);

        if("default".equals(password)){
            return "请先设置账号的密码";
        }else {
            return "成功";
        }
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

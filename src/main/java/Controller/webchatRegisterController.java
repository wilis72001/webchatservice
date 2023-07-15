package Controller;

import WebSocket.AccountGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


@Controller
public class webchatRegisterController {

    private  JdbcTemplate jdbcTemplate;
    @Autowired
    public webchatRegisterController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @RequestMapping(value="/oneKeyRegister",method = RequestMethod.GET)
    @ResponseBody
    public String oneKeyRegister() throws IOException {
        AccountGenerator ag = new AccountGenerator();
        String acount = ag.getString();
        System.out.println(acount);

        try {
            String sql = "INSERT INTO blc_user (account_add, nick_name,email_add,phone_number,icon_image,personal_credo) VALUES (?,?,?,?,?,?)";
            jdbcTemplate.update(sql,acount, " "," ", " "," ", " ");

            // 可能会抛出异常的代码
        } catch (Exception e) {
            // 处理 ExceptionType1 类型的异常
            System.out.println(e);
        }
        return acount;
    }



}

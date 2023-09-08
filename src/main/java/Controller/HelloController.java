package Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class HelloController {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public HelloController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @RequestMapping(value="/hello123",method = RequestMethod.GET)
    @ResponseBody
    public String sayHello(){

        return "Hello, World!123 123";
    }


    @RequestMapping(value = "/addUsers2",method = RequestMethod.GET)
    @ResponseBody
    public String addUsers2(String username,String password) {
        String sql = "INSERT INTO user (username, password,email) VALUES (?,?,?)";
        jdbcTemplate.update(sql, username, password,"default@qq.com");
        return "添加用户成功！";
    }



}
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getUsers",method = RequestMethod.GET)
    @ResponseBody
    /*
     * List 里的对象是Map对象，而Map对象是
     * 由一个String类型的键和Object类型的值组成
     * */
    //查询用户
    public List<Map<String,Object>> getUsers(){
        String sql="select * from user";//SQL查询语句
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    //插入用户数据---get 请求
    @RequestMapping(value = "/addUsers",method = RequestMethod.GET)
    @ResponseBody
    public String addUser() {
        String sql = "INSERT INTO user (username, password,email) VALUES (?,?,?)";
        jdbcTemplate.update(sql, "zhangfei", "1234","zhangfei@qq.com");
        return "添加用户成功！";
    }

    //修改用户数据
    @RequestMapping(value = "/updateUser",method = RequestMethod.GET)
    @ResponseBody
    public String updateUser() {
        String sql = "UPDATE user SET username = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, "赵云", "zhaoyun@qq.com", 10);
        return "修改用户成功";
    }

    //删除用户
    @RequestMapping(value = "/deleteUsers",method = RequestMethod.GET)
    @ResponseBody
    public String deleteUser() {
        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, 8);
        return "删除用户成功！";
    }

}
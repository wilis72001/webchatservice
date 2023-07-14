package Controller;

import WebSocket.AccountGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class webchatRegisterController {
    @RequestMapping(value="/oneKeyRegister",method = RequestMethod.GET)
    @ResponseBody
    public String oneKeyRegister(){
        AccountGenerator ag = new AccountGenerator();
        String acount = ag.getString();
        System.out.println(acount);
        return "欢迎您！"+acount;
    }
}

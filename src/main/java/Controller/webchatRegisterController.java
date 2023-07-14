package Controller;

import WebSocket.AccountGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
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
    private final ResourceLoader resourceLoader;

    public webchatRegisterController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(value="/oneKeyRegister",method = RequestMethod.GET)
    @ResponseBody
    public String oneKeyRegister() throws IOException {
        AccountGenerator ag = new AccountGenerator();
        String acount = ag.getString();
        System.out.println(acount);
        Resource resource = resourceLoader.getResource("classpath:static/WSChat.html");
        return acount;
    }

    @RequestMapping(value="/getWSChat", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String getWSChat() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/WSChat.html");
        Path path = resource.getFile().toPath();
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        return content;
    }

}

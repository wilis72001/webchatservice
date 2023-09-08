package Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/vue-app")
    public String vueApp() {
        return "vue-app"; // 视图名称对应到包含Vue.js的HTML文件
    }
}


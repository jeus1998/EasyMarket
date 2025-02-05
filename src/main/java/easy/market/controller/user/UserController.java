package easy.market.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/join")
    public String join() {
        return "/user/join";
    }
    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }
}

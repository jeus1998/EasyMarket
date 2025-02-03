package easy.market.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FreePostController {
    @GetMapping("/posts")
    public String posts() {
        return "/freepost/posts";
    }
}

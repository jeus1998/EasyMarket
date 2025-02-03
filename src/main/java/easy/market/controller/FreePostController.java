package easy.market.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/free")
public class FreePostController {
    @GetMapping("/posts")
    public String posts() {
        return "/free/posts";
    }
}

package easy.market.controller.post;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/free/posts")
public class FreePostController {

    @GetMapping()
    public String posts(Pageable pageable, Model model) {
        model.addAttribute("pageable", pageable);
        return "/free/posts";
    }

    @GetMapping("/create")
    public String createPost() {
        return "/free/create";
    }

    @GetMapping("/{id}")
    public String post(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "/free/post";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "/free/edit";
    }
}

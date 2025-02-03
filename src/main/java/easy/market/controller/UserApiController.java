package easy.market.controller;

import easy.market.request.JoinRequest;
import easy.market.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {
    private final UserService userService;

    @PostMapping("/join")
    public String join(@Valid @RequestBody JoinRequest joinRequest) {
        userService.join(joinRequest);
        return "success";
    }

    @GetMapping("/logout")
    public String logout() {
        log.info("logout controller");
        return "success";
    }
}

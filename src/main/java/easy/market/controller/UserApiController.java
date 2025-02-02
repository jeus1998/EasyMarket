package easy.market.controller;

import easy.market.request.JoinRequest;
import easy.market.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping("/join")
    public String join(@Valid @RequestBody JoinRequest joinRequest) {
        userService.join(joinRequest);
        return "success";
    }
}

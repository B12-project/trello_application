package b12.trello.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        log.info("index");
        return "index";
    }

    @GetMapping("/users/page/login")
    public String loginPage() {
        log.info("loginPage");
        return "login";
    }

    @GetMapping("/users/page/signup")
    public String signupPage() {
        log.info("signupPage");
        return "signup";
    }

    @GetMapping("/page/board")
    public String boardPage(@RequestParam Long boardId) {
        log.info("signupPage");
        return "board";
    }
}

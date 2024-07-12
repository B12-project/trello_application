package b12.trello.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/users/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/users/signup-page")
    public String signupPage() {
        return "signup";
    }
}

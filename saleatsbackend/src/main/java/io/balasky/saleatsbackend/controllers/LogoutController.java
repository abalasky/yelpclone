package io.balasky.saleatsbackend.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public ModelAndView log_out(HttpServletRequest req) {

        HttpSession session = req.getSession();

        session.removeAttribute("username");

        return new ModelAndView("redirect:/");
    }
}

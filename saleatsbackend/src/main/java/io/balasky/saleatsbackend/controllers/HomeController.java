package io.balasky.saleatsbackend.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView get_home(HttpServletRequest req,            HttpServletResponse res) {
        ModelAndView mv = new ModelAndView("index");

        HttpSession session = req.getSession();

        String username = (String) session.getAttribute("username");

        if(username != null) {
            mv.addObject("username", username);
        }


        return mv;
    }

}

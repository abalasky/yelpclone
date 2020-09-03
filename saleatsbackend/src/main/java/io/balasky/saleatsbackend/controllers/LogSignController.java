package io.balasky.saleatsbackend.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.balasky.saleatsbackend.persistence.models.Greeting;
import io.balasky.saleatsbackend.persistence.models.LoginForm;
import io.balasky.saleatsbackend.persistence.models.SignUpForm;
import io.balasky.saleatsbackend.persistence.models.User;
import io.balasky.saleatsbackend.persistence.repositories.UserRepo;

@Controller
public class LogSignController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //Default route for login/signup
    @GetMapping("/login-sign-up")
    public ModelAndView get_login_signup(HttpServletRequest req) {

        //Get user session object
        HttpSession session = req.getSession();

        //If a user is already logged in redirect to home page
        if(session.getAttribute("username") != null) {
            return new ModelAndView("redirect:/");
        }

        //Create view and pass loginForm and signupForm objects
        ModelAndView mv = new ModelAndView("login-sign-up");
        mv.addObject("loginForm", new LoginForm());
        mv.addObject("signupForm", new SignUpForm());

        return mv;
    }

    //Route for log in form submission
    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute LoginForm loginForm, BindingResult    bindingResult, HttpServletRequest req) {

        //Get user session object
        HttpSession session = req.getSession();

        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("login-sign-up");
            mv.addObject("loginForm", new LoginForm());
            mv.addObject("signupForm", new SignUpForm());
			return mv;
		}
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        //Check if username is wrong - return error
        if(!userRepo.userExists(username)) {
            ModelAndView mv = new ModelAndView("login-sign-up");
            mv.addObject("loginError", "Username does not exist");
            mv.addObject("loginForm", new LoginForm());
            mv.addObject("signupForm", new SignUpForm());
            return mv;
        }

        //Check if password is wrong - return error
        if(!userRepo.userPasswordCorrect(username, password)) {
            ModelAndView mv = new ModelAndView("login-sign-up");
            mv.addObject("loginError", "Invalid password");
            mv.addObject("loginForm", new LoginForm());
            mv.addObject("signupForm", new SignUpForm());
            return mv;
        }

        //Valid login
        session.setAttribute("username", username);

        return new ModelAndView("redirect:/");

    }

    @PostMapping("/google-login")
    public ModelAndView google_login(@RequestBody User googleUser, HttpServletRequest req) {

        HttpSession session = req.getSession();

        //Google user already exists
        if(userRepo.userExists(googleUser.getEmail())) {
            //Set their session
            session.setAttribute("username", googleUser.getUsername());
            return new ModelAndView("redirect:/");
        }

        //Create user object
        User newUser = new User(googleUser.getUsername(), googleUser.getPassword(), googleUser.getEmail());

        //Add the user to the db
        userRepo.addUser(newUser);

        //Set their session
        session.setAttribute("username", googleUser.getUsername());


        return new ModelAndView("redirect:/");
    }

    //Route for Sign Up form submission
    @PostMapping("/sign-up")
    public ModelAndView sign_up(@ModelAttribute SignUpForm signupForm, BindingResult bindingResult, HttpServletRequest req) {

        //Get user session object
        HttpSession session = req.getSession();

        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("login-sign-up");
            mv.addObject("loginForm", new LoginForm());
            mv.addObject("signupForm", new SignUpForm());
            return mv;
        }

        //Get fields
        String username = signupForm.getUsername();
        String password = signupForm.getPassword();
        String email = signupForm.getEmail();
        String confirmPass = signupForm.getConfirmPassword();


        //Validate password - return error
        if(!password.equals(confirmPass)) {
            ModelAndView mv = new ModelAndView("login-sign-up");
            mv.addObject("signupError", "Passwords must be the same");
            mv.addObject("loginForm", new LoginForm());
            mv.addObject("signupForm", new SignUpForm());
            return mv;
        }

        //Check if email already exists - return error
        if(userRepo.userExists(email)) {
            ModelAndView mv = new ModelAndView("login-sign-up");
            mv.addObject("signupError", "Email already exists");
            mv.addObject("loginForm", new LoginForm());
            mv.addObject("signupForm", new SignUpForm());
            return mv;
        }

        //Hash the password
        String passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        //Create user object
        User newUser = new User(username, passwordHash, email);

        //Add the user to the db
        userRepo.addUser(newUser);

        //Set their session
        session.setAttribute("username", username);

        //Successful registration redirects to homepage
        return new ModelAndView("redirect:/");
    }
}
